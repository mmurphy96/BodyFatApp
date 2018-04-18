package com.example.michael.bodyfatapp.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.example.michael.bodyfatapp.Login_Register.MainActivity;
import com.example.michael.bodyfatapp.Login_Register.Register;
import com.example.michael.bodyfatapp.Menu.MainMenu;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewProfile extends AppCompatActivity {

    private EditText mProfileName;
    private EditText mAgeField;
    private EditText mHeight;
    private EditText mWeight;
    private Spinner mGender;
    private String uid;

    private RelativeLayout mRelLayout;
    private Button mCompleteProfileButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarTranslucent(true);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mProfileName = (EditText) findViewById(R.id.selName);
        mAgeField = (EditText) findViewById(R.id.selAge);
        mHeight = (EditText) findViewById(R.id.selHeight);
        mWeight = (EditText) findViewById(R.id.selWeight);
        mGender = (Spinner) findViewById(R.id.selGender);
        mRelLayout = findViewById(R.id.profileLayout);


        //set the maximum amount of characters allowed in the fields
        mProfileName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        mAgeField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        mCompleteProfileButton = (Button) findViewById(R.id.CompleteProfile);

        //set the spinner drop down options
        String[] genders = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        mGender.setAdapter(adapter);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                }
            }
        };

        mCompleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewProfile();
            }
        });


    }

    private void startNewProfile() {
        // Firebase Upload new profile - gather the entered strings
        final String profileName = mProfileName.getText().toString();
        String gender = mGender.getSelectedItem().toString();
        String age = mAgeField.getText().toString();
        String weight = mWeight.getText().toString();
        String height = mHeight.getText().toString();


        //If new any of the  new profile fields are empty
        if (TextUtils.isEmpty(profileName) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age)
                || TextUtils.isEmpty(weight) || TextUtils.isEmpty(height)) {
            Toast.makeText(NewProfile.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }
            //Ensure a profile name can only be used once on the account
                if (Register.register = false){

                if (MainActivity.profileNameList.contains(profileName)) {
                    mProfileName.setFocusable(true);
                    mProfileName.setError("Profile Name is already in use!");
                }
        }
                else {
                    //UID is used for to set the profile details for this User
                    //create the new profile on the json file and push the details from the
                    //NewProfile Page.
                    //Adding multiple profiles has been implemented
                    FirebaseUser user = mAuth.getCurrentUser();

                    mDatabase = FirebaseDatabase.getInstance().getReference();


                    final ProfileDetails profile = new ProfileDetails(profileName, age, gender, height, weight);
                    //if the user is signed in push the new profile object
                    if (user != null) {
                        mDatabase.child("Body_Fat_App").child(uid).push().setValue(profile)
                                .addOnCompleteListener(NewProfile.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(NewProfile.this, "Profile Created", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(NewProfile.this, MainMenu.class));
                                        } else
                                            Toast.makeText(NewProfile.this, "Profile Creation Error", Toast.LENGTH_LONG).show();

                                    }
                                });
                    } else
                        Toast.makeText(NewProfile.this, "Authentication problem", Toast.LENGTH_LONG).show();

                }

    }
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

