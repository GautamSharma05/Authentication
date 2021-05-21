package com.example.authentication.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.authentication.Activity.ShowLikesActivity;
import com.example.authentication.Models.Posts;
import com.example.authentication.R;
import com.example.authentication.databinding.PostsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unchecked")
public class PostAdapter extends RecyclerView.Adapter{
    Boolean changeButton = false;
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
        PostsViewHolder viewHolder = (PostsViewHolder) holder;
        viewHolder.binding.FullName.setText(posts.getCreatedBy());
        viewHolder.binding.FullnameLower.setText(posts.getCreatedBy());
        viewHolder.binding.Captions.setText(posts.getCaptions());
        long time1 = posts.getTimeStamp();
        long time2 = System.currentTimeMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time2 - time1);
        if (seconds <= 60) {
            String time = Long.toString(seconds);
            viewHolder.binding.postTIme.setText(time + " " + "seconds ago");
        } else if (seconds > 60 && seconds <= 3600) {
            long m = seconds / 60;
            String minutes = Long.toString(m);
            viewHolder.binding.postTIme.setText(minutes + " " + "minutes ago");
        } else if (seconds > 3600 && seconds <= 86400) {
            long h = seconds / 3600;
            String hours = Long.toString(h);
            viewHolder.binding.postTIme.setText(hours + " " + "hours ago");
        } else {
            long d = seconds / 86400;
            String days = Long.toString(d);
            viewHolder.binding.postTIme.setText(days + " " + "days ago");
        }
        Glide.with(context).load(posts.getProfileImageUri()).placeholder(R.drawable.avatar).into(viewHolder.binding.userImage);
        Glide.with(context).load(posts.getPostImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.post);
        viewHolder.binding.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String likeById = FirebaseAuth.getInstance().getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Posts").document(posts.getPostId())
                        .collection("Activity").document("Likes");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                documentReference.update("TotalLikes", FieldValue.arrayUnion(likeById)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        viewHolder.binding.likes.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Not Liked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Map<String, List<String>> request = new HashMap<>();
                                request.put("TotalLikes", Arrays.asList(likeById));
                                documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        viewHolder.binding.likes.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Not Liked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        viewHolder.binding.likesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowLikesActivity.class);
                intent.putExtra("PostId", posts.getPostId());
                v.getContext().startActivity(intent);
            }
        });
        FirebaseFirestore.getInstance().collection("Posts").document(posts.getPostId()).collection("Activity").document("Likes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        List<String> groupLikes = (List<String>) documentSnapshot.get("TotalLikes");
                        viewHolder.binding.likesCount.setText(groupLikes.size() + " " + "Likes");
                    }
                }
            }
        });
        String userid = FirebaseAuth.getInstance().getUid();
        /*FirebaseFirestore.getInstance().collection("Posts")
                .document(posts.getPostId()).collection("Activity").document("Likes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        List<String> showLikes = (List<String>) documentSnapshot.get("TotalLikes");
                        for (int i = 0; i < showLikes.size(); i++) {
                            Toast.makeText(context, showLikes.get(i), Toast.LENGTH_SHORT).show();
                            if (showLikes.get(i).trim() == userid) {
                                Toast.makeText(context,"hello Ji", Toast.LENGTH_SHORT).show();
                               viewHolder.binding.likes.setImageResource(R.drawable.ic_baseline_favorite_24);
                            }
                            else{
                                viewHolder.binding.likes.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            }
                        }
                    }
                }
            }
        });*/
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
