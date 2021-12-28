package org.simplespringframework.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simplespringframework.aop.aspect.AspectInfo;
import org.simplespringframework.aop.mock.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AspectListExecutorTest {

    @DisplayName(value = "Aspect sort: aspectInfoList")
    @Test
    public void sortTest() {
        List<AspectInfo> aspectInfos = new ArrayList<>();
        aspectInfos.add(new AspectInfo(3, new MockAspectInfo1()));
        aspectInfos.add(new AspectInfo(5, new MockAspectInfo2()));
        aspectInfos.add(new AspectInfo(2, new MockAspectInfo3()));
        aspectInfos.add(new AspectInfo(4, new MockAspectInfo4()));
        aspectInfos.add(new AspectInfo(1, new MockAspectInfo5()));

        AspectListExecutor aspectListExecutor = new AspectListExecutor(AspectListExecutorTest.class, aspectInfos);

        List<AspectInfo> sortedAspectInfoList = aspectListExecutor.getSortedAspectInfoList();
        for (AspectInfo aspectInfo : sortedAspectInfoList) {
            System.out.println(aspectInfo.getAspectObject().getClass().getSimpleName());
        }
    }
}