package org.simplespringframework.mvc.processor.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;


/**
 * jsp资源请求处理
 */
public class JspRequestProcessor implements RequestProcessor {

    // jsp请求的RequestDispatcher的名称
    private static final String JSP_SERVLET = "jsp";
    // Jsp请求资源路径前缀
    private static final String JSP_RESOURCE_PREFIX = "/templates/";

    /**
     * jsp的RequestDispatcher，处理jsp资源
     */
    private RequestDispatcher jspServlet;

    public JspRequestProcessor(ServletContext servletContext) {
        jspServlet = servletContext.getNamedDispatcher(JSP_SERVLET);
        if (jspServlet == null) {
            throw new RuntimeException("There is no jsp servlet.");
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        if (isJspResource(requestProcessorChain.getRequestPath())) {
            jspServlet.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    private boolean isJspResource(String requestPath) {
        return requestPath.startsWith(JSP_RESOURCE_PREFIX);
    }
}
