package com.example.vsewa.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.example.vsewa.Authentication.LoginWithEmailIdVolunteer;
import com.example.vsewa.NavigationButton.BottomNavigatioActivity;
import com.example.vsewa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class volunteerRequiredDialogs extends AppCompatDialogFragment {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Button cancel, request;
    private String[] noOfVolunteers = {"1","2","3","4","5","6"};
    private Spinner spinnerVolunteers;
    private ArrayAdapter<String> noOfVolunteerAdapter;
    private EditText reasonEditText;
    private String reason, gender;
    private int numberVolunteer = 1;
    private RadioGroup genderGroup;
    private RadioButton male, female, both;
    private volunteerRequiredDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(getContext(), LoginWithEmailIdVolunteer.class);
            startActivity(intent);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.volunteer_required_details, null);
        builder.setView(view);
        cancel = view.findViewById(R.id.btCancel);
        request = view.findViewById(R.id.btRequest);
        reasonEditText = view.findViewById(R.id.etVolunteerRequire);

        spinnerVolunteers = view.findViewById(R.id.spinnerVolunteerRequired);
        noOfVolunteerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, noOfVolunteers);
        noOfVolunteerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVolunteers.setAdapter(noOfVolunteerAdapter);

        spinnerVolunteers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberVolunteer = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        genderGroup=view.findViewById(R.id.radioGroupGender);
        genderGroup.check(R.id.radioButtonBoth);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BottomNavigatioActivity.class);
                startActivity(intent);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = reasonEditText.getText().toString();
                if(genderGroup.getCheckedRadioButtonId()==R.id.radioButtonFemale){
                    gender = "Female";
                }else if(genderGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
                    gender = "Male";
                }else{
                    gender = "Both";
                }
                if(reason == null || reason.equals("")){
                    reasonEditText.setError("Please Enter the reason");
                } else {
                    listener.applyAllValues(gender, reason, numberVolunteer);
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (volunteerRequiredDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ "Must Implement Volunteer Required Dialog");
        }
    }

    public interface volunteerRequiredDialogListener {
        void applyAllValues(String gender, String reason, int numberofperson);
    }

}
