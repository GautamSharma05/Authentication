package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Adapter.ShowFollowRequestsAdapter;
import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityShowFollowRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unchecked")
public class ShowFollowRequestActivity extends AppCompatActivity {
    ActivityShowFollowRequestBinding binding;
    ShowFollowRequestsAdapter showFollowRequestsAdapter;
    ArrayList<Users> usersArrayList;
    List<String> group ;
    String size;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow_request);
        binding = ActivityShowFollowRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        usersArrayList = new ArrayList<>();
        showFollowRequestsAdapter = new ShowFollowRequestsAdapter(this,usersArrayList);
        binding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.requestRecyclerView.setAdapter(showFollowRequestsAdapter);
        binding.requestBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String receiverUid = mAuth.getUid();
        assert receiverUid != null;
        fStore.collection("Requests").document(receiverUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()) {
                   DocumentSnapshot documentSnapshot = task.getResult();
                   assert documentSnapshot != null;
                   if (documentSnapshot.exists()) {
                       group = (List<String>) documentSnapshot.get("FollowRequest");
                       size = String.valueOf(group.size());
                       //Toast.makeText(ShowFollowRequestActivity.this, size, Toast.LENGTH_SHORT).show();
                       for(int i =0 ; i < group.size();i++){
                           //Toast.makeText(ShowFollowRequestActivity.this, group.get(i).trim(), Toast.LENGTH_SHORT).show();
                           fStore.collection("Users").document(group.get(i).trim()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {
                                   if (documentSnapshot.exists()) {
                                       Users users = documentSnapshot.toObject(Users.class);
                                       usersArrayList.add(users);
                                       //Toast.makeText(ShowFollowRequestActivity.this, users.toString(), Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       Toast.makeText(ShowFollowRequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                   }
                                   showFollowRequestsAdapter.notifyDataSetChanged();
                               }
                           });
                       }

                   }
               }
            }
        });

    }
}