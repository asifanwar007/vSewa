package com.example.vsewa.NavigationButton.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.vsewa.NavigationButton.ui.dashboard.DashboardFragment;
import com.example.vsewa.NeedyMap.NeedyMapsActivity;
import com.example.vsewa.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(28.544787, 17.192501);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    private HomeViewModel homeViewModel;
    private Switch volunteerSwitch;
    private Button volunteerRequiredButton;
    private TextView tvLocationVolunteer, tvLocationRequired;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference, databaseReference1, databaseReference2;
    private GeoFire geoFire;
    private FirebaseUser user;
    private String gender;


    private LocationManager locationManager;
    private LocationListener locationListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        user = homeViewModel.getUser();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Setting all details");
        progressDialog.setCancelable(false);
        getLocationPermission();
        volunteerSwitch = root.findViewById(R.id.switchVolunteer);
        tvLocationRequired = root.findViewById(R.id.tvRequiredLocation);
        tvLocationVolunteer = root.findViewById(R.id.tvVolunteeringLocation);

        volunteerSwitch.setTextOff("Off");
        volunteerSwitch.setTextOn("On");
        volunteerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                progressDialog.show();
                if(b){
                    if(mLocationPermissionGranted != true){
                        progressDialog.dismiss();
                        getLocationPermission();
                        if(mLocationPermissionGranted != true){
                            Toast.makeText(getContext(), "Volunteering Mode OFF", Toast.LENGTH_SHORT).show();
                            volunteerSwitch.setChecked(false);
                            tvLocationVolunteer.setText("Please Allow Location to Proceeds Further");
                            tvLocationVolunteer.setTextColor(Color.RED);
                            tvLocationVolunteer.setVisibility(View.VISIBLE);
                        }else{
                            progressDialog.show();
                            volunteerSwitch.setChecked(true);
                            tvLocationVolunteer.setText("Location Access Granted!!");
                            tvLocationVolunteer.setTextColor(Color.GREEN);
                            tvLocationVolunteer.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Volunteering Mode On", Toast.LENGTH_SHORT).show();
                            saveLocation();
                        }
                    }else {
                        progressDialog.show();
                        Toast.makeText(getContext(), "Volunteering Mode On", Toast.LENGTH_SHORT).show();
                        saveLocation();
                    }

                }else{
                    progressDialog.show();
                    Toast.makeText(getContext(), "Volunteering Mode OFF", Toast.LENGTH_SHORT).show();
                    deleteLocation();
                }
            }
        });

        volunteerRequiredButton = root.findViewById(R.id.btVolunteerReq);

        volunteerRequiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(mLocationPermissionGranted == true){
                    if(locationManager != null){
                        locationManager.removeUpdates(locationListener);
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("VolunteerOn").child(user.getUid());
                        databaseReference3.removeValue();
                    }

                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NeedyMapsActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                }else{
                    getLocationPermission();
                    tvLocationRequired.setText("Please Allow Location to Proceeds further");
                    tvLocationRequired.setTextColor(Color.RED);
                    tvLocationRequired.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

            }
        });


        return root;
    }

    private void deleteLocation() {
        locationManager.removeUpdates(locationListener);
        FirebaseDatabase.getInstance().getReference().child("VolunteerOn").child(user.getUid()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("GeoFire").child("VolunteerOn").child(user.getUid()).removeValue();
        progressDialog.dismiss();
    }



    private void saveLocation() {
        getDeviceLocation();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("VolunteerOn").child(user.getUid());
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("GeoFire").child("VolunteerOn");
        geoFire = new GeoFire(databaseReference2);

        locationListener = new android.location.LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        gender = (String) dataSnapshot.child("gender").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                databaseReference.child("latitude").setValue(latitude);
                databaseReference.child("longitude").setValue(longitude);
                databaseReference.child("gender").setValue(gender);
                databaseReference1.child("MyLocation").child("longitude").setValue(longitude);
                databaseReference1.child("MyLocation").child("latitude").setValue(latitude);
                geoFire.setLocation(user.getUid(), new GeoLocation(latitude, longitude),
                        new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                               Log.i("tag", "geofire updated");
                            }
                        });
                progressDialog.dismiss();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListener);
        }





    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
//                            if (mLastKnownLocation != null) {
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(mLastKnownLocation.getLatitude(),
//                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                            }
                        } else {
                            Log.d("hel", "Current location is null. Using defaults.");
                            Log.e("hel", "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            mLastKnownLocation = null;
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}