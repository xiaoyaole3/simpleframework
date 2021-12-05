package com.wander.entity.dto;

import com.wander.entity.bo.HeadLine;
import com.wander.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDTO {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
