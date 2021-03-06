package com.example.authentication.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Activity.ChatActivity;
import com.example.authentication.R;
import com.example.authentication.Models.Users;
import com.example.authentication.databinding.SampleChatsBinding;


import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    private ArrayList<Users> usersArrayList;

    public CustomAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sample_chats, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Users users = usersArrayList.get(position);
        viewHolder.binding.name.setText(users.getFullName());
        Glide.with(context).load(users.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(viewHolder.binding.profile);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("FullName",users.getFullName());
                intent.putExtra("ProfileImage",users.getProfileImage());
                intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SampleChatsBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding = SampleChatsBinding.bind(view);
        }
    }
}

