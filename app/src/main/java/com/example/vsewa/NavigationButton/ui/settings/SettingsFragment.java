package com.example.vsewa.NavigationButton.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
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

public class  SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private ListView listView;
    private ArrayList<String> listViewArray;
    private CircleImageView circleProfileView;
    private ImageView imageViewIdCard;
    private TextView tvname, tvhostel, tvcity;

    private FirebaseUser user;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog progressDialog;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.show();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Getting all details");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        user = mViewModel.getUser();
        tvname = root.findViewById(R.id.name);
        tvhostel = root.findViewById(R.id.designation);
        tvcity = root.findViewById(R.id.location);
        circleProfileView = root.findViewById(R.id.profile);
        imageViewIdCard = root.findViewById(R.id.imageviewIdCard);
        mViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvname.setText(s);
            }
        });
        mViewModel.getHostel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvhostel.setText(s);
            }
        });
        mViewModel.getIcardLink().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    Glide.with(SettingsFragment.this)
//                        .using(new FirebaseImageLoader())
                        .load(s)
                        .into(imageViewIdCard);
                progressDialog.dismiss();
                }catch (Exception e){
                    Log.d("tag", "Exception");
                    progressDialog.dismiss();
                }
            }
        });
        mViewModel.getProfilePicLink().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    Glide.with(SettingsFragment.this)
//                        .using(new FirebaseImageLoader())
                            .load(s)
                            .into(circleProfileView);
                    progressDialog.dismiss();
                }catch (Exception e){
                    Log.d("tag", "Exception");
                    progressDialog.dismiss();
                }
            }
        });
        listView = root.findViewById(R.id.listviewProfile);
        listViewArray = mViewModel.getListViewArray();
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
        return root;
    }
}
