package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Adapter.PostAdapter;
import com.example.authentication.Models.Posts;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    PostAdapter postAdapter;
    ArrayList<Posts> postsArrayList;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        postsArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(this,postsArrayList);
        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView3.setAdapter(postAdapter);
        fStore.collection("Posts").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        Posts posts = d.toObject(Posts.class);
                        if(!posts.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                            postsArrayList.add(posts);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });

      binding.messagesActivity.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
              startActivity(intent);
          }
      });
      binding.imageView2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
              startActivity(intent);
          }
      });

      binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              if (item.getItemId() == R.id.Profile) {
                  Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                  startActivity(intent);
              }
              else if(item.getItemId() == R.id.Posts){
                  Intent intent = new Intent(MainActivity.this, PostsActivity.class);
                  startActivity(intent);
              }
              else if(item.getItemId() == R.id.Likes){
                 Intent intent = new Intent(MainActivity.this,UsersActivity.class);
                 startActivity(intent);
              }
              else if(item.getItemId() == R.id.Reels){
                  Intent intent = new Intent(MainActivity.this,ReelsActivity.class);
                  startActivity(intent);
              }

              return false;
          }
      });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

}
