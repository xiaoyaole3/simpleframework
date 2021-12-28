package org.simplespringframework.aop;

import com.wander.controller.superadmin.HeadLineOperationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.inject.DependencyInjector;

import static org.junit.jupiter.api.Assertions.*;

class AspectWeaverTest {

    @DisplayName(value = "织入逻辑测试 : doAop")
    @Test
    public void doAopTest() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.wander");

        // 这里应该是先 Aop 再 依赖注入，因为希望依赖注入的bean也是Aop过的bean
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();

        HeadLineOperationController headLineOperationController = (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);
        headLineOperationController.addHeadLine(null, null);
    }

}