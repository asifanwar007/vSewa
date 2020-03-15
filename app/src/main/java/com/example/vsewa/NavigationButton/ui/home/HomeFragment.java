package com.example.vsewa.NavigationButton.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vsewa.NavigationButton.ui.dashboard.DashboardFragment;
import com.example.vsewa.NeedyMap.NeedyMapsActivity;
import com.example.vsewa.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Switch volunteerSwitch;
    private Button volunteerRequiredButton;

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
        volunteerSwitch = root.findViewById(R.id.switchVolunteer);
        volunteerSwitch.setTextOff("Off");
        volunteerSwitch.setTextOn("On");
        volunteerRequiredButton = root.findViewById(R.id.btVolunteerReq);

        volunteerRequiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NeedyMapsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}