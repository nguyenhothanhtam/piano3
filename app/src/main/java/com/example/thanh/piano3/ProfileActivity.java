package com.example.thanh.piano3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.piano3.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference users;
    private String userID;

    private ImageButton btnBack;
    private TextView birthdayTW, genderTW, emailTW, usernameTW, passTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userID = intent.getStringExtra("key1");

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("key1", userID);
                startActivity(intent);
            }
        });

//        Toast.makeText(ProfileActivity.this, userID, Toast.LENGTH_SHORT).show();

        birthdayTW = (TextView)findViewById(R.id.birthTW);
        genderTW = (TextView)findViewById(R.id.genderTW);
        emailTW = (TextView)findViewById(R.id.emailTW);
        usernameTW = (TextView)findViewById(R.id.usernameTW);
        passTW = (TextView)findViewById(R.id.passTW);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("user");
        users.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                getProfile(user.getBirthdate(), user.getGender(), user.getEmail(), user.getUsername(), user.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String notice = "Failed to get user's profile";
                Toast.makeText(ProfileActivity.this, notice, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfile(String birthday, String gender, String email, String username, String password){
        birthdayTW.setText(birthday);
        genderTW.setText(gender);
        emailTW.setText(email);
        usernameTW.setText(username);
        passTW.setText(password);
    }
}
