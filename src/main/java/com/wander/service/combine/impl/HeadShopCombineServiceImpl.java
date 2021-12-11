package com.wander.service.combine.impl;

import com.wander.entity.bo.HeadLine;
import com.wander.entity.bo.ShopCategory;
import com.wander.entity.dto.MainPageInfoDTO;
import com.wander.service.combine.HeadShopCombineService;
import com.wander.service.solo.HeadLineService;
import com.wander.service.solo.ShopCategoryService;
import org.simplespringframework.core.annotation.Service;

import java.util.List;

@Service
public class HeadShopCombineServiceImpl implements HeadShopCombineService {

    private HeadLineService headLineService;
    private ShopCategoryService shopCategoryService;

    @Override
    public MainPageInfoDTO getMainPageInfo() {
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headLines = headLineService.listHeadLine(headLineCondition, 1, 4);

        ShopCategory categoryCondition = new ShopCategory();
        List<ShopCategory> shopCategories = shopCategoryService.listShopCategory(categoryCondition, 1, 100);

        MainPageInfoDTO result = new MainPageInfoDTO();
        result.setHeadLineList(headLines);
        result.setShopCategoryList(shopCategories);
        return result;
    }
}
