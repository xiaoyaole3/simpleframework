package org.simplespringframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.annotation.RequestMapping;
import org.simplespringframework.mvc.annotation.RequestParam;
import org.simplespringframework.mvc.processor.RequestProcessor;
import org.simplespringframework.mvc.type.ControllerMethod;
import org.simplespringframework.mvc.type.RequestPathInfo;
import org.simplespringframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
                    if (ValidationUtil.isEmpty(parameters)) {
                        continue;
                    }
                    for (Parameter parameter : parameters) {
                        RequestParam paramAnnotation = parameter.getAnnotation(RequestParam.class);
                        // 为了实现简单，目前暂定为Controller方法里面所有的参数都需要@RequestParam注解
                        if (paramAnnotation == null) {
                            throw new RuntimeException("The parameter must have @RequestParam.");
                        }
                        methodParams.put(paramAnnotation.value(), parameter.getType());


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



    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
