package com.wander.controller.frontend;

import com.wander.entity.dto.MainPageInfoDTO;
import com.wander.entity.dto.Result;
import com.wander.service.combine.HeadShopCombineService;
import org.simplespringframework.core.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainPageController {
    private HeadShopCombineService headShopCombineService;

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest req, HttpServletResponse response) {
        MainPageInfoDTO mainPageInfo = headShopCombineService.getMainPageInfo();
        Result<MainPageInfoDTO> result = new Result<>();
        result.setCode(200);
        result.setMsg("");
        result.setData(mainPageInfo);
        return result;
    }
}
