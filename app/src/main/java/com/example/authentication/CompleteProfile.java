package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.databinding.ActivityCompleteProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfile extends AppCompatActivity {
    ActivityCompleteProfileBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String UserId;
    String profileImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String name = getIntent().getStringExtra("FullName");
        String email = getIntent().getStringExtra("Email");
        String mobile = getIntent().getStringExtra("Mobile");

        binding.PersonName.setText(name);
        binding.emailAdress.setText(email);
        binding.phoneNumber.setText(mobile);
        binding.NewprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        });
        binding.completeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relationshipStatus = binding.reltionshipStatus.getText().toString();
                String Bio = binding.Bio.getText().toString();
                UserId = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Users").document(UserId);
                Map<String,Object> users = new HashMap<>();
                users.put("FullName",name);
                users.put("Email",email);
                users.put("MobileNumber",mobile);
                users.put("RelationShipStatus",relationshipStatus);
                users.put("Bio",Bio);
                users.put("ProfileImage",profileImageUri);
                documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       Intent intent = new Intent(CompleteProfile.this,MainActivity.class);
                       startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompleteProfile.this, "Please Try Again By Restarting The App", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                binding.NewprofileImage.setImageURI(imageUri);
                StorageReference fileRef = storageReference.child("profile.jpg");
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileImageUri = fileRef.getDownloadUrl().toString();
                    }
                });

            }
        }
    }
}