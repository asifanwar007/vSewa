package com.example.vsewa.NavigationButton.ui.home;

import android.widget.Switch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Switch volunteerSwitch;
    private FirebaseUser mUser;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public FirebaseUser getUser(){
        return mUser;
    }


}