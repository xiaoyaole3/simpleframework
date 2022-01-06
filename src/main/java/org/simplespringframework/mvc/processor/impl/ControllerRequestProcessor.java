package org.simplespringframework.mvc.processor.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.processor.RequestProcessor;

/**
 * Controller请求处理器
 */
public class ControllerRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
