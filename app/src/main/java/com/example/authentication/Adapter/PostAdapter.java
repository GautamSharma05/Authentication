package com.example.authentication.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.authentication.Models.Posts;
import com.example.authentication.R;
import com.example.authentication.databinding.PostsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter{

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
                 long time1 = posts.getTimeStamp();
                 long time2 = System.currentTimeMillis();
                 long seconds = TimeUnit.MILLISECONDS.toSeconds(time2 - time1 );
                 if(seconds <= 60){
                     String time = Long.toString(seconds);
                     viewHolder.binding.postTIme.setText(time +" "+"seconds ago");
                 }
                 else if(seconds > 60 && seconds <=3600){
                     long m = seconds/60;
                     String minutes = Long.toString(m);
                     viewHolder.binding.postTIme.setText(minutes  +" "+"minutes ago");
                 }
                 else if(seconds>3600 && seconds <=86400){
                     long h = seconds/3600;
                     String hours = Long.toString(h);
                     viewHolder.binding.postTIme.setText(hours +" "+"hours ago");
                 }
                 else {
                     long d = seconds/86400;
                     String days = Long.toString(d);
                     viewHolder.binding.postTIme.setText(days +" "+"days ago");
                 }
                 Glide.with(context).load(posts.getProfileImageUri()).placeholder(R.drawable.avatar).into(viewHolder.binding.userImage);
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
