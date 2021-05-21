package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Adapter.ShowFollowingAdapter;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityShowFollowingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class ShowFollowingActivity extends AppCompatActivity {
    ActivityShowFollowingBinding binding;
    ShowFollowingAdapter showFollowingAdapter;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<String> followingList;
    ArrayList<Users> usersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_following);
        binding = ActivityShowFollowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        usersArrayList = new ArrayList<>();
        showFollowingAdapter = new ShowFollowingAdapter(this,usersArrayList);
        binding.followingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.followingRecyclerView.setAdapter(showFollowingAdapter);
        binding.followingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String currentUserId = mAuth.getUid();
        fStore.collection("Users").document(currentUserId).collection("Peoples").document("Following").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        followingList = (List<String>) documentSnapshot.get("TotalFollowing");
                        for(int i =0 ; i < followingList.size();i++){
                            DocumentReference documentReference = fStore.collection("Users").document(followingList.get(i).trim());
                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(value != null){
                                        Users users = value.toObject(Users.class);
                                        usersArrayList.add(users);
                                    }
                                    else {
                                        Toast.makeText(ShowFollowingActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                    showFollowingAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }
                }
            }
        });
    }
}