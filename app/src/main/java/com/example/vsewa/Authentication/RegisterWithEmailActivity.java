package com.example.vsewa.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vsewa.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.regex.Matcher;

import am.appwise.components.ni.NoInternetDialog;

public class RegisterWithEmailActivity extends AppCompatActivity {
    private TextInputLayout tpPassword,tpUserName,tpReferral;
    private EditText tpAge, tpPhoneNumber, tpEmail, tpFirstName, tpLastName;
    private String email,password,phoneNumber,userName,gender,age,referralCode,city;

    private FirebaseAuth mAuth;
    private DatePickerDialog picker;
    private Matcher matcher;
    private RadioGroup radioGroup;
    private TextView login,upload, referralUid;
    private StorageReference mStorageRef;
    private Button buttonSignUp;
    private String referCode;
    private DatabaseReference databaseRef;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%!'\"~`^&*.,:{}()<>]).{6,20})";
    private static final String USERNAME_PATTERN = "((?=.*[a-z]).{6,20})";
    private final int GALLERY_PICK = 1;
    private Spinner tpCity;
    ArrayList<String> cities=new ArrayList<>();
    ArrayList<String> change=new ArrayList<>();
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private String referral_uid;
    private NoInternetDialog noInternetDialog;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_email);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.show();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait a while we arrange everything for you...");
        progressDialog.setTitle("Registering your details");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        databaseRef= FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        tpEmail=findViewById(R.id.etUsername);
        tpFirstName= findViewById(R.id.etFirstName);
        tpLastName = findViewById(R.id.etLastName);
        tpPhoneNumber=findViewById(R.id.etPhoneNumber);
//        tpPassword=findViewById(R.id.etPassword);
        tpAge=findViewById(R.id.etAge);


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait a while we arrange everything for you...");
        progressDialog.setTitle("Registering your details");
        progressDialog.setCancelable(false);

        radioGroup=findViewById(R.id.rgGender);

        login=findViewById(R.id.login);

        buttonSignUp=findViewById(R.id.btSignUp);
        cities.add("Select City");
        change.add("Select Type");
    }
}
