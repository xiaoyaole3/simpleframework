package org.simplespringframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simplespringframework.aop.aspect.AspectInfo;
import org.simplespringframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AspectListExecutor implements MethodInterceptor {
    // 被代理的类
    private Class<?> targetClass;
    // 切面对象列表信息
    @Getter
    private List<AspectInfo> sortedAspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        
        // 对aspectInfoList排序
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    /**
     * 按照order的值进行升序排序，确保order值小的aspect先被织入
     * @param aspectInfoList 切面对象列表信息
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        // 根据aspectInfo中的信息对数据进行精筛
        collectAccurateMatchedAspectList(method);
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            return returnValue = methodProxy.invokeSuper(proxy, args);
        }

        // 1. 按照order的顺序升序执行完所有Aspect的before方法
        invokeBeforeAdvices(method, args);
        // 2. 执行被代理类的方法
        try {
            returnValue = methodProxy.invokeSuper(proxy, args);
            // 3. 如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
            returnValue = invokeAfterReturningAdvices(method, args, returnValue);
        } catch (Exception e) {
            // 4. 如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method, args, e);
        }

        return returnValue;
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            return;
        }

        Iterator<AspectInfo> iterator = sortedAspectInfoList.iterator();
        while (iterator.hasNext()) {
            AspectInfo next = iterator.next();
            if (!next.getPointcutLocator().accurateMatches(method)) {
                iterator.remove();
            }
        }
    }

    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            sortedAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass, method, args, e);
        }
    }

    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result = null;
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            result = sortedAspectInfoList.get(i).getAspectObject().afterReturning(targetClass, method, args, returnValue);
        }
        return result;
    }

    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : sortedAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }
    }
}
