package com.sum.slike;

import java.util.List;

/**
 * Created by Sen on 2018/3/9.
 */

public interface AnimationFrame {

    int getType();

    boolean isRunning();

    List<Element> nextFrame(long interval);

    boolean onlyOne();

    void setAnimationEndListener(AnimationEndListener animationEndListener);

    void reset();

    void prepare(int x, int y, BitmapProvider.Provider bitmapProvider);

}
