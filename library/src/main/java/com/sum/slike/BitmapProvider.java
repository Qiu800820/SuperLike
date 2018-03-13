package com.sum.slike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.LruCache;

/**
 * Created by Sen on 2018/3/8.
 */

public class BitmapProvider {

    static class Default implements Provider{

        private LruCache<Integer, Bitmap> bitmapLruCache;
        private int NUMBER_PREFIX = 0x70000000;
        private int LEVEL_PREFIX = 0x80000000;
        private @DrawableRes int[] drawableArray;
        private @DrawableRes int[] numberDrawableArray;
        private @DrawableRes int[] levelDrawableArray;
        private String[] levelStringArray;
        private Context context;
        private float textSize;

        Default(Context context, int cacheSize, @DrawableRes int[] drawableArray, @DrawableRes int[] numberDrawableArray,
                @DrawableRes int[] levelDrawableArray, String[] levelStringArray, float textSize){
            bitmapLruCache = new LruCache<>(cacheSize);
            this.drawableArray = drawableArray;
            this.numberDrawableArray = numberDrawableArray;
            this.levelDrawableArray = levelDrawableArray;
            this.levelStringArray = levelStringArray;
            this.context = context;
            this.textSize = textSize;
        }

        @NonNull
        @Override
        public Bitmap getNumberBitmap(int number) {
            Bitmap bitmap;
            if(numberDrawableArray != null && numberDrawableArray.length > 0){
                int index = number % numberDrawableArray.length;
                bitmap = bitmapLruCache.get(NUMBER_PREFIX|numberDrawableArray[index]);
                if(bitmap == null){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), numberDrawableArray[index]);
                    bitmapLruCache.put(NUMBER_PREFIX|numberDrawableArray[index], bitmap);
                }
            }else{
                bitmap = bitmapLruCache.get(NUMBER_PREFIX|number);
                if(bitmap == null){
                    bitmap = createBitmapByText(textSize, String.valueOf(number));
                    bitmapLruCache.put(NUMBER_PREFIX|number, bitmap);
                }
            }
            return bitmap;
        }

        @NonNull
        @Override
        public Bitmap getLevelBitmap(int level) {
            Bitmap bitmap;
            if(levelDrawableArray != null && levelDrawableArray.length > 0){
                int index = Math.min(level, levelDrawableArray.length);
                bitmap = bitmapLruCache.get(LEVEL_PREFIX|levelDrawableArray[index]);
                if(bitmap == null){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), levelDrawableArray[index]);
                    bitmapLruCache.put(LEVEL_PREFIX|levelDrawableArray[index], bitmap);
                }
            }else{
                bitmap = bitmapLruCache.get(LEVEL_PREFIX|level);
                if(bitmap == null){
                    int index = Math.min(level, levelStringArray.length);
                    bitmap = createBitmapByText(textSize, levelStringArray[index]);
                    bitmapLruCache.put(LEVEL_PREFIX|level, bitmap);
                }
            }
            return bitmap;
        }

        @Override
        public Bitmap getRandomBitmap(){
            int index = (int)(Math.random() * drawableArray.length);
            Bitmap bitmap = bitmapLruCache.get(drawableArray[index]);
            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(context.getResources(), drawableArray[index]);
                bitmapLruCache.put(drawableArray[index], bitmap);
            }
            return bitmap;
        }

        public Bitmap createBitmapByText(float textSize, String text){
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(textSize);
            Bitmap bitmap = Bitmap.createBitmap((int)textPaint.measureText(String.valueOf(text)),
                    (int)textSize, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawText(String.valueOf(text), 0 ,textSize, textPaint);
            return bitmap;
        }
    }

    public static class Builder{
        Context context;
        private int cacheSize;
        private @DrawableRes int[] drawableArray;
        private @DrawableRes int[] numberDrawableArray;
        private @DrawableRes int[] levelDrawableArray;
        private String[] levelStringArray;
        private float textSize;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setCacheSize(int cacheSize){
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder setDrawableArray(@DrawableRes int[] drawableArray){
            this.drawableArray = drawableArray;
            return this;
        }

        public Builder setNumberDrawableArray(@DrawableRes int[] numberDrawableArray){
            this.numberDrawableArray = numberDrawableArray;
            return this;
        }

        public Builder setLevelDrawableArray(@DrawableRes int[] levelDrawableArray){
            this.levelDrawableArray = levelDrawableArray;
            return this;
        }

        public Builder setLevelStringArray(String[] levelStringArray){
            this.levelStringArray = levelStringArray;
            return this;
        }

        public Builder setTextSize(float textSize){
            this.textSize = textSize;
            return this;
        }

        public Provider build(){

            if(cacheSize == 0){
                cacheSize = 32;
            }

            if(drawableArray == null || drawableArray.length == 0){
                this.drawableArray = new int[]{R.drawable.super_like_default_icon};
            }

            if(levelDrawableArray == null && levelStringArray == null){
                levelStringArray = new String[]{"鼓励!", "加油!!", "太棒了!!!"};
            }

            if(textSize < 24){
                textSize = context.getResources().getDimension(R.dimen.slike_default_text_size);
            }

            return new Default(context, cacheSize, drawableArray, numberDrawableArray,
                    levelDrawableArray, levelStringArray, textSize);
        }
    }



    public interface Provider{

        Bitmap getRandomBitmap();

        @NonNull Bitmap getNumberBitmap(int number);

        @NonNull Bitmap getLevelBitmap(int level);
    }
}
