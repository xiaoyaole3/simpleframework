package com.wander.aspect;

import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.aop.annotation.Aspect;
import org.simplespringframework.aop.annotation.Order;
import org.simplespringframework.aop.aspect.DefaultAspect;
import org.simplespringframework.core.annotation.Controller;
import org.simplespringframework.core.annotation.Service;

import java.lang.reflect.Method;

@Slf4j
//@Aspect(pointcut = "within(org.simpleframework.core.annotation.Component)")
@Order(value = 1)
public class ServiceTimeCalculatorAspect extends DefaultAspect {
    private long timestampCache;

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
      log.info("[Service]Start timing : class[{}], method[{}], args[{}]", targetClass, method, args);
      timestampCache = System.currentTimeMillis();
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        long endTime = System.currentTimeMillis();
        long costTime = endTime - timestampCache;
        log.info("[Service]End timing : class[{}], method[{}], args[{}], cost time = {}", targetClass, method, args, costTime);
        return returnValue;
    }
}
