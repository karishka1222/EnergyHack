package com.example.energyhack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.energyhack.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class settings extends AppCompatActivity {

    private Button saveInformationBtn;
    private EditText usernameET, statusET;
    private CircleImageView circleImageView;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveInformationBtn = (Button) findViewById(R.id.save_user_information);
        usernameET = (EditText) findViewById(R.id.set_user_name);
        statusET = (EditText) findViewById(R.id.set_user_status);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        saveInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInformation();
            }
        });

        retrieveUserInformation();
    }


    private void UpdateInformation() {
        String setName = usernameET.getText().toString();
        String setStatus = statusET.getText().toString();

        if(TextUtils.isEmpty(setName))
        {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setStatus))
        {
            Toast.makeText(this, "Введите статус", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setName);
            profileMap.put("status", setStatus);

            rootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(settings.this, "Информация обновлена", Toast.LENGTH_SHORT).show();

                                Intent mainIntent = new Intent(settings.this, MainActivity.class);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(settings.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void retrieveUserInformation() {
        rootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("name"))
                        {
                            String retrieveUserName = snapshot.child("name").getValue().toString();
                            String retrieveStatus = snapshot.child("status").getValue().toString();

                            usernameET.setText(retrieveUserName);
                            statusET.setText(retrieveStatus);
                        }
                        else
                        {
                            Toast.makeText(settings.this, "Введите своё имя", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}