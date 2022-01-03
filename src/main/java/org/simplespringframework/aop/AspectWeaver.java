package org.simplespringframework.aop;

import org.simplespringframework.aop.annotation.Aspect;
import org.simplespringframework.aop.annotation.Order;
import org.simplespringframework.aop.aspect.AspectInfo;
import org.simplespringframework.aop.aspect.DefaultAspect;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class AspectWeaver {
    private BeanContainer beanContainer;
    public AspectWeaver() {
        this.beanContainer = BeanContainer.getInstance();
    }

    /**
     * 使用cglib动态代理方式进行AOP
     */
    public void doAop1() {
        // 1. 获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        // 2. 将切面类按照不同的织入目标进行切分
        Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap = new HashMap<>();
        if (ValidationUtil.isEmpty(aspectSet)) {
            return;
        }
        for (Class<?> aspectClass : aspectSet) {
            if (verifyAspect(aspectClass)) {
                categorizeAspect(categorizedMap, aspectClass);
            } else {
                throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class, " +
                        "or Aspect class does not extend from DefaultAspect, or the value in Aspect Tag equals @Aspect.");
            }
        }
        // 3. 按照不同的织入目标分别去按序织入Aspect的逻辑
        if (ValidationUtil.isEmpty(categorizedMap)) {
            return;
        }
        for (Class<? extends Annotation> category : categorizedMap.keySet()) {
            weaveByCategory(category, categorizedMap.get(category));
        }
    }

    /**
     * 使用AspectJ的方式进行AOP
     */
    public void doAop2() {
        // 1. 获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)) {
            return;
        }
        // 2. 拼装AspectInfoList
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);
        // 3. 遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses();
        for (Class<?> targetClass : classSet) {
            // 排除AspectClass自身
            if (targetClass.isAnnotationPresent(Aspect.class)) {
                continue;
            }
            // 4. 粗筛符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, targetClass);
            // 5. 尝试进行Aspect的织入
            wrapIfNecessary(roughMatchedAspectList, targetClass);
        }

    }

    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        // 1. 创建动态代理对象
        AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
        // 2. 将动态代理对象实例添加到容器里，取代未被代理前的类实例
        beanContainer.addBean(targetClass, proxyBean);
    }


    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> result = new ArrayList<>();

        for (AspectInfo aspectInfo : aspectInfoList) {
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)) {
                result.add(aspectInfo);
            }
        }
        return result;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();

        for (Class<?> aspectClass : aspectSet) {
            Order orderTag = aspectClass.getAnnotation(Order.class);
            Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
            String expression = aspectTag.pointcut();

            DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);
            PointcutLocator pointcutLocator = new PointcutLocator(expression);

            AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect, pointcutLocator);
            aspectInfoList.add(aspectInfo);
        }
        return aspectInfoList;
    }

    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfos) {
        // 1. 获取被代理类的集合
        Set<Class<?>> classSet = beanContainer.getClassesByAnnotation(category);
        if (ValidationUtil.isEmpty(classSet)) {
            return;
        }
        // 2. 遍历被代理类，分别为每个被代理类生成动态代理实例
        for (Class<?> targetClass : classSet) {
            // 创建动态代理对象
            AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfos);
            Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
            // 3. 将动态代理对象实例添加到容器里，取代未被代理前的类实例
            beanContainer.addBean(targetClass, proxyBean);
        }


    }

    // 将切面类按照不同的织入目标进行切分
    private void categorizeAspect(Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap, Class<?> aspectClass) {
        Order orderTag = aspectClass.getAnnotation(Order.class);
        Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);

        DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);
        AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);

        if (!categorizedMap.containsKey(aspectTag.value())) {
            // 如果织入的joinPoint第一次出现，则以该joinPoint为key，以新创建的List<AspectInfo>为value
            ArrayList<AspectInfo> aspectInfos = new ArrayList<>();
            aspectInfos.add(aspectInfo);
            categorizedMap.put(aspectTag.value(), aspectInfos);
        } else {
            // 如果织入的joinPoint不是第一次出现，则往joinPoint对应的value中添加新的Aspect切面
            List<AspectInfo> aspectInfos = categorizedMap.get(aspectTag.value());
            aspectInfos.add(aspectInfo);
        }


    }

    /**
     * 框架中一定要遵守给Aspect类添加@Aspect和@Order标签的规范，同时，必须继承自DefaultAspect.class
     * 此外，@Aspect的属性值不能是它本身
     * @param aspectClass 待检查的对象
     * @return
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass) &&
                !aspectClass.getAnnotation(Aspect.class).value().equals(Aspect.class);
    }
}
