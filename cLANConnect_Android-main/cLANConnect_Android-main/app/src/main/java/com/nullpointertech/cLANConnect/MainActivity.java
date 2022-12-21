package com.nullpointertech.cLANConnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

/**
 * MainActivity.java
 * Author: Cameron Hozouri
 * check if the user has a firebase Token
 * if not then they need to sign in or create an account
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //Checks if the user is null
        if(mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(this, ControllerActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }




    }
}
