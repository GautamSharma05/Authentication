package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.authentication.Adapter.ReelsAdapter;
import com.example.authentication.Models.Reels;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityReelsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReelsActivity extends AppCompatActivity {
    ActivityReelsBinding binding;
    ArrayList<Reels> reelsArrayList;
    ReelsAdapter reelsAdapter;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reels);
        binding = ActivityReelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        reelsArrayList = new ArrayList<>();
        reelsAdapter = new ReelsAdapter(this,reelsArrayList);
        binding.reelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.reelsRecyclerView.setAdapter(reelsAdapter);
        fStore.collection("Reels").orderBy("timeStamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    List<DocumentSnapshot> list = value.getDocuments();
                    for (DocumentSnapshot d : list){
                        Reels reels = d.toObject(Reels.class);
                        reelsArrayList.add(reels);
                    }
                }
                reelsAdapter.notifyDataSetChanged();
            }
        });
        binding.bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Profile) {
                    Intent intent = new Intent(ReelsActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.Home) {
                    Intent intent = new Intent(ReelsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.Likes) {
                    Intent intent = new Intent(ReelsActivity.this, UsersActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.Posts) {
                    Intent intent = new Intent(ReelsActivity.this, PostsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}