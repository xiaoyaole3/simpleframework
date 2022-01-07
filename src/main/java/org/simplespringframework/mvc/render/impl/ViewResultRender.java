package org.simplespringframework.mvc.render.impl;

import org.simplespringframework.mvc.RequestProcessorChain;
import org.simplespringframework.mvc.render.ResultRender;
import org.simplespringframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 页面渲染器
 */
public class ViewResultRender implements ResultRender {

    public static final String VIEW_PATH = "/templates/";
    private ModelAndView modelAndView;

    public ViewResultRender(Object result) {
        // 1. 如果入参类型是ModelAndView，则直接赋值给成员变量
        if (result instanceof ModelAndView) {
            this.modelAndView = ((ModelAndView) result);
        } else if (result instanceof String) {
            // 2. 如果入参类型是String，则为视图，需要包装后才赋值给成员变量
            this.modelAndView = new ModelAndView().setView(((String) result));
        } else {
            // 3. 针对其他情况，抛出异常
            throw new RuntimeException("Illegal request result type");
        }
    }

    /**
     * 将请求处理结果按照视图路径转发至对应视图进行展示
     * @param requestProcessorChain
     * @throws Exception
     */
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();

        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        // JSP视图展示
        request.getRequestDispatcher(VIEW_PATH + path).forward(request, response);
    }
}
