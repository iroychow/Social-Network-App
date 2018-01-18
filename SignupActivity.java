package com.example.ishitaroychowdhury.socialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText etfirstname,etlastname,etemail,etpassword,etpassword2,etage;
    Button signup;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etfirstname = (EditText) findViewById(R.id.etfirstname);
        etlastname = (EditText) findViewById(R.id.etlastname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etPassword);
        etpassword2 = (EditText) findViewById(R.id.etpassword2);
        etage = (EditText) findViewById(R.id.etAge);
        signup = (Button) findViewById(R.id.btsignup2);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = new User();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(etage.getText().toString()) > 13) {
                    if (CheckFieldValidator.checkField(etlastname) && CheckFieldValidator.checkField(etemail) &&
                            CheckFieldValidator.checkField(etpassword) && CheckFieldValidator.checkField(etfirstname) &&
                            CheckFieldValidator.checkField(etpassword2)) {
                        if (CheckFieldValidator.passwordValidation(etpassword.getText().toString(),
                                etpassword2.getText().toString())) {
                            mAuth.createUserWithEmailAndPassword(etemail.getText().toString(),
                                    etpassword.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // AuthResult res = task.getResult();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        user.setEmail(etemail.getText().toString());
                                        user.setFirstname(etfirstname.getText().toString());
                                        user.setLastname(etlastname.getText().toString());
                                        user.setAge(Integer.valueOf(etage.getText().toString().trim()));
                                        user.setId(mAuth.getCurrentUser().getUid());
                                        Map<String, Object> postValues = user.toMap();
                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("/Users/" + mAuth.getCurrentUser().getUid(), postValues);
                                        myRef.updateChildren(childUpdates);
                                        Toast.makeText(SignupActivity.this, "User created successfully",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(SignupActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                        }

                    } else{
                        Toast.makeText(SignupActivity.this, "Please enter user details", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(SignupActivity.this, "User age should be more than 13 years", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
