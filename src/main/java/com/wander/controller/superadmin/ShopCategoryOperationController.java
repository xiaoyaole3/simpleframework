package com.wander.controller.superadmin;

import com.wander.entity.bo.ShopCategory;
import com.wander.entity.dto.Result;
import com.wander.service.solo.ShopCategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopCategoryOperationController {
    private ShopCategoryService shopCategoryService;

    public Result<Boolean> addShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        Boolean aBoolean = shopCategoryService.addShopCategory(new ShopCategory());
        return new Result<>();
    };

    public Result<Boolean> removeShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        shopCategoryService.removeShopCategory(1);
        return new Result<>();
    }

    public Result<Boolean> modifyShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        shopCategoryService.modifyShopCategory(new ShopCategory());
        return new Result<>();
    }

    public Result<ShopCategory> getShopCategoryById(HttpServletRequest req, HttpServletResponse resp){
        shopCategoryService.getShopCategoryById(1);
        return new Result<>();
    }

    public Result<List<ShopCategory>> listShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        shopCategoryService.listShopCategory(null, 1, 100);
        return new Result<>();
    }
}
