package com.sum.slike;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sen on 2018/2/6.
 */

public class TextAnimationFrame extends BaseAnimationFrame{
    public static final int TYPE = 2;
    private long lastClickTimeMillis;
    private int likeCount;

    public TextAnimationFrame(long duration) {
        super(duration);
    }


    @Override
    public void prepare(int x, int y, BitmapProvider.Provider bitmapProvider) {
        reset();
        setStartPoint(x, y);
        calculateCombo();
        elements = generatedElements(x, y, bitmapProvider);
    }

    private void calculateCombo(){
        if(System.currentTimeMillis() - lastClickTimeMillis <  duration){
            likeCount++;
        }else{
            likeCount = 1;
        }
        lastClickTimeMillis = System.currentTimeMillis();
    }

    @Override
    public int getType() {
        return TYPE;
    }


    @Override
    public boolean onlyOne() {
        return true;
    }

    /**
     * 生成N个Element
     */
    protected List<Element> generatedElements(int x, int y, BitmapProvider.Provider bitmapProvider){
        List<Element> elements = new ArrayList<>();
        int count = likeCount;
        int offset_x = 0;
        while (count > 0){
            int number = count % 10;
            Bitmap bitmap = bitmapProvider.getNumberBitmap(number);
            offset_x += bitmap.getWidth();
            Element element = new TextElement(bitmap, x - offset_x);
            elements.add(element);
            count = count / 10;
        }

        int level = likeCount / 10;
        if(level > 2){
            level = 2;
        }
        elements.add(new TextElement(bitmapProvider.getLevelBitmap(level), x));
        return elements;
    }

    public static class TextElement implements Element{
        private int x;
        private int y;
        private Bitmap bitmap;

        public TextElement(Bitmap bitmap, int x){
            this.bitmap = bitmap;
            this.x = x;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public Bitmap getBitmap() {
            return bitmap;
        }

        @Override
        public void evaluate(int start_x, int start_y, double time) {
            y = start_y - 500 - (bitmap.getHeight() / 2);
        }
    }
}
