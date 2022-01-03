package org.simplespringframework.aop.aspect;

import lombok.Getter;
import org.simplespringframework.aop.PointcutLocator;

@Getter
public class AspectInfo {
    private int orderIndex;
    private DefaultAspect aspectObject;
    private PointcutLocator pointcutLocator;

    public AspectInfo(int orderIndex, DefaultAspect aspectObject) {
        this.orderIndex = orderIndex;
        this.aspectObject = aspectObject;
    }

    public AspectInfo(int orderIndex, DefaultAspect aspectObject, PointcutLocator pointcutLocator) {
        this.orderIndex = orderIndex;
        this.aspectObject = aspectObject;
        this.pointcutLocator = pointcutLocator;
    }
}
