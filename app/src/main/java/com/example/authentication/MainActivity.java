package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ArrayList<Users> usersArrayList;
    private CustomAdapter customAdapter;
   private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        usersArrayList = new ArrayList<>();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(this, usersArrayList);
        binding.recyclerView.setAdapter(customAdapter);
        fStore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                  if(!queryDocumentSnapshots.isEmpty()){
                      //binding.progressBar.setVisibility(View.GONE);
                      List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                      for (DocumentSnapshot d : list){
                          Users u = d.toObject(Users.class);
                          if(!u.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                              usersArrayList.add(u);
                          }
                      }
                      customAdapter.notifyDataSetChanged();
                  }else{
                      Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                  }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed To Get Data", Toast.LENGTH_SHORT).show();
            }
        });
      binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()){
                  case R.id.Profile:
                      Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                      startActivity(intent);
              }
              return false;
          }
      });
    }
}
