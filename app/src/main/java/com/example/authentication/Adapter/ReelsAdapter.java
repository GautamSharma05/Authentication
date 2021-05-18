package com.example.authentication.Adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.authentication.Models.Reels;
import com.example.authentication.R;
import com.example.authentication.databinding.SampleReelsBinding;



import java.util.ArrayList;

public class ReelsAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Reels> reelsArrayList;

    public ReelsAdapter(Context context, ArrayList<Reels> reelsArrayList) {
        this.context = context;
        this.reelsArrayList = reelsArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_reels,parent,false);
        return new ReelsAdapter.ReelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    Reels reels = reelsArrayList.get(position);
                    ReelsViewHolder viewHolder = (ReelsViewHolder)holder;
                    viewHolder.binding.reelsUserName.setText(reels.getReelsCreatedBy());
                    viewHolder.binding.reelscaptions.setText(reels.getReelsCaptions());
                    //viewHolder.initializeExoplayerView(reels.getReelsUri());
                    Glide.with(context).load(reels.getReelsProfileImageUri()).placeholder(R.drawable.avatar).into(viewHolder.binding.userImage);

    }

    @Override
    public int getItemCount() {
        return reelsArrayList.size();
    }

    public class ReelsViewHolder extends RecyclerView.ViewHolder{
        SampleReelsBinding binding;
        public ReelsViewHolder(View view){
            super(view);
            binding = SampleReelsBinding.bind(view);
        }

    }
}
