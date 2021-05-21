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
import com.example.authentication.databinding.SampleFollowingBinding;

import java.util.ArrayList;

public class ShowFollowingAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Users> usersArrayList;

    public ShowFollowingAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_following,parent,false);
        return new ShowFollowingAdapter.ShowFollowingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);
        ShowFollowingAdapter.ShowFollowingHolder viewHolder= (ShowFollowingAdapter.ShowFollowingHolder)holder;
        viewHolder.binding.followingUserName.setText(users.getUsername());
        viewHolder.binding.followingFullName.setText(users.getFullName());
        Glide.with(context).load(users.getProfileImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.followingImage);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ShowFollowingHolder extends RecyclerView.ViewHolder {
        SampleFollowingBinding binding;
        public ShowFollowingHolder(View view) {
            super(view);
            binding =SampleFollowingBinding.bind(view);
        }
    }
}
