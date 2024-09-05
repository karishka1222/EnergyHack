package com.example.energyhack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.energyhack.utilities.Constants;
import com.example.energyhack.utilities.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

public class settings extends AppCompatActivity {

    private TextView textUserName;
    private RoundedImageView imageProfile;
    private MaterialButton btn_sign_out;
    private PreferenceManager preferenceManager;
    private AppCompatImageView btn_map, btn_calendar, btn_users, btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textUserName = (TextView) findViewById(R.id.Name);
        imageProfile = (RoundedImageView) findViewById(R.id.imageProfile);
        btn_sign_out = (MaterialButton) findViewById(R.id.buttonSighOut);
        btn_map = (AppCompatImageView) findViewById(R.id.btn_profile);
        btn_calendar = (AppCompatImageView) findViewById(R.id.btn_calendar);
        btn_users = (AppCompatImageView) findViewById(R.id.btn_users);
        btn_chat = (AppCompatImageView) findViewById(R.id.btn_chat);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        loadUserDetails();
    }

    private void setListeners() {
        btn_sign_out.setOnClickListener(v -> signOut());
        btn_chat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), chat.class)));
        btn_map.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        btn_calendar.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), calendar.class)));
        btn_users.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }

    private void loadUserDetails() {
        textUserName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        showToast("Подождите...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Выход невыполнен"));
    }

}