package com.example.michael.bodyfatapp.Login_Register;

// by default display all results over time
//by selection choose a site and display those readings over time


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Menu.MainMenu;
import com.example.michael.bodyfatapp.Profile.ProfileDetails;
import com.example.michael.bodyfatapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLoginButton;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private String keys;
    private TextView mForgotPassword;

    public static Map<String, Object> hashmap = new HashMap<String, Object>();

    //array list to hold the account profile names
    public static ArrayList<String> profileNameList;

    public static ArrayList<String> profileGenderList = new ArrayList<>();

    private FirebaseAuth mAuth;

    //creating the strings to hold profile information
    public static String name, age, gender, weight, height;
    public static ProfileDetails profile = new ProfileDetails(name, age, gender, height, weight);


    public static ArrayList<String> measurements = new ArrayList<>();
    public static List<Object> values = new ArrayList<>();


    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.michael.bodyfatapp.R.layout.activity_main);
        getSupportActionBar().hide();
        setStatusBarTranslucent(true);
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginbutton);
        mRegisterButton = (Button) findViewById(R.id.register);
        mForgotPassword = findViewById(R.id.forgotPassword);

        mEmailField.setText("123@gmail.com");
        mPasswordField.setText("123456");

        mImageView = findViewById(R.id.imageView);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);


        //create a bitmap for the round icon
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bodyfaticon_round);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        mImageView.setImageDrawable(roundedBitmapDrawable);

        //if signed go to page
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                }
            }
        };
        //when the login button is pressed..
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.setClickable(false);
                mRegisterButton.setClickable(false);
                mPasswordField.setClickable(false);
                mPasswordField.setFocusable(false);
                mEmailField.setClickable(false);
                mEmailField.setFocusable(false);

                startSignIn();

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the Register Class
                startActivity(new Intent(MainActivity.this, Register.class));

            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailField.getText().toString().isEmpty()){
                    //ask the user to enter their email above so that email can be sent
                    Toast.makeText(MainActivity.this, "Please Enter Email above first", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(mEmailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();

                        }
                        }
                    });

                }
            }
        });

    }

    //begin the sign in process
    private void startSignIn() {

        String email = mEmailField.getText().toString();
        final String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please Enter Both Fields", Toast.LENGTH_LONG).show();
            mLoginButton.setClickable(true);
            mRegisterButton.setClickable(true);
            mPasswordField.setClickable(true);
            mPasswordField.setFocusable(true);
            mEmailField.setClickable(true);
            mEmailField.setFocusable(true);

        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            //Begin firebase sign in with email and password
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //Request profile data
                    getProfileData();
                    //update the progress bar
                    mProgressBar.setProgress(90);
                    //if the users credential are correct then ->
                    if (task.isSuccessful()) {
                        //hide the progress bar
                        mProgressBar.setProgress(100);
                        //close the activity
                        finish();
                        //launch main menu upon completion
                        startActivity(new Intent(MainActivity.this, MainMenu.class));
                    } else {
                        mProgressBar.setProgress(100);
                        Toast.makeText(MainActivity.this, "Authentication problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Gather the user profile and measurement information from the database
    public void getProfileData() {
        //initiate the array list to hold profile names
        profileNameList = new ArrayList<>();
        String uid = mAuth.getUid();

        //reference to the location of the profile data
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(uid);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //get each of the profile names and add them to the array list
                String profileName = dataSnapshot.child("name").getValue(String.class);
                profileNameList.add(profileName);

                //hash map stores the user measurement information
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.child("Measurement").getValue();

                if (map != null) {
                    values = new ArrayList<>(map.values());
                    keys = (map.keySet().toString());
                    hashmap.put(profileName, values);
                    hashmap.size();
                }
                //if the gender is equal to male we want to create add each object to a malMeas list

               // Toast.makeText(MainActivity.this, profileNameList.toString(), Toast.LENGTH_SHORT).show();

                String gender = dataSnapshot.child("gender").getValue(String.class);
                profileGenderList.add(gender);

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(MainActivity.this, "Exit", Toast.LENGTH_SHORT).show();

    }



    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

