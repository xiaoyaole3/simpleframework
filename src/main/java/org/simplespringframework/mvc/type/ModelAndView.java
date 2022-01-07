package org.simplespringframework.mvc.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储处理完后的结果数据，以及显示该数据的视图
 */
@Getter
public class ModelAndView {
    // 页面所在路径
    private String view;
    // 页面的data数据
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    // 返回对象本身是为了调用链式调用
    // modelAndView.setView("addheadline.jsp").addViewData("aaa", "bbb")
    public ModelAndView addViewData(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }
}
