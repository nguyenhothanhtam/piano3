package com.example.thanh.piano3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailED, passED;
    private ImageButton btnLogin;
    private Button btnSign;

    private FirebaseDatabase database;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailED = (EditText)findViewById(R.id.emailEditText);
        passED = (EditText)findViewById(R.id.passEditText);

        btnSign = (Button)findViewById(R.id.BtnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        users = database.getReference("user");

        btnLogin = (ImageButton)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = emailED.getText().toString().trim();
                final String pass = passED.getText().toString().trim();

                if (mail.equals("") || pass.equals(""))
                {
                    String err = "Dien thieu thong tin";
                    Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                    return;
                }

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            if (data.child("email").getValue().equals(mail) && data.child("password").getValue().equals(pass))
                            {
                                String userID = data.getKey();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("key1", userID);
                                startActivity(intent);
                                return;
                            }
                        }
                        String err = "Sai email hoac mat khau";
                        Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
