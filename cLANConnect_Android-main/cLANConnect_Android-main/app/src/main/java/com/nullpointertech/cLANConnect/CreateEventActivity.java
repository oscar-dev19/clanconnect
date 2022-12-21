package com.nullpointertech.cLANConnect;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * CreateEventActivity.java
 * Author: Cameron Hozouri
 * Create event gets shown when they want to create a party
 * They input the fields shown in the xml
 * and then gets the Geo location of the address they inputted in
 * add the LAnParty object to the events collection in Firestore Database
 */
public class CreateEventActivity extends AppCompatActivity {

    private EditText where, name, numberofPeople, descrText, gamesText;
    private RadioGroup platform;
    private RadioButton PC, Both, Console;
    private Button cancelBT, createBT;
    private Switch isPrivate;
    private boolean priv = false;
    private FirebaseAuth fauth;
    private ProgressDialog dialog;

    private String selectedPlat;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();

        /**
         * getting all the inputs and binding them
         */
        where = (EditText)findViewById(R.id.whereText);
        descrText = (EditText)findViewById(R.id.descrText);
        name = (EditText)findViewById(R.id.nameText);
        numberofPeople = (EditText)findViewById(R.id.numText);
        gamesText = (EditText)findViewById(R.id.gamesText);

        platform = (RadioGroup) findViewById(R.id.radGroup);
        PC = (RadioButton) findViewById(R.id.PCrad);
        Both = (RadioButton) findViewById(R.id.bothRad);
        Console = (RadioButton) findViewById(R.id.consRad);

        isPrivate = (Switch) findViewById(R.id.privateSwitch);

        cancelBT = (Button) findViewById(R.id.createCanc);
        createBT = (Button) findViewById(R.id.createMake);

        isPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    priv = isChecked;
                else
                    priv =false;
            }
        });

        createBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(CreateEventActivity.this, "",
                        "Loading. Please wait...", true);
                String mWhere = where.getText().toString();
                String mName = name.getText().toString();
                String mNumber = numberofPeople.getText().toString();
                String decription = descrText.getText().toString();

                if(TextUtils.isEmpty(mWhere))
                {
                    where.setError("You need an address for the party, If the party is private it wont show up");
                    return;
                }
                if(TextUtils.isEmpty(mName))
                {
                    name.setError("You need a name for the party");
                    return;
                }
                if(TextUtils.isEmpty(mNumber))
                {
                    numberofPeople.setError("We need to know how many people");
                    return;
                }

                checkButton(CreateEventActivity.this.findViewById(android.R.id.content));

                List<Address> location = ConvertAddress(mWhere);

                if(location.size() == 0)
                {
                    where.setError("Please make sure you have your address city and zip code");
                    return;
                }
                else
                {
                    Random rand = new Random();


                    /**
                     * Creation of searchable tags
                     * this is for the user when they want to look up any
                     * certain types of parties
                     */
                    String description = descrText.getText().toString();
                    String[] words = description.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

                    ArrayList<String> tags = new ArrayList<String>(Arrays.asList(words));
                    String[] nonW = {"the", "as", "but", "with", "house", "a", "of", "to", "from", "on", "in", "up", "down", "search"};
                    for(String word: nonW)
                    {
                        tags.remove(word);
                    }
                    tags.add(selectedPlat.trim());

                    String games = gamesText.getText().toString();
                    String[] gameArray = games.toLowerCase().split(", ");
                    ArrayList<String> gameList = new ArrayList<String>(Arrays.asList(gameArray));
                    tags.addAll(gameList);

                    LanParty lnParty = new LanParty(mName, Integer.toString(rand.nextInt(100000)), "Cameron Hozouri", Integer.parseInt(mNumber), mWhere, priv, location.get(0).getLatitude(), location.get(0).getLongitude(), null, gameList, decription, null, selectedPlat, tags);
                    db.collection("events").add(lnParty).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dialog.dismiss();
                            Toast.makeText(CreateEventActivity.this, "Added new party", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(CreateEventActivity.this, "failed adding new party", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }





            }
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void checkButton(View view)
    {
        int id = platform.getCheckedRadioButtonId();
        RadioButton radioButton;
        if(id == -1)
        {
            Both.setError("Need to pick a platform");
            return;
        }
        radioButton = (RadioButton) view.findViewById(id);

        selectedPlat = radioButton.getText().toString();


    }

    /**
     * ConvertAddress()
     * converts a string address
     * to a Location object which holds
     * the Lat and Long coordinates
     * @param address
     * @return
     */
    public List<Address> ConvertAddress(String address)
    {
        List<Address> loc = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this);
        try {
            loc = geocoder.getFromLocationName(address, 1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return loc;

    }



}
