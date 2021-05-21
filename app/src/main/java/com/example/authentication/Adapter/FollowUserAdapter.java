package com.example.authentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ShowProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowUserAdapter extends RecyclerView.Adapter {
    String receiverUid,senderUid;
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
                 receiverUid = users.getUid();
                 senderUid = FirebaseAuth.getInstance().getUid();
                String Accept = "no";
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Requests").document(receiverUid);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                documentReference.update("FollowRequest",FieldValue.arrayUnion(senderUid)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Request Send", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Request Not Send", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Map<String,List<String>> request = new HashMap<>();
                                request.put("FollowRequest",Arrays.asList(senderUid));
                                documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Request is Send", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Request is Not Send", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        /*DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Requests").document(FirebaseAuth.getInstance().getUid());
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if(task.isSuccessful()){
                  DocumentSnapshot documentSnapshot = task.getResult();
                  if(documentSnapshot.exists()){
                      List<String> showRequested = (List<String>) documentSnapshot.get("FollowRequest");
                      for (int i =0;i<showRequested.size();i++){
                          if(showRequested.get(i).trim() == receiverUid){
                              viewHolder.binding.followButton.setText("Requested");
                          }
                          else {
                              viewHolder.binding.followButton.setText("Follow");
                          }
                      }
                  }
              }
            }
        });*/
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
