package org.simplespringframework.aop;

import com.wander.controller.superadmin.HeadLineOperationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.inject.DependencyInjector;

class AspectWeaverTest {

    @DisplayName(value = "织入逻辑测试 : doAop1.0")
    @Test
    public void doAop1Test() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.wander");

        // 这里应该是先 Aop 再 依赖注入，因为希望依赖注入的bean也是Aop过的bean
        new AspectWeaver().doAop1();
        new DependencyInjector().doIoc();

        HeadLineOperationController headLineOperationController = (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);
//        headLineOperationController.addHeadLine(null, null);
    }

    @DisplayName(value = "织入逻辑测试 : doAop2.0")
    @Test
    public void doAop2Test() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.wander");

        // 这里应该是先 Aop 再 依赖注入，因为希望依赖注入的bean也是Aop过的bean
        new AspectWeaver().doAop2();
        new DependencyInjector().doIoc();

        HeadLineOperationController headLineOperationController = (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);
//        headLineOperationController.addHeadLine(null, null);
    }
}