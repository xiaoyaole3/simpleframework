package org.simplespringframework.mvc.render.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * 内部错误渲染器
 */
public class InternalErrorResultRender implements ResultRender {

    private String errorMsg;

    public InternalErrorResultRender(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
    }
}
