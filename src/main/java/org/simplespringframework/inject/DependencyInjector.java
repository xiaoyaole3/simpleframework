package org.simplespringframework.inject;

import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.inject.annotation.Autowired;
import org.simplespringframework.util.ClassUtil;
import org.simplespringframework.util.ValidationUtil;

import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class DependencyInjector {
    /**
     * Bean容器
     */
    private BeanContainer beanContainer;
    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行IOC
     */
    public void doIoc() {
        // 1. 变量Bean容器中所有的Class对象
        if (ValidationUtil.isEmpty(beanContainer.getClasses())) {
            log.warn("Empty class Set in BeanContainer.");
            return;
        }
        for (Class<?> clazz : beanContainer.getClasses()) {
            // 2. 遍历Class对象的所有成员变量
            Field[] fields = clazz.getDeclaredFields();
            if (ValidationUtil.isEmpty(fields)) {
                continue;
            }
            for (Field field : fields) {
                // 3. 找出被Autowired标记的成员变量
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    // 4. 获取这些成员变量的类型
                    Class<?> filedClass = field.getType();
                    // 5. 获取和谐成员变量的类型在容器里对应的实例
                    Object fieldValue = getFieldInstance(filedClass, autowiredValue);
                    if (fieldValue == null) {
                        throw new RuntimeException("Unable to inject relevant type, target fieldClass is : {}" + filedClass.getName() + " " + autowiredValue);
                    } else {
                        // 6. 通过反射将对应的成员变量实例注入到成员变量所在类的实例里
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setField(field, targetBean, fieldValue, true);
                    }
                }
            }
        }

    }

    /**
     * 根据Class在beanContainer里获取其实例或者实现类
     * @param filedClass 类
     * @param autowiredValue Autowired中value的值
     * @return 类的实例
     */
    private Object getFieldInstance(Class<?> filedClass, String autowiredValue) {
        Object fieldValue = beanContainer.getBean(filedClass);
        if (fieldValue != null) {
            return fieldValue;
        } else {
            Class<?> implementedClass = getImplementClass(filedClass, autowiredValue);
            if (implementedClass != null) {
                return beanContainer.getBean(implementedClass);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取接口的实现类
     *
     * 原本Spring是通过@Qualified注解和@Autowired注解一同标记对象，这里进行了简化，给Autowired加上value属性
     *
     * @param filedClass 接口类
     * @return 其实现类
     */
    private Class<?> getImplementClass(Class<?> filedClass, String autowiredValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(filedClass);
        if (!ValidationUtil.isEmpty(classSet)) {
            if (ValidationUtil.isEmpty(autowiredValue)) {
                if (classSet.size() == 1) {
                    return classSet.iterator().next();
                } else {
                    throw new RuntimeException("Multiple implemented classes for " + filedClass.getName() + " please set @Autowired's value to pick one.");
                }
            } else {
                for (Class<?> clazz : classSet) {
                    if (autowiredValue.equals(clazz.getSimpleName())) {
                        return clazz;
                    }
                }
            }
        }
        return null;
    }
}
