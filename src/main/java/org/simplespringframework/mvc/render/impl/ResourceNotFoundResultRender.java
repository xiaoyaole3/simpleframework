package org.simplespringframework.mvc.render.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源找不到时使用的渲染器
 */
public class ResourceNotFoundResultRender implements ResultRender {

    private String httpMethod;
    private String httpPath;


    public ResourceNotFoundResultRender(String method, String requestPath) {
        this.httpMethod = method;
        this.httpPath = requestPath;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                "Can not find resource, httpPath = [" + httpPath + "], httpMethod = [" + httpMethod + "]");
    }
}
