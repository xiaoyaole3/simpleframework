package org.simplespringframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.annotation.RequestMapping;
import org.simplespringframework.mvc.annotation.RequestParam;
import org.simplespringframework.mvc.annotation.ResponseBody;
import org.simplespringframework.mvc.processor.RequestProcessor;
import org.simplespringframework.mvc.render.ResultRender;
import org.simplespringframework.mvc.render.impl.JsonResultRender;
import org.simplespringframework.mvc.render.impl.ResourceNotFoundResultRender;
import org.simplespringframework.mvc.render.impl.ViewResultRender;
import org.simplespringframework.mvc.type.ControllerMethod;
import org.simplespringframework.mvc.type.RequestPathInfo;
import org.simplespringframework.util.ConvertUtil;
import org.simplespringframework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller请求处理器
 */
@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {

    // IOC容器
    private BeanContainer beanContainer;

    // 请求和Controller方法的映射集合
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap = new ConcurrentHashMap<>();

    /**
     * 依靠容器的能力，建立起请求路径、请求方法与Controller方法实例的映射
     */
    public ControllerRequestProcessor() {
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtil.isEmpty(requestMappingSet)) {
            return;
        }
        // 1. 遍历所有被@RequestMapping标记的类，获取类上面该注解的属性值作为一级路径
        for (Class<?> requestMappingClass : requestMappingSet) {
            RequestMapping requestMappingClassAnnotation = requestMappingClass.getAnnotation(RequestMapping.class);
            String basePath = requestMappingClassAnnotation.value();
            if (!basePath.startsWith("/")) {
                basePath = "/" + basePath;
            }

            // 2. 遍历类里所有被@RequestMapping标记的方法，获取方法上面该注解的属性值，作为二级路径
            Method[] declaredMethods = requestMappingClass.getDeclaredMethods();
            if (ValidationUtil.isEmpty(declaredMethods)) {
                continue;
            }
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                    String methodPath = methodAnnotation.value();
                    if (!methodPath.startsWith("/")) {
                        methodPath = "/" + methodPath;
                    }
                    String url = basePath + methodPath;

                    // 3. 解析方法里被@RequestParam标记的参数
                    // 获取该注解的属性值，作为参数名，
                    // 获取被标记的参数的数据类型，简历参数名和参数类型的映射
                    HashMap<String, Class<?>> methodParams = new HashMap<>();
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtil.isEmpty(parameters)) {
                        for (Parameter parameter : parameters) {
                            RequestParam paramAnnotation = parameter.getAnnotation(RequestParam.class);
                            // 为了实现简单，目前暂定为Controller方法里面所有的参数都需要@RequestParam注解
                            if (paramAnnotation == null) {
                                throw new RuntimeException("The parameter must have @RequestParam.");
                            }
                            methodParams.put(paramAnnotation.value(), parameter.getType());
                        }
                    }

                    // 4. 将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放置到映射表里
                    String httpMethod = String.valueOf(methodAnnotation.method());
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    if (this.pathControllerMethodMap.containsKey(requestPathInfo)) {
                        log.warn("duplicate url:{} registration, current class {} method {} will override the former one",
                                requestPathInfo.getHttpPath(), requestMappingClass.getName(), method.getName());

                    }
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    this.pathControllerMethodMap.put(requestPathInfo, controllerMethod);

                }
            }
        }



    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1. 解析HttpServletRequest的请求方法，请求路径，获取对应的ControllerMethod实例
        String method = requestProcessorChain.getRequestMethod();
        String requestPath = requestProcessorChain.getRequestPath();
        ControllerMethod controllerMethod = this.pathControllerMethodMap.get(new RequestPathInfo(method, requestPath));
        if (controllerMethod == null) {
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method, requestPath));
            return false;
        }
        // 2. 解析请求参数，并传递给获取到的ControllerMethod实例去执行
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        // 3. 根据解析的结果，选择对应的render进行渲染
        setResultRender(result, controllerMethod, requestProcessorChain);
        return true;
    }

    /**
     * 根据不同的情况设置不同的渲染器
     * @param result 结果
     * @param controllerMethod Controller方法对象
     * @param requestProcessorChain 请求链
     */
    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null) {
            return;
        }
        ResultRender resultRender;
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            resultRender = new JsonResultRender(result);
        } else {
            resultRender = new ViewResultRender(result);
        }
        requestProcessorChain.setResultRender(resultRender);
    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        // 1. 从请求里获取GET或者POST的参数名及其对应的值
        Map<String, String> requestParamMap = new HashMap<>();
        // 该方法只能支持获取get和post类型的值
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> parameter: parameterMap.entrySet()) {
            if (!ValidationUtil.isEmpty(parameter.getValue())) {
                // 为了实现的简单，这里只支持一个参数对应一个值的形式
                requestParamMap.put(parameter.getKey(), parameter.getValue()[0]);
            }
        }
        // 2. 根据获取到的请求参数名及其对应的值，以及controllerMethod里面的参数和类型的映射关系，去实例化出方法对应的桉树
        List<Object> methodParams = new ArrayList<>();
        Map<String, Class<?>> methodParamMap = controllerMethod.getMethodParameters();
        for (String paramName : methodParamMap.keySet()) {
            Class<?> type = methodParamMap.get(paramName);
            String requestValue = requestParamMap.get(paramName);

            // 为了实现上的简单，只支持String以及基础类型char,int,short,byte,double,long,float,boolean及他们的包装类型
            Object value;
            if (requestValue == null) {
                // 将请求里的参数值转成适配于参数类型的空值
                value = ConvertUtil.primitiveNull(type);
            } else {
                value = ConvertUtil.convert(type, requestValue);
            }
            methodParams.add(value);
        }

        // 3. 执行Controller里面对应的方法并返回结果
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);

        Object result;
        try {
            if (methodParams.size() == 0) {
                // 没有参数
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (InvocationTargetException e) {
            // 如果是调用异常的话可以通过 e.getTargetException()来获取执行方法抛出的异常
            throw new RuntimeException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
