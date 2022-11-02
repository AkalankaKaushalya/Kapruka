package com.kapruka.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapruka.Models.YtVideoModel;
import com.kapruka.VideoViewActivity;
import com.kapruka.databinding.RowPropulerVideoBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YtVideoAdapter extends RecyclerView.Adapter<YtVideoAdapter.YtVideoHolder> {

    public List<YtVideoModel> ytVideoModelList;
    RowPropulerVideoBinding binding;
    Context context;

    public YtVideoAdapter(List<YtVideoModel> ytVideoModelList, Context context) {
        this.ytVideoModelList = ytVideoModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public YtVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowPropulerVideoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new YtVideoHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull YtVideoHolder holder, int position) {
        YtVideoModel ytVideoModel = ytVideoModelList.get(position);
        String id = ytVideoModel.getVideoID();
        String userid = ytVideoModel.getUserID();
        String videotitle = ytVideoModel.getVideoTitle();
        String videoimg = ytVideoModel.getVideoImge();
        String videourl = ytVideoModel.getVideoUrl();
        String viewcount = ytVideoModel.getViewCount();


        holder.VideoTitleTv.setText(videotitle);
        holder.ViewCountTv.setText(viewcount);
        Picasso.get().load(videoimg).into(holder.VideoImg, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoViewActivity.class);
                intent.putExtra("VideoUrl", videourl);
                intent.putExtra("VideoID", id);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ytVideoModelList.size();
    }

    class YtVideoHolder extends RecyclerView.ViewHolder {
        TextView VideoTitleTv, ViewCountTv;
        RoundedImageView VideoImg;

        public YtVideoHolder(@NonNull View itemView) {
            super(itemView);
            VideoTitleTv = binding.tvVideoTitle;
            ViewCountTv = binding.tvViewCount;
            VideoImg = binding.ivVideoimg;
        }
    }
}
