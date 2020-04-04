package com.example.vsewa.NavigationButton.ui.settings;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

public class SettingsViewModel extends ViewModel {
    private MutableLiveData<String> mTextName, mTextCity, mTextHostel, mTextProfilePicLink,mTextIcardLink;
    private ArrayList<String> mListViewArray;
    private DatabaseReference databaseReference;
    private FirebaseUser mUser;
    private String mName, mCity, mHostel, mProfilePicLink, mIcardLink;


    public SettingsViewModel() {
        mTextName = new MutableLiveData<>();
        mTextHostel = new MutableLiveData<>();
        mTextCity = new MutableLiveData<>();
        mTextProfilePicLink = new MutableLiveData<>();
        mTextIcardLink = new MutableLiveData<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mName = ((String) dataSnapshot.child("fullName").getValue()).toUpperCase();
                mHostel = ((String) dataSnapshot.child("Hostels").getValue()).toUpperCase();
                mProfilePicLink = ((String) dataSnapshot.child("selfie").getValue());
                mIcardLink = (String) dataSnapshot.child("id_proof").getValue();
                mTextName.setValue(mName);
                mTextHostel.setValue(mHostel);
                mTextProfilePicLink.setValue(mProfilePicLink);
                mTextIcardLink.setValue(mIcardLink);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SettingsVieweModel", "Firebase won't be able to read the data");
            }
        });
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public ArrayList<String> getListViewArray(){
        mListViewArray = new ArrayList<String>();
        mListViewArray.add("Account Information");
        mListViewArray.add("Volunteer History");
        mListViewArray.add("Privacy Policy");
        mListViewArray.add("Sign Out");
        mListViewArray.add("Help");
        return  mListViewArray;
    }

    public LiveData<String> getName(){
        return mTextName;
    }
    public LiveData<String> getHostel(){
        return mTextHostel;
    }
    public LiveData<String> getProfilePicLink(){
        return mTextProfilePicLink;
    }
    public LiveData<String> getIcardLink(){
        return mTextIcardLink;
    }


}
