package org.simplespringframework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyCreator {

    /**
     * 创建动态代理对象并返回
     * @param targetClass 被代理的class对象
     * @param methodInterceptor 方法拦截器
     * @return 被代理后的对象
     */
    public static Object createProxy(Class<?> targetClass, MethodInterceptor methodInterceptor) {
        return Enhancer.create(targetClass, methodInterceptor);
    }
}
