package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.authentication.Adapter.FollowUserAdapter;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityUsersBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity {
    ActivityUsersBinding binding;
    ArrayList<Users> usersArrayList;
    FollowUserAdapter followUserAdapter;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        usersArrayList = new ArrayList<>();
        followUserAdapter = new FollowUserAdapter(this,usersArrayList);
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.userRecyclerView.setAdapter(followUserAdapter);
        fStore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        Users u = d.toObject(Users.class);
                        if(!Objects.requireNonNull(u).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                            usersArrayList.add(u);
                        }
                    }
                    followUserAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(UsersActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UsersActivity.this, "We Try To Resolve", Toast.LENGTH_SHORT).show();
            }
        });

        binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    Intent intent = new Intent(UsersActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Posts){
                    Intent intent = new Intent(UsersActivity.this, PostsActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Profile) {
                    Intent intent = new Intent(UsersActivity.this,ProfileActivity.class);
                    startActivity(intent);
                }
                /*else if(item.getItemId() == R.id.Reels){
                    Intent intent = new Intent(UsersActivity.this,ReelsActivity.class);
                    startActivity(intent);
                }*/

                return false;
            }
        });
    }
}