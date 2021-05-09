package com.example.authentication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.databinding.PostsBinding;
import com.example.authentication.databinding.SampleChatsBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter   {

    Context context;
    ArrayList<Posts> postsArrayList;

    public PostAdapter(Context context, ArrayList<Posts> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts,parent,false);
        return new PostAdapter.PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                 Posts posts = postsArrayList.get(position);
                 PostsViewHolder viewHolder = (PostsViewHolder)holder;
                 viewHolder.binding.FullName.setText(posts.getCreatedBy());
                 viewHolder.binding.FullnameLower.setText(posts.getCreatedBy());
                 viewHolder.binding.Captions.setText(posts.getCaptions());
                 Glide.with(context).load(posts.getPostImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.post);
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        PostsBinding binding;
        public PostsViewHolder(View view) {
            super(view);
            binding = PostsBinding.bind(view);
        }
    }
}
