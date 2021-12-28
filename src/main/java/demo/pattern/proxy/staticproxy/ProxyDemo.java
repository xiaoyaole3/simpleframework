package demo.pattern.proxy.staticproxy;

import demo.pattern.proxy.staticproxy.impl.RealPayToC;
import demo.pattern.proxy.staticproxy.impl.ToCPaymentImpl;

public class ProxyDemo {
    public static void main(String[] args) {
        ToCPayment toCPayment = new RealPayToC(new ToCPaymentImpl());
        toCPayment.pay();
    }
}
