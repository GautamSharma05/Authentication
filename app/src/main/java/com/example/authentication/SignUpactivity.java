package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.authentication.databinding.ActivitySignUpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpactivity extends AppCompatActivity {
    ActivitySignUpactivityBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);
        binding = ActivitySignUpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        String userName = binding.PersonName.getText().toString();
        String mobile = binding.phoneNumber.getText().toString();

        binding.SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SignUp();
            }
        });
    }

    public void SignUp() {
        String name = binding.PersonName.getText().toString().trim();
        String mobile = binding.phoneNumber.getText().toString();
        String email = binding.emailAdress.getText().toString();
        String password = binding.password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            binding.PersonName.setError("Full Name is Required");

        }
        if (TextUtils.isEmpty(mobile)) {
            binding.phoneNumber.setError("Mobile Number is Required");
        }
        if (TextUtils.isEmpty(email)) {
            binding.emailAdress.setError("Email is Required");
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is Required");
        }
        if (password.length() < 6) {
            binding.password.setError("Password Must be equal to 6 character or greater than 6");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("UserCreated", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w("User Creation Failed", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpactivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });

        }
    }
    private void updateUI(FirebaseUser user) {
        if(user != null){
            String name = binding.PersonName.getText().toString();
            String mobile = binding.phoneNumber.getText().toString();
            String email = binding.emailAdress.getText().toString();
            Intent intent = new Intent(SignUpactivity.this,CompleteProfile.class);
            intent.putExtra("FullName",name);
            intent.putExtra("Email",email);
            intent.putExtra("Mobile",mobile);
            startActivity(intent);
        }

    }






    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUpactivity.this,MainActivity.class);
            startActivity(intent);
        }
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