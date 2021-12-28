package demo.pattern.proxy.staticproxy;

import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;
import demo.pattern.proxy.jdkproxy.RealPayInvocationHandler;
import demo.pattern.proxy.staticproxy.impl.RealPayToC;
import demo.pattern.proxy.staticproxy.impl.ToCPaymentImpl;

// 静态代理模式在编译时就将代码写入到对应的对象中
public class ProxyDemo {
    public static void main(String[] args) {
//        staticProxy();
        jdkDynamicProxy();
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
}
