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

import com.example.michael.bodyfatapp.Algorithms.MaleAlgorithm;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;


public class NewRecordingMale extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView mProfile;
    private Button mCalMeasurement;
    private EditText mAbdominalField;
    private EditText mTricepField;
    private EditText mSubscapField;
    private EditText mWaistField, mWeightField;

    private SimpleDateFormat simpleDateFormat;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recording_male);

        mAuth = FirebaseAuth.getInstance();
        mProfile = findViewById(R.id.ProfileSel);
        mProfile.setText("Profile: "+Measurements.nameList.get(Measurements.spinnerPosition));
        mCalMeasurement = findViewById(R.id.CalMeasurement);
        mAbdominalField = findViewById(R.id.abdominalNum);
        mTricepField = findViewById(R.id.TricepNum);
        mSubscapField = findViewById(R.id.SubscapularNum);
        mWaistField = findViewById(R.id.WaistNum);
        mWeightField = findViewById(R.id.WeightNum);

        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = mAuth.getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarTranslucent(true);

        //when the calculate button is pressed call the method
        mCalMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //call the method to calculate and push the results
               calMeasurement();
            }
        });


    }
    //calc
    public void calMeasurement(){
        String abdominal = mAbdominalField.getText().toString();
        String tricep = mTricepField.getText().toString();
        String subscap = mSubscapField.getText().toString();
        String waistCir = mWaistField.getText().toString();
        String weight = mWeightField.getText().toString();

        //convert the strings to doubles
        double dAbdominal = Double.parseDouble(abdominal);
        double dTricep = Double.parseDouble(tricep);
        double dSubcap = Double.parseDouble(subscap);
        double dWaistCir = Double.parseDouble(waistCir);
        double dWeight = Double.parseDouble(weight);

        //ensure all the fields are entered, if so continue with the push
        if (TextUtils.isEmpty(abdominal) || TextUtils.isEmpty(tricep) || TextUtils.isEmpty(subscap)
                ||TextUtils.isEmpty(waistCir)) {

            Toast.makeText(NewRecordingMale.this, "Please Enter All Fields", Toast.LENGTH_LONG).show();
        }
        else{
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = mAuth.getUid();

            //if the user is till signed in or the connection is still active
            if (user != null) {

                //create the object to calculate the values
                //assign the doubles
                MaleAlgorithm object = new MaleAlgorithm(dTricep,dAbdominal,dSubcap,dWaistCir,dWeight);
                //get the mass and percentage body fat
                final Double BodyFatMass =  object.getBodyFatMass(object);
                final Double BodyFatePercentage = object.getBodyFatPercentage();

                //get the dat of the recording
                simpleDateFormat = new SimpleDateFormat("dd-MM");
                Long date = System.currentTimeMillis();
          //      System.out.println(simpleDateFormat);

                //create the malemeasurment object that can be pushed to firebase
                MaleMeasurement record = new MaleMeasurement(tricep,abdominal,subscap,waistCir,weight,BodyFatMass.toString(),BodyFatePercentage.toString(),date);

                //push the object using the profile keys postion
                //this position is the location of the profile key
                mDatabase.child("Body_Fat_App").child(uid).child(Measurements.profileKeys.get(Measurements.spinnerPosition)).child("Measurement").push().setValue(record).
                        addOnCompleteListener(NewRecordingMale.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //if the task is successful start the activity
                        if (task.isSuccessful()) {
                            Toast.makeText(NewRecordingMale.this, BodyFatePercentage.toString(), Toast.LENGTH_LONG).show();
                            finish();
                            Measurements.count --;
                            startActivity(new Intent(NewRecordingMale.this, Measurements.class));

                        } else
                            Toast.makeText(NewRecordingMale.this, "Database Problem", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else Toast.makeText(NewRecordingMale.this, "Authentication problem",Toast.LENGTH_LONG).show();
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
