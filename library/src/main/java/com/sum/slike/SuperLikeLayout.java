package com.sum.slike;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sen on 2018/2/6.
 */

public class SuperLikeLayout extends View implements AnimationEndListener, BitmapProvider{

    private static final String TAG = "SuperLikeLayout";
    private static final int MAX_FRAME_SIZE = 8;
    private static final int ERUPTION_ELEMENT_AMOUNT = 4;
    private static final int DURATION = 2000;
    private static final long INTERVAL = 40;
    private int maxFrameSize;
    private int elementAmount;
    private int frameCount;
    private List<AnimationFrame> runnableFrameList;
    private List<AnimationFrame> idleFrameList;
    private LruCache<Integer, Bitmap> bitmapLruCache;
    private @DrawableRes int[] drawableArray;
    private AnimationHandler animationHandler;


    public SuperLikeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SuperLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        bitmapLruCache = new LruCache<>(maxFrameSize * elementAmount / 2);
        drawableArray = new int[]{R.drawable.super_like_default_icon};
        animationHandler = new AnimationHandler(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperLikeLayout, defStyleAttr, 0);
        elementAmount = a.getInteger(R.styleable.SuperLikeLayout_eruption_element_amount, ERUPTION_ELEMENT_AMOUNT);
        maxFrameSize = a.getInteger(R.styleable.SuperLikeLayout_max_eruption_total, MAX_FRAME_SIZE);
        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(runnableFrameList == null || runnableFrameList.size() == 0)
            return;
        //遍历所有AnimationFrame 并绘制Element
        for (int i = runnableFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runnableFrameList.get(i);
            List<AnimationFrame.Element> elementList = animationFrame.nextFrame(INTERVAL);
            for(AnimationFrame.Element element : elementList){
                canvas.drawBitmap(element.getBitmap(), element.getX(), element.getY(), null);
            }
        }

    }

    @Override
    public Bitmap getBitmap(){
        // todo 控制Bitmap大小 防止OOM
        int index = (int)(Math.random() * drawableArray.length);
        Bitmap bitmap = bitmapLruCache.get(drawableArray[index]);
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), drawableArray[index]);
            bitmapLruCache.put(drawableArray[index], bitmap);
        }
        return bitmap;
    }


    public void launch(int x, int y) {
        AnimationFrame animationFrame = obtain();
        if(animationFrame != null && !animationFrame.isRunnable()) {
            animationFrame.prepare(x, y, this);
        }
        animationHandler.removeMessages(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION);
        animationHandler.sendEmptyMessageDelayed(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION, INTERVAL);

    }

    public boolean hasAnimation(){
        return runnableFrameList != null && runnableFrameList.size() > 0;
    }

    public void setDrawableArray(@DrawableRes int[] drawableArray){
        this.drawableArray = drawableArray;
    }

    private AnimationFrame obtain() {
        // 有空闲AnimationFrame直接使用
        AnimationFrame animationFrame = null;
        if (idleFrameList != null && !idleFrameList.isEmpty()) {
            animationFrame = idleFrameList.remove(idleFrameList.size() - 1);
        } else if (frameCount < maxFrameSize) {
            frameCount++;
            animationFrame = new AnimationFrame(elementAmount, DURATION);
            animationFrame.setAnimationEndListener(this);
        }
        if (animationFrame != null) {
            if (runnableFrameList == null) {
                runnableFrameList = new ArrayList<>(maxFrameSize);
            }
            runnableFrameList.add(animationFrame);
        }
        return animationFrame;
    }

    /**
     * 回收SurpriseView  添加至空闲队列方便下次使用
     *
     * @param animationFrame AnimationFrame
     */
    private void onRecycle(AnimationFrame animationFrame) {
        if (idleFrameList == null)
            idleFrameList = new ArrayList<>(maxFrameSize);
        Log.v(TAG, "=== AnimationFrame recycle ===");
        animationFrame.reset();
        runnableFrameList.remove(animationFrame);
        idleFrameList.add(animationFrame);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(!hasAnimation())
            return;
        // 回收所有view 并暂停动画
        for (int i = runnableFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runnableFrameList.get(i);
            onRecycle(animationFrame);
        }
        animationHandler.removeMessages(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION);
    }

    @Override
    public void onAnimationEnd(AnimationFrame animationFrame) {
        onRecycle(animationFrame);
    }


    private static final class AnimationHandler extends Handler{
        public static final int MESSAGE_CODE_REFRESH_ANIMATION = 1001;
        private WeakReference<SuperLikeLayout> weakReference;

        public AnimationHandler(SuperLikeLayout superLikeLayout){
            weakReference = new WeakReference<>(superLikeLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MESSAGE_CODE_REFRESH_ANIMATION && weakReference != null && weakReference.get() != null){
                weakReference.get().invalidate();
                // 动画还未结束继续刷新
                if(weakReference.get().hasAnimation()){
                    sendEmptyMessageDelayed(MESSAGE_CODE_REFRESH_ANIMATION, INTERVAL);
                }
            }

        }
    }

}
