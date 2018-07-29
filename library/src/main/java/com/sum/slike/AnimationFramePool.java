package com.sum.slike;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sen on 2018/3/12.
 */

public class AnimationFramePool {

    private static final int DURATION = 1000;
    private List<AnimationFrame> runningFrameList;
    private List<AnimationFrame> idleFrameList;


    private int maxFrameSize;
    private int elementAmount;
    private int frameCount;

    AnimationFramePool(int maxFrameSize, int elementAmount){
        this.maxFrameSize = maxFrameSize;
        this.elementAmount = elementAmount;
        runningFrameList = new ArrayList<>(maxFrameSize);
        idleFrameList = new ArrayList<>(maxFrameSize);
    }

    boolean hasRunningAnimation(){
        return runningFrameList.size() > 0;
    }

    AnimationFrame obtain(int type) {

        // RunningAnimationFrame 存在onlyOne直接复用
        AnimationFrame animationFrame = getRunningFrameListByOnlyOneAndType(type);
        if(animationFrame != null)
            return animationFrame;

        // 有空闲AnimationFrame直接使用, 加入runningFrame队列中
        animationFrame = removeIdleFrameListDownByType(type);
        if (animationFrame == null && frameCount < maxFrameSize) {
            frameCount++;
            if (type == EruptionAnimationFrame.TYPE) {
                animationFrame = new EruptionAnimationFrame(elementAmount, DURATION);
            } else {
                animationFrame = new TextAnimationFrame(DURATION);
            }
        }
        if (animationFrame != null) {
            runningFrameList.add(animationFrame);
        }

        return animationFrame;
    }

    private AnimationFrame getRunningFrameListByOnlyOneAndType(int type) {
        for (int i = runningFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runningFrameList.get(i);
            if(type == animationFrame.getType() && animationFrame.onlyOne())
                return animationFrame;
        }
        return null;
    }

    void recycleAll() {
        for (int i = runningFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runningFrameList.get(i);
            recycle(animationFrame);
            animationFrame.reset();
        }
    }

    void recycle(AnimationFrame animationFrame) {
        runningFrameList.remove(animationFrame);
        idleFrameList.add(animationFrame);
    }

    List<AnimationFrame> getRunningFrameList() {
        return runningFrameList;
    }

    private AnimationFrame removeIdleFrameListDownByType(int type){
        for (int i = idleFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = idleFrameList.get(i);
            if(type == animationFrame.getType())
                return idleFrameList.remove(i);
        }
        return null;
    }
}
