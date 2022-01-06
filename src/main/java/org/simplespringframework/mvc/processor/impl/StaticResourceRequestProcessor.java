package org.simplespringframework.mvc.processor.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 静态资源请求处理，包括但不限于图片、css、以及js文件等
 */
public class StaticResourceRequestProcessor implements RequestProcessor {
    private static final String DEFAULT_TOMCAT_SERVLET = "default";
    private static final String STATIC_RESOURCE_PREFIX = "/static/";

    // tomcat默认请求派发器RequestDispatcher的名称
    RequestDispatcher defaultDispatcher;

    public StaticResourceRequestProcessor(ServletContext servletContext) {
        this.defaultDispatcher = servletContext.getNamedDispatcher(DEFAULT_TOMCAT_SERVLET);
        if (this.defaultDispatcher == null) {
            throw new RuntimeException("There is no default tomcat servlet.");
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1. 通过请求路径判断是否是请求的静态资源 webapp/static
        if (isStaticResource(requestProcessorChain.getRequestPath())) {
            // 2. 如果是静态资源，则将请求转发给default servlet处理
            defaultDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
        }
        return true;
    }

    // 通过请求路径前缀（目录）是否为静态资源/static/
    private boolean isStaticResource(String path) {
        return path.startsWith(STATIC_RESOURCE_PREFIX);
    }
}
