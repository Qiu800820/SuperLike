package com.sum.slike.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.slike.SuperLikeLayout;

/**
 * Created by Sen on 2018/4/26.
 */

public class RecyclerViewActivity extends AppCompatActivity{

    private SuperLikeLayout superLikeLayout;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentAdapter.setAdapterItemListener(adapterItemListener);
        recyclerView.setAdapter(commentAdapter);

        superLikeLayout = findViewById(R.id.super_like_layout);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
    }

    CommentAdapter.AdapterItemListener<Boolean> adapterItemListener = new CommentAdapter.AdapterItemListener<Boolean>() {
        long duration = 2000;
        long lastClickTime;
        @Override
        public void onItemClickListener(Boolean isLike, int position, int id, View v) {

            if(System.currentTimeMillis() - lastClickTime> duration){ // 防抖
                isLike = !isLike;
                commentAdapter.updateLikeStatusByPosition(isLike, position);
            }
            lastClickTime = System.currentTimeMillis();
            if(isLike){
                int[] itemPosition = new int[2];
                int[] superLikePosition = new int[2];
                v.getLocationOnScreen(itemPosition);
                superLikeLayout.getLocationOnScreen(superLikePosition);
                int x = itemPosition[0] + v.getWidth() / 2;
                int y = (itemPosition[1] - superLikePosition[1]) + v.getHeight() / 2;
                superLikeLayout.launch(x, y);
            }


        }
    };


}
