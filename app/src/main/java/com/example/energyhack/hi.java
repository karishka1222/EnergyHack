package com.example.energyhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class hi extends AppCompatActivity {

    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi);

        btn_next = findViewById(R.id.next);
        btn_next.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), calendar.class)));

    }
}