package com.wander.service.solo.impl;

import com.wander.entity.bo.HeadLine;
import com.wander.service.solo.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import org.simplespringframework.core.annotation.Service;

import java.util.List;

@Slf4j
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    public Boolean addHeadLine(HeadLine headLine) {
        log.info("addHeadLine run.");
        return null;
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
        return null;
    }
}
