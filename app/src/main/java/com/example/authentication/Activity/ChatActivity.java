package com.example.authentication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.example.authentication.Models.Message;
import com.example.authentication.Adapter.MessagesAdapter;
import com.example.authentication.R;
import com.example.authentication.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Date;




public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    MessagesAdapter messagesAdapter;
    ArrayList<Message> messageArrayList;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    //private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String senderRoom,receiverRoom;
    String receiverUid,senderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        messageArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this,messageArrayList);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setAdapter(messagesAdapter);
        String name = getIntent().getStringExtra("FullName");
        String profile = getIntent().getStringExtra("ProfileImage");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = mAuth.getUid();
        binding.name.setText(name);
        Glide.with(ChatActivity.this).load(profile).placeholder(R.drawable.avatar).into(binding.profile);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;
        //This code is to read a message from database
        database.getReference().child("Chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Message message = snapshot1.getValue(Message.class);
                    messageArrayList.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
                binding.recyclerView2.smoothScrollToPosition(binding.recyclerView2.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //This code is to write a message in a database
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageTxt = binding.messageBox.getText().toString();
                Date date = new Date();
                Message message = new Message(messageTxt,senderUid,date.getTime());
                binding.messageBox.setText(" ");
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference()
                                .child("Chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });

    }


}