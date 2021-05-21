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
import com.example.authentication.databinding.SampleFollowersBinding;

import java.util.ArrayList;

public class ShowFollowersAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Users> usersArrayList;

    public ShowFollowersAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_followers,parent,false);
        return new ShowFollowersAdapter.ShowFollowersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                   Users users = usersArrayList.get(position);
                   ShowFollowersHolder viewHolder= (ShowFollowersHolder)holder;
                   viewHolder.binding.followersUserName.setText(users.getUsername());
                   viewHolder.binding.followersFullName.setText(users.getFullName());
                   Glide.with(context).load(users.getProfileImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.followersImage);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class ShowFollowersHolder extends RecyclerView.ViewHolder{
        SampleFollowersBinding binding;
        public ShowFollowersHolder(@NonNull View view){
            super(view);
            binding = SampleFollowersBinding.bind(view);
        }
    }

}
