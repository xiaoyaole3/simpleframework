package com.wander.service.solo.impl;

import com.wander.entity.bo.ShopCategory;
import com.wander.service.solo.ShopCategoryService;
import org.simplespringframework.core.annotation.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Override
    public Boolean addShopCategory(ShopCategory headLine) {
        return null;
    }

    @Override
    public Boolean removeShopCategory(int headLineId) {
        return null;
    }

    @Override
    public Boolean modifyShopCategory(ShopCategory headLine) {
        return null;
    }

    @Override
    public ShopCategory getShopCategoryById(int headLineId) {
        return null;
    }

    @Override
    public List<ShopCategory> listShopCategory(ShopCategory headLineCondition, int pageIndex, int pageSize) {
        return null;
    }
}
