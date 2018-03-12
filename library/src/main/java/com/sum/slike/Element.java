package com.sum.slike;

import android.graphics.Bitmap;

/**
 * Created by Sen on 2018/3/9.
 */

public interface Element {

    int getX();

    int getY();

    Bitmap getBitmap();

    void evaluate(int start_x, int start_y, double time);

}
