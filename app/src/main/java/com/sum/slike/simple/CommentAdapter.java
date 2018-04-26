package com.sum.slike.simple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sen on 2018/4/26.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private boolean[] likeStatusArray = new boolean[10];
    private AdapterItemListener<Boolean> adapterItemListener;

    public void setAdapterItemListener(AdapterItemListener<Boolean> adapterItemListener) {
        this.adapterItemListener = adapterItemListener;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        holder.avatar.setImageResource(R.mipmap.ic_launcher);
        holder.userName.setText("管理员");
        holder.sendTime.setText("刚刚");
        holder.content.setText("哎哟不错哟");
        holder.likeCount.setText(likeStatusArray[position]?"1":"0");
        holder.likeStatus.setSelected(likeStatusArray[position]);
        holder.likeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapterItemListener != null){
                    adapterItemListener.onItemClickListener(
                            likeStatusArray[holder.getAdapterPosition()], holder.getAdapterPosition(), v.getId(), v);
                }
            }
        });
    }

    public void updateLikeStatusByPosition(boolean isLike, int position){
        likeStatusArray[position] = isLike;
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return likeStatusArray.length;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView userName;
        TextView sendTime;
        TextView content;
        TextView likeCount;
        View likeStatus;
        public CommentViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            userName = itemView.findViewById(R.id.user_name);
            sendTime = itemView.findViewById(R.id.send_time);
            content = itemView.findViewById(R.id.content);
            likeCount = itemView.findViewById(R.id.like_count);
            likeStatus = itemView.findViewById(R.id.like_status);
        }
    }

    public interface AdapterItemListener<T> {
        void onItemClickListener(T data, int position, int id, View view);
    }

}
