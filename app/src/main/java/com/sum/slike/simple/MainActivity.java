package com.sum.slike.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sum.slike.BitmapProvider;
import com.sum.slike.SuperLikeLayout;

public class MainActivity extends AppCompatActivity {

    SuperLikeLayout superLikeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superLikeLayout = findViewById(R.id.super_like_layout);
        BitmapProvider.Provider provider = new BitmapProvider.Builder(this)
                .setDrawableArray(new int[]{R.mipmap.emoji_1, R.mipmap.emoji_2, R.mipmap.emoji_3, R.mipmap.emoji_4, R.mipmap.emoji_5, R.mipmap.emoji_6,
                        R.mipmap.emoji_7, R.mipmap.emoji_8, R.mipmap.emoji_9, R.mipmap.emoji_10, R.mipmap.emoji_11, R.mipmap.emoji_12, R.mipmap.emoji_13,
                        R.mipmap.emoji_14, R.mipmap.emoji_15, R.mipmap.emoji_16, R.mipmap.emoji_17, R.mipmap.emoji_18, R.mipmap.emoji_19, R.mipmap.emoji_20})
                .setNumberDrawableArray(new int[]{R.mipmap.multi_digg_num_0, R.mipmap.multi_digg_num_1, R.mipmap.multi_digg_num_2, R.mipmap.multi_digg_num_3,
                        R.mipmap.multi_digg_num_4, R.mipmap.multi_digg_num_5, R.mipmap.multi_digg_num_6, R.mipmap.multi_digg_num_7,
                        R.mipmap.multi_digg_num_8, R.mipmap.multi_digg_num_9})
                .setLevelDrawableArray(new int[]{R.mipmap.multi_digg_word_level_1, R.mipmap.multi_digg_word_level_2, R.mipmap.multi_digg_word_level_3})
                .build();
        superLikeLayout.setProvider(provider);
        findViewById(R.id.like_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = (int)(v.getX() + v.getWidth() / 2);
                int y = (int)(v.getY() + v.getHeight() / 2);
                superLikeLayout.launch(x, y);
            }
        });
    }
}
