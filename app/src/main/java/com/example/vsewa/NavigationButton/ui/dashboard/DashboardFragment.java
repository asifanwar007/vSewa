package com.example.vsewa.NavigationButton.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vsewa.Dialogs.volunteerRequiredDialogs;
import com.example.vsewa.R;

public class DashboardFragment extends Fragment implements volunteerRequiredDialogs.volunteerRequiredDialogListener {

    private DashboardViewModel dashboardViewModel;
    private String volunteerGender, reasonRequired;
    private int noOfVolunteerRequired;
    private TextView tvGender, tvReason, tvNumberVolunteer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        tvGender = root.findViewById(R.id.tvGender);
        tvReason = root.findViewById(R.id.tvShowReason);
        tvNumberVolunteer = root.findViewById(R.id.tvNumberOfVolunteer);

        tvGender.setText(volunteerGender);
        tvReason.setText(reasonRequired);
        tvNumberVolunteer.setText(noOfVolunteerRequired + "");
        return root;
    }

    @Override
    public void applyAllValues(String gender, String reason, int numberofperson) {
        volunteerGender = gender;
        reasonRequired = reason;
        noOfVolunteerRequired = numberofperson;
    }
}