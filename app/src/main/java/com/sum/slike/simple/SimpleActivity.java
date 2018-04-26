package com.sum.slike.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sum.slike.SuperLikeLayout;

public class SimpleActivity extends AppCompatActivity {

    SuperLikeLayout superLikeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superLikeLayout = findViewById(R.id.super_like_layout);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
        findViewById(R.id.like_btn).setOnClickListener(new View.OnClickListener() {
            long duration = 2000;
            long lastClickTime;
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - lastClickTime > duration){ // 防抖
                    v.setSelected(!v.isSelected());
                }
                lastClickTime = System.currentTimeMillis();
                if(v.isSelected()){
                    int x = (int)(v.getX() + v.getWidth() / 2);
                    int y = (int)(v.getY() + v.getHeight() / 2);
                    superLikeLayout.launch(x, y);
                }

            }
        });
    }
}
