package com.wander.controller.frontend;

import com.wander.entity.dto.MainPageInfoDTO;
import com.wander.entity.dto.Result;
import com.wander.service.combine.HeadShopCombineService;
import lombok.Getter;
import org.simplespringframework.core.annotation.Controller;
import org.simplespringframework.inject.annotation.Autowired;
import org.simplespringframework.mvc.annotation.RequestMapping;
import org.simplespringframework.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Getter
@RequestMapping(value = "/main")
public class MainPageController {

    @Autowired(value = "HeadShopCombineServiceImpl")
    private HeadShopCombineService headShopCombineService;

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest req, HttpServletResponse response) {
        MainPageInfoDTO mainPageInfo = headShopCombineService.getMainPageInfo();
        Result<MainPageInfoDTO> result = new Result<>();
        result.setCode(200);
        result.setMsg("");
        result.setData(mainPageInfo);
        return result;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void throwException() {
        throw new RuntimeException("Test throw Exception.");
    }
}
