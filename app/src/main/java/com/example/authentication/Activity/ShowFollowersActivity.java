package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Adapter.ShowFollowersAdapter;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityShowFollowersBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ShowFollowersActivity extends AppCompatActivity {
    ActivityShowFollowersBinding binding;
    ShowFollowersAdapter showFollowersAdapter;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<String> followersList;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_followers);
        binding = ActivityShowFollowersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        usersArrayList = new ArrayList<>();
        showFollowersAdapter = new ShowFollowersAdapter(this,usersArrayList);
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.followersRecyclerView.setAdapter(showFollowersAdapter);
        binding.followersBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String currentUserId = mAuth.getUid();
        fStore.collection("Users").document(currentUserId).collection("Peoples").document("Followers").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        followersList = (List<String>) documentSnapshot.get("TotalFollowers");
                        for(int i =0 ; i < followersList.size();i++){
                            DocumentReference documentReference = fStore.collection("Users").document(followersList.get(i).trim());
                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                         if(value != null){
                                             Users users = value.toObject(Users.class);
                                             usersArrayList.add(users);
                                         }
                                         else {
                                             Toast.makeText(ShowFollowersActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                                         }
                                         showFollowersAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            }
        });


    }


}