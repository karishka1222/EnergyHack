package com.example.energyhack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.example.energyhack.adapters.ChatAdapter;
import com.example.energyhack.adapters.UsersAdapter;
import com.example.energyhack.databinding.ActivityChatWithFriendsBinding;
import com.example.energyhack.models.ChatMessage;
import com.example.energyhack.models.User;
import com.example.energyhack.utilities.Constants;
import com.example.energyhack.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatWithFriendsActivity extends AppCompatActivity {

    private ActivityChatWithFriendsBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatWithFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

//    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_CHAT)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful() && task.getResult() != null) {
//                        List<ChatMessage> chatMessages = new ArrayList<>();
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            ChatMessage chatMessage = new ChatMessage();
//                            chatMessage.senderId = queryDocumentSnapshot.getString(Constants.KEY_SENDER_ID);
//                            chatMessage.receiverId = queryDocumentSnapshot.getString(Constants.KEY_RECEIVER_ID);
//                            chatMessage.message = queryDocumentSnapshot.getString(Constants.KEY_MESSAGE);
//                            chatMessage.dataTime = getReadableDateTime(queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP));
//                            chatMessage.dateObject = queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP);
//                            chatMessages.add(chatMessage);
//                        }
//                        binding.chatRecyclerView.setAdapter(chatAdapter);
//                        binding.chatRecyclerView.setVisibility(View.VISIBLE);
//                        binding.progressBar.setVisibility(View.GONE);
//                        System.out.println("test");
//                    }
//                });
//    });

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        binding.chatRecyclerView.setVisibility(View.VISIBLE);
        if(error != null) {
            return;
        }
        if(value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dataTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(chatMessages, Comparator.comparing(o -> o.dateObject));
            }
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
                System.out.println("nothing");
            } else {
//                chatAdapter.notifyItemRangeInserted(0, 1);
                chatAdapter.notifyItemInserted(chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                for (int i = 0; i <= chatAdapter.getItemCount() - 1; i++) {
                    System.out.println(chatAdapter.chatMessages.get(i).message);
                    System.out.println(binding.chatRecyclerView.findViewHolderForAdapterPosition(i));
                }

//                System.out.println((TextView) binding.chatRecyclerView.getLayoutManager().findViewByPosition(binding.chatRecyclerView.get));
            }

        }
        binding.progressBar.setVisibility(View.GONE);
    });

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}