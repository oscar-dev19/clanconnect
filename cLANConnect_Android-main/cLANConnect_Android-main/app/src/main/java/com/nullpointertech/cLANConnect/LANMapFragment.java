package com.nullpointertech.cLANConnect;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

;


/**
 * LanMapFragment
 * Author: Cameron Hozouri
 * Description: is the fragment that creates the map and displays
 * all events going on in their location. Also gets Geo Location Permissions
 */
public class LANMapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private GoogleMap map;
    private View view;
    private FloatingActionButton mLocationButton;
    private FloatingActionButton mAddPartyButton;
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lanmap,
                container, false);
        db = FirebaseFirestore.getInstance();
        //checkLocationPermission();
        initGmap();

        mLocationButton = v.findViewById(R.id.GPSButt);
        mAddPartyButton = v.findViewById(R.id.LanFab);

        searchView = (SearchView)v.findViewById(R.id.searchbarLan);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RebaseCameraToLocation();
            }
        });

        mAddPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                AddEvents(query);
                return false;
            }
        });

        return v;
    }
    public void initGmap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        // check if map is created successfully or not


    }

    /**
     * rebases camera of the map to the users
     * Geo location
     */
    public void RebaseCameraToLocation()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            locationManager.requestLocationUpdates(bestProvider, 20, 0, this);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
            //map.setMyLocationEnabled(true);
            //initGamp();
        }
        else
        {
            checkLocationPermission();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        //checkLocationPermission();
        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            //map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            locationManager.requestLocationUpdates(bestProvider, 20, 0, this);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
            AddEvents(null);
        }
        else {
            checkLocationPermission();
        }
    }

    /**
     * Gets called when map is fully setup
     * calls for the firestore database
     * to give all items in the events Collection
     * also Checks if the user has queried info from the search bar
     * @param query
     */
    public void AddEvents(String query)
    {
        if(map != null)
        {
            map.setInfoWindowAdapter(new CustomPartyWindowAdapter(getContext()));
            if(query == null)
            {
                CollectionReference eventsref = db.collection("events");
                eventsref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                LanParty lanParty = documentSnapshot.toObject(LanParty.class);
                                if(!lanParty.isPrivate())
                                {
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng(lanParty.getLat(), lanParty.getLong());
                                    markerOptions.position(latLng);
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_re));
                                    markerOptions.title(lanParty.getPartyName());
                                    StringBuilder str = new StringBuilder();
                                    str.append(lanParty.getPlatform());
                                    str.append("//");
                                    str.append(String.join(",", lanParty.getGames()));
                                    markerOptions.snippet(str.toString());
                                    map.addMarker(markerOptions);
                                }
                            }
                        }
                    }
                });
            }
            else
            {
                CollectionReference eventsref = db.collection("events");
                String[] search = query.split("\\s+");
                Query eventQuery;

                for(String word: search)
                {
                    eventsref.whereArrayContains("partyTags", word);
                    eventsref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                {
                                    LanParty lanParty = documentSnapshot.toObject(LanParty.class);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng(lanParty.getLat(), lanParty.getLong());
                                    markerOptions.position(latLng);
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_re));
                                    markerOptions.title(lanParty.getPartyName());
                                    StringBuilder str = new StringBuilder();
                                    str.append(lanParty.getPlatform());
                                    str.append("//");
                                    str.append(String.join(",", lanParty.getGames()));
                                    markerOptions.snippet(str.toString());
                                    map.addMarker(markerOptions);
                                }
                            }
                        }
                    });
                }


            }

        }
    }
    /**
     * checkLocationPermission()
     * checks the users settings to make sure they do or do not have
     * location enabled for the app
     * if not request by sending a permission request to the
     * Controller Activity
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
                //initGamp();
            }
        }
    }

    /**
     * Resulot of permission code requested by user
     * uses the Controller Activity to ask the permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        //map.setMyLocationEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String bestProvider = locationManager.getBestProvider(criteria, true);
                        Location location = locationManager.getLastKnownLocation(bestProvider);
                        locationManager.requestLocationUpdates(bestProvider, 20, 0, this);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
                        AddEvents(null);
                        //initGamp();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
