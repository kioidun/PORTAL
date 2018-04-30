package com.example.kioi.jkuatportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button mLogin;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin=(Button)findViewById(R.id.login);
        mRegister=(Button)findViewById(R.id.Register);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login= new Intent(LoginActivity.this,MainActivity.class);
                startActivity(login);
                finish();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);

            }
        });
    }
}
