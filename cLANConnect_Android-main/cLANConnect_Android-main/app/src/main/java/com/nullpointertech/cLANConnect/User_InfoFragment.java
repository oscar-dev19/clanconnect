package com.nullpointertech.cLANConnect;

/**
 * @author Minghe Yang
 * Date Last Modified: Sunday May 13, 2019.
 * Modification: Converted the activity into fragment.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.core.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_InfoFragment extends Fragment {
    FirebaseFirestore db;
    EditText username,name,email,phone,description;
    Button quit,save;
    String uid ;
    FirebaseAuth mAuth;

    public User_InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user__info, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //init component
        username = v.findViewById(R.id.userinfo_activity_username_editText);
        name = v.findViewById(R.id.userinfo_activity_name_editText);
        email = v.findViewById(R.id.userinfo_activity_email_editText);
        phone = v.findViewById(R.id.userinfo_activity_phone_editText);
        description = v.findViewById(R.id.userinfo_activity_description_editText);

        quit = (Button)v.findViewById(R.id.userinfo_activity_quit_button);
        save = (Button)v.findViewById(R.id.userinfo_activity_save_button);

        uid = mAuth.getCurrentUser().getUid();

        if(uid != null)
        {
            updateView();
        }


        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                name.setText("");
                email.setText("");
                phone.setText("");
                description.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsers();
            }
        });

        return v;
    }

    public void updateView(){
        DocumentReference user = db.collection("users").document(uid);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    name.setText(doc.get("name").toString());
                    username.setText(doc.get("username").toString());
                    email.setText(doc.get("email").toString());
                    description.setText(doc.get("description").toString());
                    phone.setText(doc.get("phone").toString());
                }
            }
        });
    }

    public void updateUsers(){
        Guild_User new_user = new Guild_User(username.getText().toString(),
                name.getText().toString(),
                email.getText().toString(),
                phone.getText().toString(),
                description.getText().toString()
        );
        if (uid == null || uid.equals("")){
            uid = "account";
        }
        System.out.println("set uid:" + uid);
        db.collection("users")
                .document(uid)
                .update("name",name.getText().toString(),"username",username.getText().toString(),"email",email.getText().toString(),
                        "phone",phone.getText().toString(),"description",description.getText().toString());
    }
}
