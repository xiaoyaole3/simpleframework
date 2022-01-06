package org.simplespringframework.mvc;

import com.wander.controller.frontend.MainPageController;
import com.wander.controller.superadmin.HeadLineOperationController;
import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.aop.AspectWeaver;
import org.simplespringframework.core.BeanContainer;
import org.simplespringframework.inject.DependencyInjector;
import org.simplespringframework.mvc.processor.RequestProcessor;
import org.simplespringframework.mvc.processor.impl.ControllerRequestProcessor;
import org.simplespringframework.mvc.processor.impl.JspRequestProcessor;
import org.simplespringframework.mvc.processor.impl.PreRequestProcessor;
import org.simplespringframework.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 运行时需要在虚拟机Run中配置如下参数
 * clean tomcat7:run
 */
@Slf4j
@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

    private List<RequestProcessor> PROCESSORS = new ArrayList<>();

    @Override
    public void init(){
        // 1. 初始化容器
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.wander");
        new AspectWeaver().doAop2();
        new DependencyInjector().doIoc();

        // 2. 初始化请求处理器责任链, 这里必须要按照指定顺序
        PROCESSORS.add(new PreRequestProcessor());
        PROCESSORS.add(new StaticResourceRequestProcessor());
        PROCESSORS.add(new JspRequestProcessor());
        PROCESSORS.add(new ControllerRequestProcessor());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 创建责任链对象实例
        RequestProcessorChain requestProcessorChain = new RequestProcessorChain(PROCESSORS.iterator(), req, resp);
        // 2. 通过责任链模式来依次调用请求处理器对请求进行处理
        requestProcessorChain.doRequestProcessorChain();
        // 3. 对处理结果进行渲染
        requestProcessorChain.doRender();
    }
}
