package com.example.michael.bodyfatapp.Recordings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Algorithms.FemaleAlgorithm;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;


public class NewRecordingFemale extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView mProfileDetails;
    private Button mCalMeasurementF;
    private EditText mChinField;
    private EditText mTricepFieldF;
    private EditText mSubscapFieldF;
    private EditText mHipField;
    private EditText mKneeBreadth, mWeight;

    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recording_female);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarTranslucent(true);

        mAuth = FirebaseAuth.getInstance();

        //link the profile details
        mProfileDetails = findViewById(R.id.ProfileView);
        mProfileDetails.setText("Profile: " + Measurements.nameList.get(Measurements.spinnerPosition));

        //retrieve widgets in the interface to interact with
        mCalMeasurementF = findViewById(R.id.CalMeasurementF);
        mChinField = findViewById(R.id.ChinNum);
        mTricepFieldF = findViewById(R.id.TricepNumF);
        mSubscapFieldF = findViewById(R.id.SubscapularNumF);
        mHipField = findViewById(R.id.hipCircumference);
        mKneeBreadth = findViewById(R.id.KneeBreadthNum);
        mWeight = findViewById(R.id.WeightNumF);

        //get the current user
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //get the uid
        String uid = mAuth.getUid();

        //enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCalMeasurementF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calMeasurementF();
            }
        });
    }

    //method to calculate the female body fat mass
    public void calMeasurementF() {
        String hip = mHipField.getText().toString();
        String tricep = mTricepFieldF.getText().toString();
        String subscap = mSubscapFieldF.getText().toString();
        String chin = mChinField.getText().toString();
        String knee = mKneeBreadth.getText().toString();
        String weight =mWeight.getText().toString();

       // convert the strings to doubles
        double dHip = Double.parseDouble(hip);
        double dTricep = Double.parseDouble(tricep);
        double dSubscap = Double.parseDouble(subscap);
        double dChin = Double.parseDouble(chin);
        double dKnee = Double.parseDouble(knee);
        double dWeight = Double.parseDouble(weight);

        //ensure all fields are entered
        if (TextUtils.isEmpty(hip) || TextUtils.isEmpty(tricep) || TextUtils.isEmpty(subscap)
                || TextUtils.isEmpty(chin) || TextUtils.isEmpty(knee) ||TextUtils.isEmpty(weight)) {
            Toast.makeText(NewRecordingFemale.this, "Please Enter All Fields", Toast.LENGTH_LONG).show();
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = mAuth.getUid();

            if (user != null) {

                //enter values to the femaleAlgorithm object to obtain a result.
                FemaleAlgorithm object = new FemaleAlgorithm(dTricep, dChin, dSubscap, dHip, dKnee, dWeight);

                //get the body fat result
                final Double BodyFatMass = object.getBodyFatMass(object);
                final Double bodyFatPercentage = object.getBodyFatPercentage();

                //calculate the time/date to be pushed to firebase
                Long date = System.currentTimeMillis();

                //create the object that holds the result as well as the measurement information.
                FemaleMeasurement record = new FemaleMeasurement(tricep, chin, subscap, hip,knee,weight, BodyFatMass.toString(),bodyFatPercentage.toString(), date);

                //push the object to the specific profile location determined by the profile keys
                mDatabase.child("Body_Fat_App").child(uid).child(Measurements.
                        profileKeys.get(Measurements.spinnerPosition)).child("Measurement").push().setValue(record).
                        addOnCompleteListener(NewRecordingFemale.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    //if the object has been pushed successfully start the measuremtns clas
                                    Toast.makeText(NewRecordingFemale.this, bodyFatPercentage.toString(), Toast.LENGTH_LONG).show();
                                    finish();
                                    Measurements.count --;
                                    startActivity(new Intent(NewRecordingFemale.this, Measurements.class));

                                } else
                                    Toast.makeText(NewRecordingFemale.this, "Database Problem", Toast.LENGTH_LONG).show();
                            }
                        });
            } else
                Toast.makeText(NewRecordingFemale.this, "Authentication problem", Toast.LENGTH_LONG).show();
        }
    }

    //hid the status bar method
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}