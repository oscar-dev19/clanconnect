package com.nullpointertech.cLANConnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "Signup_Activity";

    ProgressDialog progressBar;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextUsername, editTextName,
    editTextPhone;
    Guild_User new_user;
    Map<String,Object> users;
    Button Signup;

    FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.signup_activity_email_editText);
        editTextPassword = findViewById(R.id.signup_activity_password_editText);
        editTextConfirmPassword = findViewById(R.id.signup_activity_confirm_password_editText);
        editTextUsername = findViewById(R.id.signup_activity_username_editText);
        editTextName = findViewById(R.id.signup_activity_name_editText);
        editTextPhone = findViewById(R.id.signup_activity_phone_editText);
        Signup = findViewById(R.id.signup_activity_signup_button);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        final String name, username, email, phone, password, confPassword;

        name = editTextName.getText().toString().trim();
        username = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        phone = editTextPhone.getText().toString();
        password = editTextPassword.getText().toString();
        confPassword = editTextConfirmPassword.getText().toString();

        if(name.isEmpty()){
            editTextName.setError("Please enter a name for users to find you");
            editTextName.requestFocus();
            return;
        }

        if(username.isEmpty()){
            editTextUsername.setError("Please enter a username");
            editTextUsername.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            editTextPhone.setError("Please enter a phone number");
            editTextPhone.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Please enter a valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required to complete sign up");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(confPassword)){
            editTextConfirmPassword.setError("Passwords do not match!");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(password.length() < 10){
            editTextPassword.setError("Minimum length of password should be at least 10 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar = ProgressDialog.show(SignupActivity.this, "",
                "Loading. Please wait...", true);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Add user to users database.
                    Guild_User new_user = new Guild_User(username,name,email,phone,"");
                    //users.put(username,new_user);

                    database.collection("users")
                            .document(mAuth.getCurrentUser().getUid())
                            .set(new_user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"User " + mAuth.getCurrentUser().getUid()
                                            + " successfully added." );
                                    startActivity(new Intent(SignupActivity.this, ControllerActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG,e.toString());
                                    Log.w(TAG,"Error adding user " +
                                            mAuth.getCurrentUser().getUid());
                                }
                            });
                }
                else{
                    Toast.makeText(getApplication(), task.getException().
                            getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Error creating and registering user with Firebase.");
                }
            }
        });
    }

    /*@Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            finish();
            //startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_activity_signup_button:
                registerUser();
                break;
        }
    }*/
}