package demo.pattern.proxy.staticproxy;

import demo.pattern.proxy.staticproxy.impl.RealPayToC;
import demo.pattern.proxy.staticproxy.impl.ToCPaymentImpl;

// 静态代理模式在编译时就将代码写入到对应的对象中
public class ProxyDemo {
    public static void main(String[] args) {
        ToCPayment toCPayment = new RealPayToC(new ToCPaymentImpl());
        toCPayment.pay();
    }
}
