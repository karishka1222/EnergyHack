package com.example.energyhack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;

public class calendar extends AppCompatActivity {

    private AppCompatImageView btn_profile, btn_map, btn_users, btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn_profile = (AppCompatImageView) findViewById(R.id.btn_profile);
        btn_map = (AppCompatImageView) findViewById(R.id.btn_map);
        btn_users = (AppCompatImageView) findViewById(R.id.btn_users);
        btn_chat = (AppCompatImageView) findViewById(R.id.btn_chat);

        setListeners();
    }

    private void setListeners() {
        btn_chat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), chat.class)));
        btn_profile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), settings.class)));
        btn_map.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        btn_users.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }
}