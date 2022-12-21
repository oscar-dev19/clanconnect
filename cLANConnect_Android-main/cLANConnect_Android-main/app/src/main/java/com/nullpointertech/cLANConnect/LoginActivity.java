package com.nullpointertech.cLANConnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity{

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    ProgressBar progressBar;
    EditText emailEditText, passwordEditText;
    Button loginBt, signBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.login_activity_email_editText);
        passwordEditText = findViewById(R.id.login_activity_password_editText);

        loginBt = findViewById(R.id.login_activity_login_button);
        signBt = findViewById(R.id.login_activity_signup_button);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });


    }


    public void loginUser(){
        final String email, password;

        email = emailEditText.toString().trim();
        password = passwordEditText.toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Please enter an email");
            return;
        }

        /*if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please enter a valid Email");
            emailEditText.requestFocus();
            return;
        }*/

        if(password.isEmpty()){
            passwordEditText.setError("Please enter a password");
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, ControllerActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error: Login is Incorrect. Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   /* @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            finish();
            //startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
        }
    }*/


    /*@Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.login_activity_login_button:
                loginUser();
                break;
            case R.id.login_activity_signup_button:

                break;
        }
    }*/
}
