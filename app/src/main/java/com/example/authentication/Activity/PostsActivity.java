package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.R;
import com.example.authentication.Models.Users;
import com.example.authentication.databinding.ActivityPostsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostsActivity extends AppCompatActivity {
    ActivityPostsBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String UserId;
    String postImageUri;
    String userName,profileImageUri;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        binding = ActivityPostsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Photo...");
        dialog.setCancelable(false);
        binding.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2000);
            }
        });
        fStore.collection("Users").document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users u = documentSnapshot.toObject(Users.class);
                assert u != null;
                userName = u.getFullName();
                profileImageUri = u.getProfileImage();
            }
        });
        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String captions = binding.captions.getText().toString();
                UserId = mAuth.getCurrentUser().getUid();
                Date date = new Date();
                DocumentReference documentReference = fStore.collection("Posts").document();
                Map<String,Object> posts = new HashMap<>();
                posts.put("uid",UserId);
                posts.put("CreatedBy",userName);
                posts.put("Captions",captions);
                posts.put("PostImage",postImageUri);
                posts.put("ProfileImageUri",profileImageUri);
                posts.put("timeStamp",date.getTime());
                documentReference.set(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(PostsActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostsActivity.this, "Post Not Created", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                binding.postImage.setImageURI(imageUri);
                StorageReference fileRef = storageReference.child("Posts").child("myPost"+ Calendar.getInstance().getTime() + ".jpg");
                dialog.show();
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                postImageUri = uri.toString();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostsActivity.this, "Image Uri Not Found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
}