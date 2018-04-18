package com.example.michael.bodyfatapp.Recordings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Login_Register.MainActivity;
import com.example.michael.bodyfatapp.Profile.ProfileDetails;
import com.example.michael.bodyfatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Measurements extends MainActivity {

    private Button mAddNewMeas, mDelMeas;
    private Spinner mSelProfile;
    public static ArrayList<String> nameList = new ArrayList<>();
    private ArrayAdapter <String> profileAdapter;
    public String name, age, gender, height, weight, genderCheck;
    private int i =0;

    public static int spinnerPosition,count;
    private List <ProfileDetails> profileList = new ArrayList<ProfileDetails>();
    private  List <String> measIDs = new ArrayList<>();
    public static List <String> profileKeys = new ArrayList<>();


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private  static List<String> listHeader;
    private static HashMap<String, List<String>> listChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);
        getSupportActionBar().show();

        mDelMeas= findViewById(R.id.delMeas);
        listView = findViewById(R.id.measurementList);
        listChild = new HashMap<String, List<String>>();
        listHeader = new ArrayList<String>();
        mAddNewMeas = findViewById(R.id.newMeas);
        mSelProfile = findViewById(R.id.selectProfile);

        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getUid();

        //Set the selector equal to the list of profile names pulled from the main menu
        profileAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
        profileAdapter.clear();
        //get the Spinner selection


        //PULL THE LIST OF MEASUREMENTS PLUS DATE FROM FIREBASE
        //reference to the exact location the dataSnapshot will pull from


        if (mAuth != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(uid);
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {

                        //Get each of the profile names for the spinner
                        String profileName = dataSnapshot.child("name").getValue(String.class);
                        nameList.add(profileName);


                        name = dataSnapshot.child("name").getValue(String.class);
                        age = dataSnapshot.child("age").getValue(String.class);
                        weight = dataSnapshot.child("weight").getValue(String.class);
                        gender = dataSnapshot.child("gender").getValue(String.class);
                        height = dataSnapshot.child("height").getValue(String.class);
                        String key = dataSnapshot.getKey();
                        //add a new profile to the profile list
                        //each profile here will contain the name,age,height,weight,gender of that applicant.
                        profileList.add(new ProfileDetails(name, age, gender, height, weight));
                        profileKeys.add(key);
                        //  Toast.makeText(Measurements.this, String.valueOf(profileList.size()), Toast.LENGTH_SHORT).show();
                    }

                    //check the spinner selection
                    //if the spinner selections profile gender is male start the male measurement activity
                    //else (female) start the female activity
                    mSelProfile.setAdapter(profileAdapter);


                    mAddNewMeas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            spinnerPosition = mSelProfile.getSelectedItemPosition();
                             genderCheck = profileList.get(spinnerPosition).gender;
                            if (genderCheck.equals("Male")) {
                                //start the activity that uses the male algorithm
                                startActivity(new Intent(Measurements.this, NewRecordingMale.class));

                            } else {
                                //  Toast.makeText(Measurements.this, genderCheck, Toast.LENGTH_SHORT).show();
                                    //start the activity that uses the female algorithm
                                if (genderCheck.equals("Female")){

                                    startActivity(new Intent(Measurements.this, NewRecordingFemale.class));
                                }
                            }
                        }
                    });


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

      //  adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allResults);
        listAdapter = new ExpandableList(Measurements.this,listHeader,listChild);




        mSelProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //adapter.clear();
          //      Toast.makeText(Measurements.this, "Retrieving!", Toast.LENGTH_SHORT).show();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App")
                        .child(mAuth.getUid()).child(profileKeys.get(position)).child("Measurement");

                count = 0;
                listHeader.clear();
                measIDs.clear();
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (dataSnapshot.exists()) {
                            //Retrieve the measurements for the profile
                            String measID = dataSnapshot.getKey();
                            measIDs.add(measID);
                            String value = dataSnapshot.child("BodyFatPercentage").getValue(String.class);
                            Long date = dataSnapshot.child("Date").getValue(Long.class);
                            if (date != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                                String dateString = dateFormat.format(new Date(date));

                                spinnerPosition = mSelProfile.getSelectedItemPosition();
                                genderCheck = profileList.get(spinnerPosition).gender;

                                if (value.length()>5){
                                    listHeader.add(dateString+ ": "+ "Body Fat: "+ value.substring(0,5) + "%");
                                }
                                else{
                                    listHeader.add(dateString+ ": "+ "Body Fat: "+ value+ "%");
                                }
                                List<String> allMeas = new ArrayList<String>();
                                count=0;
                                if(genderCheck.equals("Female")) {
                                    String trimBFM ="";
                                    String bfm = dataSnapshot.child("BodyFatMass").getValue(String.class);
                                    if(bfm.length()>5){
                                        trimBFM = bfm.substring(0,5);
                                    }
                                    else{
                                        allMeas.add("Body Fat Mass: "+ trimBFM + " kg");
                                    }
                                    allMeas.add("Weight: " + dataSnapshot.child("Weight").getValue(String.class)+ " kg");
                                    allMeas.add("Chin: " + dataSnapshot.child("Chin").getValue(String.class)+ " mm");
                                    allMeas.add("Knee Breadth: " + dataSnapshot.child("KneeBreadth").getValue(String.class)+ " cm");
                                    allMeas.add("Subscapular: " +dataSnapshot.child("Subscapular").getValue(String.class)+ " mm");
                                    allMeas.add("Tricep: "+ dataSnapshot.child("Tricep").getValue(String.class)+ "mm");
                                    allMeas.add("Hip Circumference: " + dataSnapshot.child("HipCircumference").getValue(String.class)+ " cm");

                                    listChild.put(listHeader.get(count), allMeas);
                                    count++;
                                    listView.invalidateViews();
                                }

                                if(genderCheck.equals("Male")){
                                    String trimBFM = "";
                                    String bfm = dataSnapshot.child("BodyFatMass").getValue(String.class);
                                    if(bfm.length()>5){
                                        trimBFM = bfm.substring(0,5);
                                    }
                                    else{
                                        allMeas.add("Body Fat Mass: "+ trimBFM + " kg");
                                    }
                                    allMeas.add("Body Fat Mass: "+ trimBFM + " kg");
                                    allMeas.add("Weight: " + dataSnapshot.child("Weight").getValue(String.class)+ " kg");
                                    allMeas.add("Abdominal: "+ dataSnapshot.child("Abdominal").getValue(String.class) + " mm");
                                    allMeas.add("Subscapular: "+ dataSnapshot.child("Subscapular").getValue(String.class)+ " mm");
                                    allMeas.add("Tricep: "+ dataSnapshot.child("Tricep").getValue(String.class)+ " mm");
                                    allMeas.add("Waist Circumference: "+ dataSnapshot.child("WaistCircumference").getValue(String.class)+ " cm");

                                    listChild.put(listHeader.get(count), allMeas);
                                    count++;
                                    listView.invalidateViews();
                                }
                            }
                            listView.setAdapter(listAdapter);


                     //       Toast.makeText(Measurements.this, "complete!", Toast.LENGTH_SHORT).show();

                        }


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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Measurements.this,"Please select a Profile",Toast.LENGTH_SHORT).show();
            }

        });


        mDelMeas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    for (i =0; i<listHeader.size(); i++){
                        if (listView.isGroupExpanded(i)){

                                    mDelMeas.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App")
                                                    .child(mAuth.getUid()).child(profileKeys.get(mSelProfile.getSelectedItemPosition())).child("Measurement");

                                            mDatabase.child(measIDs.get(i-1)).removeValue();
                                            Toast.makeText(Measurements.this, "Measurement Deleted!", Toast.LENGTH_SHORT).show();
                                        listHeader.remove(i-1);
                                        listChild.remove(i-1);
                                        listView.invalidateViews();
                                        }
                                    });


                        }
                        else {
                            Toast.makeText(Measurements.this, "First, select a result to delete!", Toast.LENGTH_SHORT).show();

                        }
                    }


            }
        });


        }

        @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Call the method get profile data from the main menu,
        //this is needed when as a new measurement may be added
        getProfileData();
      //  Toast.makeText(Measurements.this, "Data Pulled from mainActivity", Toast.LENGTH_SHORT).show();


    }




}


