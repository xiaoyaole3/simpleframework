package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class RealPayMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        beforePayment();
        Object result = methodProxy.invokeSuper(o, args);
        afterPayment();
        return result;
    }

    private void afterPayment() {
        System.out.println("[cglib proxy]Real pay after.");
    }

    private void beforePayment() {
        System.out.println("[cglib proxy]Real pay before.");
    }
}
