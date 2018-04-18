package com.example.michael.bodyfatapp.Login_Register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Profile.NewProfile;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends MainActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button dRegisterButton;

    private FirebaseAuth mAuth;
    public static boolean register = false;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setStatusBarTranslucent(true);
        getSupportActionBar().show();

        mAuth = FirebaseAuth.getInstance();

        mEmailField =  (EditText) findViewById(R.id.emailFieldAcc);
        mPasswordField = (EditText) findViewById(R.id.passwordFieldAcc);

        dRegisterButton = findViewById(R.id.registerAcc);

        dRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the registration message
                startRegistration();
            }
        });
    }
    private void startRegistration(){
        //convert the entered edit text fields to strings
        String email = mEmailField.getText().toString();
        final String password  = mPasswordField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Register.this, "Please Enter Both Fields", Toast.LENGTH_LONG).show();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Registration Complete!", Toast.LENGTH_LONG).show();
                        register = true;
                        startActivity(new Intent(Register.this, NewProfile.class));
                    }
                    else{
                        Toast.makeText(Register.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
