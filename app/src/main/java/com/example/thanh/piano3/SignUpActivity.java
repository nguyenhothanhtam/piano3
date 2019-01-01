package com.example.thanh.piano3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    ImageButton imgBack;
    Button btnDate, btnSign;
    DatePicker datePicker;
    TextView dateView;
    EditText genderED, emailED, usernameED, passED;

    FirebaseDatabase database;
    DatabaseReference users;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
        database.getReference("app_title").setValue("Piano1");
        database.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read app title value.", databaseError.toException());
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

                if (name.equals("") || pass.equals("") || mail.equals("")  || gender.equals("") || date.equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "dien khong du thong tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                users.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String err = "Email da ton tai";
                            Toast.makeText(SignUpActivity.this, err, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            createUser(name, mail, pass, gender, date);
                            String notice = "Dang ky thanh cong";
                            Toast.makeText(SignUpActivity.this, notice, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

    private void createUser(String name, String email, String pass, String gender, String date) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = users.push().getKey();
        }
        User user = new User(date, gender, email, pass, name);
        users.child(userId).setValue(user);
        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        users.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.getUsername() + ", " + user.getEmail());

                // Display newly updated name and email;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}
