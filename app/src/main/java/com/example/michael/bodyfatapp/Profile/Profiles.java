package com.example.michael.bodyfatapp.Profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Account.MyAccount;
import com.example.michael.bodyfatapp.Menu.MainMenu;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Profiles extends AppCompatActivity {

    private ListView mProfilesList;
    private Button mAddProfile, mBackButton ;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> names = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Dialog dialog, delDialog;
    private String name, age, gender, weight, height, profileUID;
    private int count, itemClickPosition;
    private List <ProfileDetails> profileList = new ArrayList<ProfileDetails>();
    private List <String> keyList  = new ArrayList<String>();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        //create a new dialog for the pop up window
        dialog = new Dialog(this);
        delDialog = new Dialog(Profiles.this);

        //set the buttons and text fields
        mProfilesList = (ListView) findViewById(R.id.profilesList);
        mBackButton = (Button) findViewById(R.id.BackButton);
        mAddProfile = findViewById(R.id.addProfile);

        //when the back button is hit
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Profiles.this, MainMenu.class));
            }
        });


        //set the adapter for the list to include the list of profile names
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                //Set your Font Size Here.
                textview.setTextSize(20);
                return view;
            }
        };

        mProfilesList.setAdapter(adapter);

        String uid = mAuth.getUid();

        //create the database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(uid);

        //Get the names of the children for the profile list
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){

                    String profileName = dataSnapshot.child("name").getValue(String.class);
                    //Toast.makeText(Profiles.this, profileName, Toast.LENGTH_SHORT).show();
                    count++;
                    //the profile list should show with the format "1. Michael 2.John
                    String visibleProfileName = count +": "+ profileName;
                    //add the names to the list used to populate the list item
                    names.add(visibleProfileName);
                    //notify the adapter of the change in the data set
                    adapter.notifyDataSetChanged();
                    //set the list to visible
                    mProfilesList.setVisibility(View.VISIBLE);

                    //get the various profile details
                    name = dataSnapshot.child("name").getValue(String.class);
                    age = dataSnapshot.child("age").getValue(String.class);
                    weight = dataSnapshot.child("weight").getValue(String.class);
                    gender = dataSnapshot.child("gender").getValue(String.class);
                    height = dataSnapshot.child("height").getValue(String.class);
                    String key = dataSnapshot.getKey();

                    //add a new profile to the profile list
                    //each profile here will contain the name,age,height,weight,gender of that applicant.

                    profileList.add(new ProfileDetails(name,age,gender,height,weight));
                    keyList.add(key);
                //    Toast.makeText(Profiles.this, String.valueOf(profileList.size()), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Profiles.this, "Could not get Profile Information", Toast.LENGTH_SHORT).show();
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


        mAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profiles.this,NewProfile.class));

            }
        });
        // at this stage a profile (profile Det) has been created and added to the Profile List
        // we know need to select which profile information needs to be displayed based on the
        //selected item
        mProfilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //the position integer refers to the position in the list of profiles that was clicked
                // this integer can be used to select the position in the array list holding all the profile information
               // Toast.makeText(Profiles.this,names.get(position),Toast.LENGTH_SHORT).show();
                itemClickPosition = position;
                showPopUp();
            }
        });

    }
    public void showPopUp(){
        dialog.setContentView(R.layout.customprofilepopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       // mName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        //mAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

        final Button mClosePopUp, mDelProfile;
        Button mUpdateProfile;
        final EditText mName,mAge,mHeight,mWeight;
        final Spinner mGender;

        mClosePopUp = dialog.findViewById(R.id.closeButton);
        mName = dialog.findViewById(R.id.ProfileName);
        mAge = dialog.findViewById(R.id.ProfileAge);
        mHeight = dialog.findViewById(R.id.ProfileHeight);
        mWeight = dialog.findViewById(R.id.ProfileWeight);
        mGender = dialog.findViewById(R.id.ProfileGender);
        mGender.setClickable(false);
        mGender.setEnabled(false);
        mUpdateProfile = dialog.findViewById(R.id.UpdateProfile);
        mDelProfile = dialog.findViewById(R.id.delProfile);



        String[] genders = new String[]{"Male", "Female"};
        ArrayAdapter<String> GenderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        mGender.setAdapter(GenderAdapter);

        //create a new profile object
        final ProfileDetails popUpProfile;
        //set the profile object = to the one stored in the array list via the clicked option.
        popUpProfile = profileList.get(itemClickPosition);

        //Display the current profile information on the various fields in the popup
        mName.setText(popUpProfile.getName());
        mAge.setText(popUpProfile.getAge());
        mHeight.setText(popUpProfile.getHeight());
        mWeight.setText(popUpProfile.getWeight());

        //As the gender is an option field this works slighly different
        //as a spinner needs to be set
        String profileGender = popUpProfile.getGender();
        int genderPosition;
        if( profileGender.equals( "Male")){
            genderPosition = 0;
        }
        else{
            genderPosition = 1;
        }
        mGender.setSelection(genderPosition);



        //The firebase information should be updated when the button Update Profile is called.

        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first ensure all the fields are entered
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(height)||
                        TextUtils.isEmpty(weight)||TextUtils.isEmpty(age)){
                    Toast.makeText(Profiles.this, "Please Enter All Fields", Toast.LENGTH_LONG).show();

                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String profileUID = mAuth.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(profileUID).child(keyList.get(itemClickPosition));
                    if (user!= null){
                        mDatabase.child("name").setValue(mName.getText().toString());
                        mDatabase.child("age").setValue(mAge.getText().toString());
                        mDatabase.child("height").setValue(mHeight.getText().toString());
                        mDatabase.child("weight").setValue(mWeight.getText().toString());
                        mDatabase.child("gender").setValue(mGender.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //if the upload was successful close the dialog window and start the activity again to update the information
                                if( task.isSuccessful()){
                                    Toast.makeText(Profiles.this, "Updating Profile Information", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent (Profiles.this,Profiles.class));
                                }
                            }
                        });


                    }
                }
            }


        });

        mDelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                delDialog.setContentView(R.layout.custom_delete_profile);
                delDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                user = mAuth.getCurrentUser();
                profileUID = mAuth.getUid();
                //create the buttons and fields needed
                Button mDelCloseButton, mConfirmDelete;
                final EditText mPasswordConfirm;

                mDelCloseButton = delDialog.findViewById(R.id.dCloseButton);
                mConfirmDelete = delDialog.findViewById(R.id.confirmDelete);
                mPasswordConfirm = delDialog.findViewById(R.id.passwordConfirm);

                //set the listener for the item is clicked
                mConfirmDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the user details
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = user.getEmail();

                        //perform the re-authentication before delting the account
                        if (mPasswordConfirm.getText().toString().isEmpty()){
                            //ensure the password is entered first
                            Toast.makeText(Profiles.this, "Password must be confirmed first", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //set the credential object used in the re-auth method
                            AuthCredential credential = EmailAuthProvider.getCredential(email, mPasswordConfirm.getText().toString());
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //if successful delete the account and start the login activity
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Body_Fat_App").child(profileUID).child(keyList.get(itemClickPosition));
                                        mDatabase.removeValue();
                                        Toast.makeText(Profiles.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
                                        delDialog.dismiss();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);

                                    }
                                    else{
                                        Toast.makeText(Profiles.this, "Password Incorrect", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });
                mDelCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delDialog.dismiss();
                    }
                });
                delDialog.show();

            }
        });

        //Close the pop up - should return to the main profiles section
        mClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    dialog.show();

    }
}
