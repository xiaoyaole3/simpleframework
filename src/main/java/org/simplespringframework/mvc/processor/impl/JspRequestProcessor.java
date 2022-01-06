package org.simplespringframework.mvc.processor.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.processor.RequestProcessor;


/**
 * jsp资源请求处理
 */
public class JspRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
