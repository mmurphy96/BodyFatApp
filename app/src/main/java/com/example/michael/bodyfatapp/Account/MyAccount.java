package com.example.michael.bodyfatapp.Account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.bodyfatapp.Login_Register.MainActivity;
import com.example.michael.bodyfatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyAccount extends AppCompatActivity {
    //create text views an buttons
    private TextView mEmailReset, mPasswordReset, mDeleteAccount,mShowBugs, mInfo,mBugs;
    private Button mSignOut;
    public ArrayList<String> measurements = new ArrayList<>();
    private String newEmail,email = "";
    private FirebaseAuth mAuth;
    private Dialog dialog, delDialog;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarTranslucent(true);

        //create a new dialog windows
        dialog = new Dialog(MyAccount.this);
        delDialog = new Dialog(MyAccount.this);

        //create the Firebase Authorisation object
        mAuth = FirebaseAuth.getInstance();
        //get the current users email
        email = mAuth.getCurrentUser().getEmail();

        //assign the java items to the XML items
        mSignOut = findViewById(R.id.signOutButton);
        mEmailReset = findViewById(R.id.changeEmail);
        mPasswordReset =findViewById(R.id.accChangePassword);
        mDeleteAccount = findViewById(R.id.delAccount);
        mShowBugs = findViewById(R.id.ShowBugs);
        mBugs = findViewById(R.id.text1);
        mInfo = findViewById(R.id.info);
        mBugs.setVisibility(View.INVISIBLE);
        mInfo.setVisibility(View.INVISIBLE);


        //When the sign out button is clicked, sign the user out.

        mShowBugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBugs.setVisibility(View.VISIBLE);
                mInfo.setVisibility(View.VISIBLE);
            }
        });
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignOut();
            }
        });


        //when the password button is clicked send the password reset email using users email
        mPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MyAccount.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            }
        });

        // when the email reset is clicked call the method to show the window
        mEmailReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEmailReset();
            }
        });

        //call the delete account method dialog when the delete button is clicked
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDeleteAccount();

            }
        });

    }

    //start the sign out process
    public void startSignOut(){

                    FirebaseAuth.getInstance().signOut();
                    //start the login screen
                    startActivity(new Intent(MyAccount.this, MainActivity.class));
    }


    public void popUpEmailReset(){

        //set the new dialog window
        dialog.setContentView(R.layout.custom_email_change);
        //set the background of the dialog to be transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //create the new buttons and text views
        final EditText mCurrentEmail,mNewEmail;
        Button mUpdateEmail,mAccClosePopUp;

        mAccClosePopUp = dialog.findViewById(R.id.AccCloseButton);
        mCurrentEmail = dialog.findViewById(R.id.currentEmail);
        mNewEmail = dialog.findViewById(R.id.newEmailAdd);
        mUpdateEmail = dialog.findViewById(R.id.bUpdateEmail);

        mCurrentEmail.setText(email);

        //when the Update email button is clicked
        mUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //get the current user details
                newEmail = mNewEmail.getText().toString().trim();
                //ensure the field is not empty
                if (newEmail.isEmpty() ){
                    Toast.makeText(MyAccount.this, "Please enter New email address above", Toast.LENGTH_SHORT).show();
                    //set the focus back to the new email field
                    mNewEmail.setFocusable(true);
                }
                else {
                    user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //if successful sign out and open the login screen
                            if(task.isSuccessful()){
                                startSignOut();
                                Toast.makeText(MyAccount.this, "Email Changed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MyAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
        //if the close buttons is selected
        mAccClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void popUpDeleteAccount(){
        //set the custom dialog
        delDialog.setContentView(R.layout.custom_delete_profile);
        delDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //create the buttons and fields needed
        Button mDelCloseButton, mConfirmDelete;
        final EditText mPasswordConfirm;

        mDelCloseButton = delDialog.findViewById(R.id.dCloseButton);
        mConfirmDelete = delDialog.findViewById(R.id.confirmDelete);
        mConfirmDelete.setText("Confirm Account Delete");
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
                    Toast.makeText(MyAccount.this, "Password must be confirmed first", Toast.LENGTH_SHORT).show();
                }
                else{
                    //set the credential object used in the re-auth method
                    AuthCredential credential = EmailAuthProvider.getCredential(email, mPasswordConfirm.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //if successful delete the account and start the login activity
                                user.delete();
                                startSignOut();
                                Toast.makeText(MyAccount.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MyAccount.this, "Password Incorrect", Toast.LENGTH_SHORT).show();

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
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
