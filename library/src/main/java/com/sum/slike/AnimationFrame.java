package com.sum.slike;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sen on 2018/2/6.
 */

public class AnimationFrame {
    private int x;
    private int y;
    private double currentTime;
    private boolean isRunnable;
    private List<Element> elements;
    private AnimationEndListener animationEndListener;
    private int elementSize;
    private long duration;

    public AnimationFrame(int elementSize, long duration){
        this.elementSize = elementSize;
        this.duration = duration;
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    public List<Element> nextFrame(long interval){
        currentTime += interval;
        if(currentTime >= duration){
            isRunnable = false;
            if(animationEndListener != null){
                animationEndListener.onAnimationEnd(this);
            }
        }else{
            for(Element element : elements){
                element.evaluate(x, y, currentTime);
            }
        }
        return elements;
    }

    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.animationEndListener = animationEndListener;
    }

    public void reset() {
        currentTime = 0;
        if(elements != null){
            elements.clear();
        }
    }

    /**
     * 生成N个Element
     */
    public void prepare(int x, int y, BitmapProvider bitmapProvider) {
        reset();
        if(elements == null){
            elements = new ArrayList<>(elementSize);
        }
        this.x = x;
        this.y = y;
        for (int i = 0; i < elementSize; i++) {
            double startAngle = Math.random() * 45 + (i * 30);
            double speed = 500 + Math.random() * 200;
            Element element = new Element(x, y, startAngle, speed, bitmapProvider.getBitmap());
            elements.add(element);
        }

    }

    public static class Element {
        private int x;
        private int y;
        private double angle;
        private double speed;
        /**
         * 重力加速度px/s
         */
        private static final float GRAVITY = 800;
        private Bitmap bitmap;

        public Element(int x, int y, double angle, double speed, Bitmap bitmap){
            this.angle = angle;
            this.speed = speed;
            this.bitmap = bitmap;
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        Bitmap getBitmap() {
            return bitmap;
        }

        void evaluate(int start_x, int start_y, double time) {
            time = time / 1000;
            double x_speed = speed * Math.cos(angle * Math.PI / 180);
            double y_speed = -speed * Math.sin(angle * Math.PI / 180);
            x = (int)(start_x + (x_speed * time)  - (bitmap.getWidth() / 2));
            y = (int)(start_y + (y_speed * time) + (GRAVITY * time * time) / 2 - (bitmap.getHeight() / 2));
        }
    }
}
