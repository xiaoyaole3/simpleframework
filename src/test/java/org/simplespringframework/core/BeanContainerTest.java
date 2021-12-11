package org.simplespringframework.core;

import com.wander.controller.DispatcherServlet;
import com.wander.controller.frontend.MainPageController;
import com.wander.service.solo.HeadLineService;
import com.wander.service.solo.impl.HeadLineServiceImpl;
import org.junit.jupiter.api.*;
import org.simplespringframework.core.annotation.Controller;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {
    private static BeanContainer beanContainer;

    @BeforeAll
    static void init() {
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName(value = "加载目标类以及其实例到BeanContainer, loadBeansTest")
    @Order(1)
    @Test
    public void loadBeansTest() {
        Assertions.assertFalse(beanContainer.isLoaded());
        beanContainer.loadBeans("com.wander");
        Assertions.assertEquals(6, beanContainer.size());
        Assertions.assertTrue(beanContainer.isLoaded());
    }

    @DisplayName(value = "根据类获取其实例")
    @Order(2)
    @Test
    public void getBeanTest() {
        MainPageController controller = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertNotNull(controller);
        Assertions.assertTrue(controller instanceof MainPageController);

        DispatcherServlet dispatcherServlet = (DispatcherServlet) beanContainer.getBean(DispatcherServlet.class);
        Assertions.assertNull(dispatcherServlet);
    }

    @DisplayName(value = "根据注解获取其实例")
    @Order(3)
    @Test
    public void getClassesByAnnotationTest() {
        Assertions.assertTrue(beanContainer.isLoaded());
        Assertions.assertEquals(3, beanContainer.getClassesByAnnotation(Controller.class).size());
    }

    @DisplayName(value = "根据接口获取其实现类")
    @Order(3)
    @Test
    public void getClassesBySuperTest() {
        Assertions.assertTrue(beanContainer.isLoaded());
        Assertions.assertTrue(beanContainer.getClassesBySuper(HeadLineService.class).contains(HeadLineServiceImpl.class));
    }
}
