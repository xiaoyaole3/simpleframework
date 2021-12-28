package demo.pattern.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RealPayInvocationHandler implements InvocationHandler {
    private Object targetObject;

    public RealPayInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforePayment();
        Object result = method.invoke(targetObject, args);
        afterPayment();
        return result;
    }


    private void afterPayment() {
        System.out.println("[JDK proxy]Real pay after.");
    }

    private void beforePayment() {
        System.out.println("[JDK proxy]Real pay before.");
    }
}
