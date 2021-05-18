package com.example.authentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Models.Request;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.SampleChatsBinding;
import com.example.authentication.databinding.SampleProfileBinding;
import com.example.authentication.databinding.ShowProfileBinding;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowUserAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Users> usersArrayList;

    public FollowUserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_profile,parent,false);
        return new FollowUserAdapter.FollowUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);
        FollowUserHolder viewHolder = (FollowUserAdapter.FollowUserHolder)holder;
        viewHolder.binding.userName.setText(users.getUsername());
        viewHolder.binding.fullName.setText(users.getFullName());
        Glide.with(context).load(users.getProfileImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.imageView);
        viewHolder.binding.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.binding.followButton.setText("Requested");
                String receiverUid = users.getUid();
                String senderUid = FirebaseAuth.getInstance().getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Requests").document(receiverUid);
                documentReference.update("FollowRequest",FieldValue.arrayUnion(senderUid)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "RequestSend", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Request Not Send", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    public static class FollowUserHolder extends RecyclerView.ViewHolder {
        ShowProfileBinding binding;
        public FollowUserHolder(View view) {
            super(view);
            binding = ShowProfileBinding.bind(view);
        }
    }
}
