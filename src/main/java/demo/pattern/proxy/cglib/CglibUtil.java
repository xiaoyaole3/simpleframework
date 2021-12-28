package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibUtil {
    public static <T> T createProxy(T target, MethodInterceptor methodInterceptor) {
        return ((T) Enhancer.create(target.getClass(), methodInterceptor));
    }
}
