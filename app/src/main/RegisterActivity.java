package com.example.kioi.jkuatportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private EditText mStudentName;
    private EditText mStudentRegNo;
    private EditText mCourse;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button  mRegister;

    //firebase variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStudentName=(EditText)findViewById(R.id.studentName);
        mStudentRegNo=(EditText)findViewById(R.id.studentReg);
        mCourse=(EditText)findViewById(R.id.course);
        mEmail=(EditText)findViewById(R.id.mail);
        mPassword=(EditText)findViewById(R.id.password);
        mConfirmPassword=(EditText)findViewById(R.id.confirmpassword);


        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });
         //store an instance of the firebase class in the firebase auth
        mAuth=FirebaseAuth.getInstance();
    }
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmail.setError(null);
        mConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mConfirmPassword.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password
        String confirmPassword=mConfirmPassword.getText().toString();
        return confirmPassword.equals(password)&& password.length()>4;
    }

    // TODO: Create a Firebase user
    private void createFirebaseUser(){

        String email = mEmail.getText().toString();
        String password=mConfirmPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //REPORT BACK IF THE CREATION OF USER HAS BEEN SUCCESFUL OR HAS AN ERROR
                Log.d("JKUATPORTAL","createUser onComplete:" + task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.d("JKUATPORTAL","user creation failed");
                    showErrorDialog("Registration attempt failed");

                }else{
                    saveDisplayName();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
        });

    }
    //save data to shared preferences
    private void saveDisplayName(){
        String displayName=mStudentName.getText().toString();
        SharedPreferences prefs =getSharedPreferences(CHAT_PREFS,0);
        prefs.edit().putString(DISPLAY_NAME_KEY,displayName).apply();
    }
    //creating an alert dialog to show incase registration fails
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

