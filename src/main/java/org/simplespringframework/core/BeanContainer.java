package org.simplespringframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.annotation.Component;
import org.simplespringframework.core.annotation.Controller;
import org.simplespringframework.core.annotation.Repository;
import org.simplespringframework.core.annotation.Service;
import org.simplespringframework.util.ClassUtil;
import org.simplespringframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    /**
     * 存放所有被配置标记的目标对象的Map
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();
    /**
     * 加载bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS
            = Arrays.asList(Component.class, Controller.class, Service.class, Repository.class);
    /**
     * 容器是否已经加载过bean
     */
    private boolean loaded = false;
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Bean 实例数量
     * @return 数量
     */
    public int size() {
        return beanMap.size();
    }



    /**
     * 获取Bean容器实例
     * @return BeanContainer
     */
    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;

        private BeanContainer instance;
        ContainerHolder() {
            instance = new BeanContainer();
        }
    }


    /**
     * 扫描加载所有Bean
     */
    public synchronized void loadBeans(String packageName) {
        // 判断bean容器是否被加载过
        if (isLoaded()) {
            log.warn("BeanContainer has been loaded");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("Extract nothing from packageName {}", packageName);
            return;
        }

        for (Class<?> aClass : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATIONS) {
                // 如果类上面标记了对应的注解
                if (aClass.isAnnotationPresent(annotation)) {
                    // 将目标类本身作为键，目标类的实例作为值，放入到beanMap中
                    beanMap.put(aClass, ClassUtil.newInstance(aClass, true));
                }
            }
        }

        loaded = true;
    }
}
