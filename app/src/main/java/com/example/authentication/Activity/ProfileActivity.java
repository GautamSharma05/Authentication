package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.authentication.Adapter.ProfileAdapter;
import com.example.authentication.Models.Posts;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    ArrayList<Posts> postsArrayList;
    ProfileAdapter profileAdapter;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UserId;
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
        fStore.collection("Posts").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        Posts posts = d.toObject(Posts.class);
                        if(posts.getUid().equals(FirebaseAuth.getInstance().getUid())) {
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
        binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Chats) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.Posts){
                    Intent intent = new Intent(ProfileActivity.this, PostsActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}