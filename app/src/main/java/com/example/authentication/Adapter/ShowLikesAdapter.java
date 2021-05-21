package com.example.authentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.SampleLikesProfileBinding;

import java.util.ArrayList;

public class ShowLikesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Users> usersArrayList;

    public ShowLikesAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_likes_profile,parent,false);
        return new ShowLikesAdapter.LikesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);
        LikesHolder viewHolder = (ShowLikesAdapter.LikesHolder)holder;
        viewHolder.binding.LikedUserName.setText(users.getUsername());
        viewHolder.binding.LikedFullName.setText(users.getFullName());
        Glide.with(context).load(users.getProfileImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.imageView);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class LikesHolder extends RecyclerView.ViewHolder{
        SampleLikesProfileBinding binding;
        public LikesHolder(View view){
            super(view);
            binding = SampleLikesProfileBinding.bind(view);
        }
    }
}
