package demo.pattern.proxy.staticproxy.impl;

import demo.pattern.proxy.staticproxy.ToCPayment;

public class ToCPaymentImpl implements ToCPayment {
    @Override
    public void pay() {
        System.out.println("ToC payment impl.");
    }
}
