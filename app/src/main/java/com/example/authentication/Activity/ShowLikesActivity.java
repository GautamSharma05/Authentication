package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Adapter.ShowLikesAdapter;
import com.example.authentication.Models.Posts;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityShowLikesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unchecked")
public class ShowLikesActivity extends AppCompatActivity {
    ActivityShowLikesBinding binding;
    ArrayList<Users> usersArrayList;
    ShowLikesAdapter showLikesAdapter;
    List<String> groupLikes ;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_likes);
        binding = ActivityShowLikesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        usersArrayList = new ArrayList<>();
        showLikesAdapter = new ShowLikesAdapter(this,usersArrayList);
        binding.likesProfileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.likesProfileRecyclerView.setAdapter(showLikesAdapter);
        binding.likesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String postId = getIntent().getStringExtra("PostId");
        fStore.collection("Posts").document(postId).collection("Activity").document("Likes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if(task.isSuccessful()){
                  DocumentSnapshot documentSnapshot = task.getResult();
                  if (documentSnapshot.exists()){
                      groupLikes = (List<String>) documentSnapshot.get("TotalLikes");
                      for(int i =0 ; i < groupLikes.size();i++){
                          fStore.collection("Users").document(groupLikes.get(i).trim()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                              @Override
                              public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    Users users = documentSnapshot.toObject(Users.class);
                                    usersArrayList.add(users);
                                }
                                else{
                                    Toast.makeText(ShowLikesActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                showLikesAdapter.notifyDataSetChanged();
                              }
                          });

                      }
                  }
              }

            }
        });




    }
}