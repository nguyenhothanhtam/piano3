package com.example.thanh.piano3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.piano3.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton imgBack;
    private Button btnDate, btnSign;
    private DatePicker datePicker;
    private TextView dateView;
    private EditText genderED, emailED, usernameED, passED;

    private FirebaseDatabase database;
    private DatabaseReference users;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        userID = intent.getStringExtra("key1");

        datePicker = (DatePicker)findViewById(R.id.calendar);
        dateView = (TextView)findViewById(R.id.dateTextView);

        genderED = (EditText)findViewById(R.id.genderEditText);
        emailED = (EditText)findViewById(R.id.emailEditText);
        usernameED = (EditText)findViewById(R.id.usernameEditText);
        passED = (EditText)findViewById(R.id.passEditText);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("key1", userID);
                startActivity(intent);
            }
        });

        btnDate = (Button)findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePicker.getLayoutParams().height == 0) {
                    ViewGroup.LayoutParams param = datePicker.getLayoutParams();
                    param.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    datePicker.setLayoutParams(param);
                    btnDate.setText("CHOOSE");
                }
                else {
                    ViewGroup.LayoutParams param = datePicker.getLayoutParams();
                    param.height = 0;
                    datePicker.setLayoutParams(param);
                    btnDate.setText("DATE");
                    dateView.setText(getDate(datePicker));
                }
            }
        });

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
                Toast.makeText(EditProfileActivity.this, notice, Toast.LENGTH_SHORT).show();
            }
        });

        btnSign = (Button)findViewById(R.id.BtnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = usernameED.getText().toString().trim();
                final String pass = passED.getText().toString().trim();
                final String mail = emailED.getText().toString().trim();
                final String gender = genderED.getText().toString().trim();
                final String date = dateView.getText().toString();

                if (!name.equals("")) users.child(userID).child("username").setValue(name);
                if (!pass.equals("")) users.child(userID).child("password").setValue(pass);
                if (!mail.equals("")) users.child(userID).child("email").setValue(mail);
                if (!gender.equals(""))users.child(userID).child("gender").setValue(gender);
                if (!date.equals("")) users.child(userID).child("birthdate").setValue(date);

                Toast.makeText(EditProfileActivity.this, "Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getDate(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String date = day + "/" + month + "/" + year;
        return date;
    }

    private void getProfile(String birthday, String gender, String email, String username, String password){
        dateView.setText(birthday);
        genderED.setText(gender);
        emailED.setText(email);
        usernameED.setText(username);
        passED.setText(password);
    }
}
