package com.wander.aspect;

import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.aop.annotation.Aspect;
import org.simplespringframework.aop.annotation.Order;
import org.simplespringframework.aop.aspect.DefaultAspect;
import org.simplespringframework.core.annotation.Controller;
import org.simplespringframework.core.annotation.Service;

import java.lang.reflect.Method;

@Slf4j
//@Aspect(value = Service.class)
//@Aspect(pointcut = "within(org.simpleframework.core.annotation.Component)")
@Order(value = 9)
public class ServiceInfoRecordAspect extends DefaultAspect {

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        log.info("[Service]Record start : class[{}], method[{}], args[{}]", targetClass, method, args);
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        log.info("[Service]Record end : class[{}], method[{}], args[{}], returnValue[{}]", targetClass, method, args, returnValue);
        return returnValue;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable {
        log.error("[Service]Record exception : class[{}], method[{}], args[{}], exception[{}]", targetClass, method, args, e.getMessage());
    }
}
