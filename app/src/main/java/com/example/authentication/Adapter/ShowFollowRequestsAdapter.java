package com.example.authentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.SampleRequestsBinding;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowFollowRequestsAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Users> usersArrayList;

    public ShowFollowRequestsAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_requests,parent,false);
        return new ShowFollowRequestsAdapter.ShowFollowRequestsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Users users = usersArrayList.get(position);
                ShowFollowRequestsHolder viewHolder = (ShowFollowRequestsHolder)holder;
                viewHolder.binding.requestedUserName.setText(users.getUsername());
                viewHolder.binding.requestedFullName.setText(users.getFullName());
                Glide.with(context).load(users.getProfileImage()).placeholder(R.drawable.avatar).into(viewHolder.binding.imageView);
                viewHolder.binding.confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentUser = FirebaseAuth.getInstance().getUid();
                        String senderUser = users.getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(currentUser).collection("Peoples").document("Followers");
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if(task.isSuccessful()){
                                   DocumentSnapshot documentSnapshot = task.getResult();
                                   if(documentSnapshot.exists()){
                                       documentReference.update("TotalFollowers",FieldValue.arrayUnion(senderUser)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(context, "Comfirm", Toast.LENGTH_SHORT).show();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(context, "Try Again! SomeTime Your network connection is slow", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }else{
                                       Map<String,List<String>> followers = new HashMap<>();
                                       followers.put("TotalFollowers",Arrays.asList(senderUser));
                                       documentReference.set(followers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(context, "Try Again! SomeTime Your network connection is slow", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                               }
                            }
                        });
                        DocumentReference documentReference1 = FirebaseFirestore.getInstance().collection("Users").document(senderUser).collection("Peoples").document("Following");
                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                             if(task.isSuccessful()){
                                 DocumentSnapshot documentSnapshot1 = task.getResult();
                                 if(documentSnapshot1.exists()){
                                     documentReference1.update("TotalFollowing",FieldValue.arrayUnion(currentUser)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {
                                             Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show();
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }else{
                                     Map<String,List<String>> following = new HashMap<>();
                                     following.put("TotalFollowing",Arrays.asList(senderUser));
                                     documentReference1.set(following).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {
                                             Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show();
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }
                             }
                            }
                        });
                    }
                });
                viewHolder.binding.cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentUser = FirebaseAuth.getInstance().getUid();
                        String senderUser = users.getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Requests").document(currentUser);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable  DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value != null){
                                    documentReference.update("FollowRequest",FieldValue.arrayRemove(senderUser)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Follow Request Denied", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    public static class ShowFollowRequestsHolder extends RecyclerView.ViewHolder{
        SampleRequestsBinding binding;
        public ShowFollowRequestsHolder(@NonNull View view) {
            super(view);
            binding = SampleRequestsBinding.bind(view);
        }
    }
}
