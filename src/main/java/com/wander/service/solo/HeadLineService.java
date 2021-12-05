package com.wander.service.solo;

import com.wander.entity.bo.HeadLine;

import java.util.List;

public interface HeadLineService {
    Boolean addHeadLine(HeadLine headLine);

    Boolean removeHeaderLine(int headLineId);

    Boolean modifyHeadLine(HeadLine headLine);

    HeadLine getHeadLineById(int headLineId);

    List<HeadLine> listHeadLine(HeadLine headLineCondition, int pageIndex, int pageSize);
}
