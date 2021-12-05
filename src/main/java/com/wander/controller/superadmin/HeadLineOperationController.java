package com.wander.controller.superadmin;

import com.wander.entity.bo.HeadLine;
import com.wander.entity.dto.Result;
import com.wander.service.solo.HeadLineService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HeadLineOperationController {
    private HeadLineService headLineService;

    public Result<Boolean> addHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        Boolean aBoolean = headLineService.addHeadLine(new HeadLine());
        return new Result<>();
    };

    public Result<Boolean> removeHeaderLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.removeHeaderLine(1);
        return new Result<>();
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
