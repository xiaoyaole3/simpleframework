package com.wander.controller.superadmin;

import com.wander.entity.bo.HeadLine;
import com.wander.entity.dto.Result;
import com.wander.service.solo.HeadLineService;
import org.simplespringframework.core.annotation.Controller;
import org.simplespringframework.inject.annotation.Autowired;
import org.simplespringframework.mvc.annotation.RequestMapping;
import org.simplespringframework.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/headline")
public class HeadLineOperationController {

    // 这里这样做的原因是因为 在DependencyInjector类的getImplementClass函数中要求@Autowired的value要和Class的名字相同
    @Autowired(value = "HeadLineServiceImpl")
    private HeadLineService headLineService;

    public Result<Boolean> addHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        Boolean aBoolean = headLineService.addHeadLine(new HeadLine());
        return new Result<>();
    };

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public void removeHeaderLine() {
        headLineService.removeHeaderLine(1);
    }

    public Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.modifyHeadLine(new HeadLine());
        return new Result<>();
    }

    public Result<HeadLine> getHeadLineById(HttpServletRequest req, HttpServletResponse resp){
        headLineService.getHeadLineById(1);
        return new Result<>();
    }

    public Result<List<HeadLine>> listHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.listHeadLine(null, 1, 100);
        return new Result<>();
    }
}
