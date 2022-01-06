package org.simplespringframework.mvc.processor.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.processor.RequestProcessor;

/**
 * 请求预处理，包括编码以及路径处理
 */
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
