package com.example.vsewa.NavigationButton.ui.home;

import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseUser mUser;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //location check and update

    }

    public LiveData<String> getText() {
        return mText;
    }

    public FirebaseUser getUser(){
        return mUser;
    }


}