package demo.pattern.proxy.staticproxy.impl;

import demo.pattern.proxy.staticproxy.ToCPayment;

public class RealPayToC implements ToCPayment {
    private ToCPayment toCPayment;

    public RealPayToC(ToCPayment toCPayment) {
        this.toCPayment = toCPayment;
    }

    @Override
    public void pay() {
        beforePayment();
        toCPayment.pay();
        afterPayment();
    }

    private void afterPayment() {
        System.out.println("Real pay after.");
    }

    private void beforePayment() {
        System.out.println("Real pay before.");
    }
}
