package demo.pattern.proxy.staticproxy.impl;

// 没有实现任何接口的类
// Cglib是支持不实现任何接口的动态代理的
public class CommonPayment {
    public void pay() {
        System.out.println("common payment.");
    }
}
