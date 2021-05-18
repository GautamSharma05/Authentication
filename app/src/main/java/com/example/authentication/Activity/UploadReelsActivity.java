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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.Models.Users;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityUploadReelsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadReelsActivity extends AppCompatActivity {
    ActivityUploadReelsBinding binding;
    String reelsVideoUri;
    String userName,profileImageUri,UserId;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_reels);
        binding = ActivityUploadReelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Reels...");
        dialog.setCancelable(false);
        binding.reelsVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
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
                    userName = u.getFullName();
                    profileImageUri = u.getProfileImage();
                }
                else{
                    Log.d("error","User Data not found");
                }
            }
        });
        binding.uploadReelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reelsCaptions = "Under Construction";
                Date date = new Date();
                DocumentReference documentReference = fStore.collection("Reels").document();
                String reelsId = documentReference.getId();
                Map<String,Object> reels = new HashMap<>();
                reels.put("uid",UserId);
                reels.put("reelsCaptions",reelsCaptions);
                reels.put("ReelsCreatedBy",userName);
                reels.put("ReelsUri",reelsVideoUri);
                reels.put("reelsProfileImageUri",profileImageUri);
                reels.put("timeStamp",date.getTime());
                reels.put("reelsId",reelsId);
                documentReference.set(reels).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(UploadReelsActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadReelsActivity.this, "Sorry Failed To Upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri videoUri  = data.getData();
                binding.reelsVideo.setVideoURI(videoUri);
                binding.reelsVideo.seekTo(10);
                dialog.show();
                StorageReference fileRef = storageReference.child("Reels").child("myReel"+ Calendar.getInstance().getTime() + ".mp4");
                fileRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                             reelsVideoUri = uri.toString();
                             dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadReelsActivity.this, "Video not Found! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
}