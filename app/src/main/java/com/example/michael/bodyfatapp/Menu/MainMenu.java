package com.example.michael.bodyfatapp.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.michael.bodyfatapp.Graphs.GraphMain;
import com.example.michael.bodyfatapp.Login_Register.MainActivity;
import com.example.michael.bodyfatapp.Account.MyAccount;
import com.example.michael.bodyfatapp.Profile.Profiles;
import com.example.michael.bodyfatapp.R;
import com.example.michael.bodyfatapp.Recordings.Measurements;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainMenu extends  MainActivity {

    private Button mMeasurements;
    private Button mProfiles;
    private Button mGraphs;
    private Button mMyAccount;


    public static ArrayList<String> profileNames = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setStatusBarTranslucent(true);
        //hide the status bar
        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        //get the profile uid
        profileNames.clear();
        //profile names need to be cleared when the activity is started to avoid duplicated
        if (mAuth != null){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(uid);
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //get the profile name and add it to the list
                    String pName = dataSnapshot.child("name").getValue(String.class);
                    profileNames.add(pName);
                //    Toast.makeText(MainMenu.this, profileNames.toString(), Toast.LENGTH_SHORT).show();

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
        }

        mMeasurements = findViewById(R.id.MeasurementButton);
        mGraphs = findViewById(R.id.GraphsButton);
        mMyAccount = findViewById(R.id.AccountButton);
        mProfiles = findViewById(R.id.profilebutton);

        //upon clicking on of the menu buttons start the corresponding activity
        mMeasurements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Measurements.class));
            }
        });

        mMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, MyAccount.class));
            }
        });

        mGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, GraphMain.class));
            }
        });

        mProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainMenu.this, Profiles.class));
            }
        });
    }

    //method to set the status bar translucent
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
