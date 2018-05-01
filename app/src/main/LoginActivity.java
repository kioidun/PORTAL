package com.example.kioi.jkuatportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mLoginButton;
    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail=(EditText)findViewById(R.id.email);
        mPassword=(EditText)findViewById(R.id.password) ;
        mLoginButton=(Button)findViewById(R.id.loginButton);
        mRegister=(Button)findViewById(R.id.Register);


        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);

            }
        });
        mAuth=FirebaseAuth.getInstance();
    }
    public void signInExistingUser(View v){
        attemptLogin();

    }
    private void attemptLogin(){
        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();

        if(email.equals("") || password.equals(""))return;
        Toast.makeText(getApplication(),"Login in Progress...",Toast.LENGTH_SHORT).show();

        //using firebase email and password
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("JKUATPORTAL","signInWithEmail() onComplete: "+task.getException());

                if(!task.isSuccessful()){
                    Log.d("JKUATPORTAL","problem signing in:"+ task.getException());
                    showErrorDialog("problem signing in");
                }else{
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    finish();
                    startActivity(intent);

                }
            }
        });

    }
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
