package org.simplespringframework.mvc.processor;

import org.simplespringframework.mvc.RequestProcessorChain;

/**
 * 请求执行器
 */
public interface RequestProcessor {

    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;
}
