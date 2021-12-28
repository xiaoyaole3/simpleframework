package demo.pattern.proxy;

import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.cglib.RealPayMethodInterceptor;
import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;
import demo.pattern.proxy.jdkproxy.RealPayInvocationHandler;
import demo.pattern.proxy.staticproxy.ToCPayment;
import demo.pattern.proxy.staticproxy.impl.CommonPayment;
import demo.pattern.proxy.staticproxy.impl.RealPayToC;
import demo.pattern.proxy.staticproxy.impl.ToCPaymentImpl;

// 静态代理模式在编译时就将代码写入到对应的对象中
public class ProxyDemo {
    public static void main(String[] args) {
//        staticProxy();
//        jdkDynamicProxy();
        cglibProxy();
    }

    private static void staticProxy() {
        ToCPayment toCPayment = new RealPayToC(new ToCPaymentImpl());
        toCPayment.pay();
    }

    private static void jdkDynamicProxy() {
        ToCPayment toCPayment = new ToCPaymentImpl();
        RealPayInvocationHandler handler = new RealPayInvocationHandler(toCPayment);
        ToCPayment proxyInstance = JdkDynamicProxyUtil.newProxyInstance(toCPayment, handler);
        proxyInstance.pay();
    }

    /**
     * cglib 同时可以实现对于实现了相同接口和没有实现任何接口的织入
     */
    private static void cglibProxy() {
        /**
         * 说明了Jdk动态代理机制是通过让动态代理类和被代理类实现同样的接口来实现的
         * Exception in thread "main" java.lang.ClassCastException: class com.sun.proxy.$Proxy0 cannot be cast to class demo.pattern.proxy.staticproxy.impl.CommonPayment (com.sun.proxy.$Proxy0 and demo.pattern.proxy.staticproxy.impl.CommonPayment are in unnamed module of loader 'app')
         * 	at demo.pattern.proxy.ProxyDemo.cglibProxy(ProxyDemo.java:33)
         * 	at demo.pattern.proxy.ProxyDemo.main(ProxyDemo.java:15)
         */
        CommonPayment commonPayment = new CommonPayment();
//        RealPayInvocationHandler invocationHandler = new RealPayInvocationHandler(commonPayment);
//        CommonPayment commonPaymentProxy = JdkDynamicProxyUtil.newProxyInstance(commonPayment, invocationHandler);
//        commonPaymentProxy.pay();

        RealPayMethodInterceptor methodInterceptor = new RealPayMethodInterceptor();
        CommonPayment cglibCommonPaymentProxy = CglibUtil.createProxy(commonPayment, methodInterceptor);
        cglibCommonPaymentProxy.pay();

    }
}
