package com.example.vsewa.NavigationButton.ui.dashboard;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> mDataset;
    private ArrayList<String> ar, mListViewArray;
    private DatabaseReference databaseReference;
    private FirebaseUser mUser;


    public DashboardViewModel() {
        mDataset = new MutableLiveData<>();
        ar = new ArrayList<String>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("VolunteerOn");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
//                List<Object> ar = td.values();
//                ar = new ArrayList<String>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
//                    Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keynamem
                    ar.add(childDataSnapshot.getKey()) ;
                }
                mDataset.postValue(ar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public LiveData<ArrayList<String>> getData(){
        return mDataset;

    }
    public ArrayList<String> getData1(){
        return ar;
    }

}