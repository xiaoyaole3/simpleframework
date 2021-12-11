package org.simplespringframework.inject;


import com.wander.controller.frontend.MainPageController;
import com.wander.service.combine.HeadShopCombineService;
import com.wander.service.combine.impl.HeadShopCombineServiceImpl;
import com.wander.service.combine.impl.HeadShopCombineServiceImpl2;
import com.wander.service.solo.impl.HeadLineServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simplespringframework.core.BeanContainer;

public class DependencyInjectorTest {

    @DisplayName(value = "依赖注入doIoc")
    @Test
    public void doIocTest() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.wander");
        Assertions.assertTrue(beanContainer.isLoaded());

        MainPageController mainPageController = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertTrue(mainPageController instanceof MainPageController);
        Assertions.assertNull(mainPageController.getHeadShopCombineService());

        new DependencyInjector().doIoc();

        Assertions.assertNotNull(mainPageController.getHeadShopCombineService());
        Assertions.assertTrue(mainPageController.getHeadShopCombineService() instanceof HeadShopCombineServiceImpl);
        Assertions.assertFalse(mainPageController.getHeadShopCombineService() instanceof HeadShopCombineServiceImpl2);


    }
}
