package com.wander.service.solo;

import com.wander.entity.bo.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    Boolean addShopCategory(ShopCategory headLine);

    Boolean removeShopCategory(int headLineId);

    Boolean modifyShopCategory(ShopCategory headLine);

    ShopCategory getShopCategoryById(int headLineId);

    List<ShopCategory> listShopCategory(ShopCategory headLineCondition, int pageIndex, int pageSize);
}
