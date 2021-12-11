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
import java.util.*;
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

    /**
     * 添加一个class对象及其Bean实例
     * @param clazz Class对象
     * @param bean Bean实例
     * @return 原有的Bean实例，没有则返回null
     */
    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }

    /**
     * 移除一个IOC容器管理的对象
     * @param clazz Class对象
     * @return 删除的Bean实例，没有则返回null
     */
    public Object removeBean(Class<?> clazz) {
        return beanMap.remove(clazz);
    }

    /**
     * 根据Class对象获取Bean实例
     * @param clazz Class对象
     * @return Bean实例
     */
    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    /**
     * 获取所有Bean集合
     * @return Bean集合
     */
    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    /**
     * 获取所有Bean的集合
     * @return Bean集合
     */
    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    /**
     * 根据注解筛选出Bean的Class集合
     * @param annotation 注解
     * @return Class集合
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        // 1. 获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("Nothing in beanMap");
            return null;
        }
        // 2. 通过注解筛选被注解标记的class对象，并添加到ClassSet里
        HashSet<Class<?>> classHashSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            // 类是否有相同的注解标记
            if (clazz.isAnnotationPresent(annotation)) {
                classHashSet.add(clazz);
            }
        }
        return classHashSet.size() > 0 ? classHashSet : null;
    }


    /**
     * 通过接口或者父类获取实现类或者子类的Class集合，不包括其本身
     * @param interfaceOrClass 接口Class或者父类Class
     * @return Class集合
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        // 1. 获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("Nothing in beanMap");
            return null;
        }
        // 2. 判断keySet里的元素是否是传入的接口或者类的子类，如果是，就将其添加到classSet里
        HashSet<Class<?>> classHashSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            // 由于FirstClass.isAssignableFrom(FirstClass) = true,因此这里还需要排除其本身
            if (interfaceOrClass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrClass)) {
                classHashSet.add(clazz);
            }
        }
        return classHashSet.size() > 0 ? classHashSet : null;
    }
}
