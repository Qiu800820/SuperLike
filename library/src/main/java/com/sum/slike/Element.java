package com.sum.slike;

import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * Created by Sen on 2018/3/9.
 */

public interface Element {

    int getX();

    int getY();

    Bitmap getBitmap();

    void evaluate(int start_x, int start_y, double time);

    Paint getPaint();
}
