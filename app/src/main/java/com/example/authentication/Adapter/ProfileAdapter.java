package com.example.authentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Models.Posts;
import com.example.authentication.R;
import com.example.authentication.databinding.SampleProfileBinding;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Posts> postsArrayList;

    public ProfileAdapter(Context context, ArrayList<Posts> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_profile,parent,false);
        return new ProfileAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Posts posts = postsArrayList.get(position);
        ProfileViewHolder viewHolder = (ProfileViewHolder)holder;
        Glide.with(context).load(posts.getPostImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.currentUserImage);


    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public  class ProfileViewHolder extends RecyclerView.ViewHolder{
        SampleProfileBinding binding;
        public ProfileViewHolder(@NonNull View view){
            super(view);
            binding = SampleProfileBinding.bind(view);
        }
    }
}
