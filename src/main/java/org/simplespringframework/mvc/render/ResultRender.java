package org.simplespringframework.mvc.render;

import org.simplespringframework.mvc.RequestProcessorChain;

/**
 * 渲染请求结果
 */
public interface ResultRender {
    // 执行渲染
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
