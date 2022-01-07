package com.wander.service.solo.impl;

import com.wander.entity.bo.HeadLine;
import com.wander.service.solo.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.annotation.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    public Boolean addHeadLine(HeadLine headLine) {
        log.info("Add head line info = {}", headLine.toString());
        return true;
    }

    @Override
    public Boolean removeHeaderLine(int headLineId) {
        return null;
    }

    @Override
    public Boolean modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public HeadLine getHeadLineById(int headLineId) {
        return null;
    }

    @Override
    public List<HeadLine> listHeadLine(HeadLine headLineCondition, int pageIndex, int pageSize) {
        List<HeadLine> headLines = new ArrayList<>();

        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("1LineName");
        headLine1.setLineLink("1LineLink");
        headLine1.setLineImg("1LineImg");
        headLines.add(headLine1);

        HeadLine headLine2 = new HeadLine();
        headLine1.setLineId(2L);
        headLine1.setLineName("2LineName");
        headLine1.setLineLink("2LineLink");
        headLine1.setLineImg("2LineImg");
        headLines.add(headLine1);

        return headLines;
    }
}
