package com.example.vsewa.NavigationButton.ui.settings;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vsewa.Authentication.LoginWithEmailId;
import com.example.vsewa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private ListView listView;
    private ArrayList<String> listViewArray;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String name, city, hostel,profilePicLink;
    private Uri profilePic;

    private CircleImageView circleProfileView;
    private TextView tvname, tvhostel, tvcity;

    private NoInternetDialog noInternetDialog;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.show();
        mViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
//        mViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        listView = root.findViewById(R.id.listviewProfile);
        listViewArray = new ArrayList<>();
        listViewArray.add("Profile");
        listViewArray.add("Account");
        listViewArray.add("Sign Out");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listViewArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listViewArray.get(i).equals("Sign Out")){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginWithEmailId.class);
                    startActivity(intent);

                } else if (listViewArray.get(i).equals("Profile")){
                    Toast.makeText(getContext(), "Haven't set yet : " + listViewArray.get(i), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Haven't set yet : " + listViewArray.get(i), Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvname = root.findViewById(R.id.name);
        tvhostel = root.findViewById(R.id.designation);
        tvcity = root.findViewById(R.id.location);
        circleProfileView = root.findViewById(R.id.profile);

        storageReference = FirebaseStorage.getInstance().getReference().child("Image").child("Users").child(user.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = ((String) dataSnapshot.child("fullName").getValue()).toUpperCase();
                hostel = (String) dataSnapshot.child("Hostels").getValue();
                profilePic = Uri.parse((String) dataSnapshot.child("selfie").getValue());
                profilePicLink = (String) dataSnapshot.child("selfie").getValue();
                tvname.setText(name);
                tvhostel.setText(hostel);
//                circleProfileView.setImageURI(profilePic);
                Glide.with(getContext())
//                        .using(new FirebaseImageLoader())
                        .load(profilePicLink)
                        .into(circleProfileView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("log1", "Unable to retrieve name");

            }
        });
        Log.d("log2", name +" " + hostel);






        return root;
    }



}
