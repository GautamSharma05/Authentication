package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.authentication.Adapter.ProfileAdapter;
import com.example.authentication.Models.Posts;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    ArrayList<Posts> postsArrayList;
    ProfileAdapter profileAdapter;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UserId;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        postsArrayList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this,postsArrayList);
        binding.currentUserPostRecyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.currentUserPostRecyclerView.setAdapter(profileAdapter);
        binding.reelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,UploadReelsActivity.class);
                startActivity(intent);
            }
        });
        fStore.collection("Posts").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        Posts posts = d.toObject(Posts.class);
                        if(posts.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                            count++;
                            String postNumber = String.valueOf(count);
                            binding.postNumber.setText(postNumber);
                            postsArrayList.add(posts);
                        }
                        profileAdapter.notifyDataSetChanged();
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        UserId = mAuth.getUid();
        fStore.collection("Users").document(UserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.d("error","onEvent",error);
                    return;
                }
                if(value != null){
                    Users u = value.toObject(Users.class);
                    binding.userName.setText(u.getUsername());
                    binding.fullName.setText(u.getFullName());
                    binding.relationShipStatus.setText(u.getRelationShipStatus());
                    binding.userBio.setText(u.getBio());
                    Picasso.get().load(u.getProfileImage()).placeholder(R.drawable.avatar).into(binding.userImage);
                }
                else{
                    Log.d("error","User Data not found");
                }

            }
        });
        fStore.collection("Users").document(mAuth.getUid()).collection("Peoples").document("Followers").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   DocumentSnapshot documentSnapshot = task.getResult();
                   if(documentSnapshot.exists()){
                       List<String> followersList = (List<String>) documentSnapshot.get("TotalFollowers");
                       binding.followersNumber.setText(String.valueOf(followersList.size()));
                   }
               }

            }
        });
        fStore.collection("Users").document(mAuth.getUid()).collection("Peoples").document("Following").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        List<String> followingList = (List<String>) documentSnapshot.get("TotalFollowing");
                        binding.followingNumbers.setText(String.valueOf(followingList.size()));
                    }
                }

            }
        });
        binding.followersNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ShowFollowersActivity.class);
                startActivity(intent);
            }
        });
        binding.followersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ShowFollowersActivity.class);
                startActivity(intent);
            }
        });
        binding.followingNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ShowFollowingActivity.class);
                startActivity(intent);
            }
        });
        binding.followingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ShowFollowingActivity.class);
                startActivity(intent);
            }
        });
        binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Posts){
                    Intent intent = new Intent(ProfileActivity.this, PostsActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Likes){
                    Intent intent = new Intent(ProfileActivity.this,UsersActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Reels){
                    Intent intent = new Intent(ProfileActivity.this,ReelsActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}