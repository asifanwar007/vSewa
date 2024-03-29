package com.example.vsewa.NavigationButton.ui.notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vsewa.NavigationButton.ui.dashboard.DataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<DataModel>> mDataset;
    private ArrayList<DataModel> ar, mListViewArray;
    private DatabaseReference databaseReference;
    private FirebaseUser mUser;
    public NotificationsViewModel() {
        mDataset = new MutableLiveData<>();
        ar = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("GeoFire").child("NeedyOn");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                for (final DataSnapshot childDataSnapshot : dataSnapshot1.getChildren()) {
//                    Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
//                    Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keynamem

//                    ar.add(childDataSnapshot.getKey()) ;
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(childDataSnapshot.getKey());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = (String) dataSnapshot.child("fullName").getValue();
                            String addr = (String) dataSnapshot.child("Hostels").getValue();
                            String age = (String) dataSnapshot.child("Age").getValue();
                            String imglink = (String) dataSnapshot.child("selfie").getValue();
                            String gender = (String) dataSnapshot.child("gender").getValue();
                            String uid = (String) childDataSnapshot.getKey();
                            DataModel dataModel = new DataModel(name, age, uid, addr, gender, imglink);
                            ar.add(dataModel);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



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

    public LiveData<ArrayList<DataModel>> getData(){
        return mDataset;

    }


}