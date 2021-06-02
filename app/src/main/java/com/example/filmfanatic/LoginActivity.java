package com.example.filmfanatic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filmfanatic.maps.TheaterActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName=findViewById(R.id.etUserName);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignUp=findViewById(R.id.btnSignUp);

        getSupportActionBar().hide();
        Window window = getWindow();      //setting status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.coolgreen));


        if(ParseUser.getCurrentUser() != null) {
            Log.i("login activity", "User already logged in; starting MainActivity");
            goMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserName=etUserName.getText().toString();
                String PassWord=etPassword.getText().toString();

                ParseUser.logInInBackground(UserName, PassWord, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {        //if succeeds, e is null
                        if (e!=null) {
                            Toast.makeText(LoginActivity.this,"Incorrect Username or Password",Toast.LENGTH_LONG).show();
                            Log.e("error", "issue with login");
                            return;
                        }

                        Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                        goMainActivity();
                    }
                });

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser user = new ParseUser();
                user.setUsername(etUserName.getText().toString());
                user.setPassword(etPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this,"Account Created",Toast.LENGTH_LONG).show();
                            goMainActivity();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Could not create account",Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });


    }

    private void goMainActivity() {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}