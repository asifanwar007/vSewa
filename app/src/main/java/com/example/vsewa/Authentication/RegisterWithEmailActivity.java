package com.example.vsewa.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsewa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;

public class RegisterWithEmailActivity extends AppCompatActivity {
    private TextInputLayout tpUserName,tpReferral;
    private EditText tpAge, tpPhoneNumber, tpEmail, tpFirstName, tpLastName, tpPassword;
    private String email,password,phoneNumber,userName, firstName, lastName, gender,age,city, prefrences, pathToSelfie;
    private ImageButton icard, selfie;
    private TextView login,upload, selfieText, uploadClickable;


    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatePickerDialog picker;
    private FirebaseUser user;

    private Matcher matcher;
    private RadioGroup radioGroup;
    private RadioButton male, female, other;
    private DatabaseReference databaseRef;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%!'\"~`^&*.,:{}()<>]).{6,20})";
    private static final String NAME_PATTERN = "((?=.*[a-z]).{3,20})";
    private final int GALLERY_PICK = 1, CAMERA_PICK=2;
    private Spinner tpCity, tpSwitchPrefrece;
    ArrayList<String> cities=new ArrayList<>();
    ArrayList<String> change=new ArrayList<>();
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
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
        tpPassword= findViewById(R.id.etPassword);
        tpAge=findViewById(R.id.etAge);
        checkBox = findViewById(R.id.checkBoxUserAgreement);
        radioGroup = findViewById(R.id.rgGender);
        female = findViewById(R.id.rbFemale);
        female.setChecked(true);

        icard = findViewById(R.id.ibIdProofImage);
        selfie = findViewById(R.id.ibCaptureImage);
        upload = findViewById(R.id.tvIdProof);
        uploadClickable = findViewById(R.id.tvUploadIdProof);
        selfieText = findViewById(R.id.tvSelfie);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait a while we arrange everything for you...");
        progressDialog.setTitle("Registering your details");
        progressDialog.setCancelable(false);

        radioGroup=findViewById(R.id.rgGender);

        login=findViewById(R.id.login);

        Button buttonSignUp=findViewById(R.id.btSignUp);
        cities.add("Select Hostels");
        change.add("Select Type");

        Button buttonNeedy = findViewById(R.id.btSignUpNeedy);
        Button buttonVolunteer = findViewById(R.id.btSignUpVolunteer);

        buttonNeedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NeedyIntent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailIdNeedy.class);
                startActivity(NeedyIntent);
                finish();
            }
        });

        buttonVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volunteerIntent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailIdVolunteer.class);
                startActivity(volunteerIntent);
                finish();
            }
        });

        tpCity = findViewById(R.id.spinnerCity);
        tpSwitchPrefrece = findViewById(R.id.spinnerSwitch);
        databaseRef.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    cities.add((String) data.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cities.add("Data pull Cancelled");
            }
        });

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(RegisterWithEmailActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tpCity.setAdapter(cityAdapter);

        tpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = cities.get(i);
                tpCity.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        databaseRef.child("Prefrences").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    change.add((String) data.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                change.add("Data ref cancelled");
            }
        });
        ArrayAdapter<String> SwitchPrefrecesAdapter = new ArrayAdapter<>(
                RegisterWithEmailActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                change);
        SwitchPrefrecesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tpSwitchPrefrece.setAdapter(SwitchPrefrecesAdapter);

        tpSwitchPrefrece.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefrences = change.get(i);
                tpSwitchPrefrece.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = tpEmail.getEditableText().toString();
                password = tpPassword.getEditableText().toString();
                phoneNumber = tpPhoneNumber.getEditableText().toString();
                age = tpAge.getEditableText().toString();
                firstName = tpFirstName.getEditableText().toString();
                lastName = tpLastName.getEditableText().toString();
                userName = firstName + " " + lastName;

                if (firstName.equals("")) {
                    progressDialog.dismiss();
//                    tpFirstName.setErrorEnabled(true);
                    tpFirstName.setError("Name should not be null");
                } else if (!validateName(firstName)) {
                    progressDialog.dismiss();
//                    tpUserName.setErrorEnabled(true);
                    tpFirstName.setError("Name must be from 3-20 characters long)");
                }else{
                    progressDialog.show();
                    if (lastName.equals("")) {
                        progressDialog.dismiss();
//                    tpFirstName.setErrorEnabled(true);
                        tpLastName.setError("Username should not be null");
                    } else if (!validateName(lastName)) {
                        progressDialog.dismiss();
//                    tpUserName.setErrorEnabled(true);
                        tpLastName.setError("Username must be from 6-20 characters long)");
                    }else {
                        progressDialog.show();

                        if (email.equals("") || !validateEmail(email)) {
                            progressDialog.dismiss();
                            //                    tpEmail.setErrorEnabled(true);
                            tpEmail.setError("Please enter an email");
                        } else {
                            //                    tpEmail.setErrorEnabled(false);
                            progressDialog.show();
                            if (password.equals("")) {
                                progressDialog.dismiss();
                                tpPassword.setError("Please set a password");
                            } else if (!validatePassword(password)) {
                                progressDialog.dismiss();
                                tpPassword.setError("Must contain lower case letters,a digit,a special symbol(minimum 6-20)");
                            } else {
                                //                    tpPassword.setErrorEnabled(false);
                                progressDialog.show();
                                if (phoneNumber.length() < 10) {
                                    progressDialog.dismiss();
                                    //                                tpPhoneNumber.setErrorEnabled(true);
                                    tpPhoneNumber.setError("Please enter 10 digit valid phone number");
                                } else if (phoneNumber.contains("+91")) {
                                    progressDialog.dismiss();
                                    //                                tpPhoneNumber.setErrorEnabled(true);
                                    tpPhoneNumber.setError("Enter without country code(+91)");
                                } else if (!(validatePhone(phoneNumber))) {
                                    progressDialog.dismiss();
                                    //                                tpPhoneNumber.setErrorEnabled(true);
                                    tpPhoneNumber.setError("Please enter 10 digit valid phone number");
                                } else {
                                    progressDialog.show();
//                                tpPhoneNumber.setErrorEnabled(false);

//                                        progressDialog.show();
                                    if(age == null || age.equals("")){
                                        progressDialog.dismiss();
                                        tpAge.setError("Please enter Age");
                                    }else if (Integer.parseInt(age) < 5 || Integer.parseInt(age) > 200) {
                                        progressDialog.dismiss();
                                        tpAge.setError("Please enter valid Age");
                                    } else {
                                        progressDialog.show();
                                        //tpDOB.setErrorEnabled(false);
                                        if (city == null || city.equals("Select Hostels")) {
                                            Toast.makeText(RegisterWithEmailActivity.this, "Select Hostels", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            progressDialog.show();
                                            if (prefrences == null || prefrences.equals("Select Type")) {
                                                Toast.makeText(RegisterWithEmailActivity.this, "Select Prefrence", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                if(upload.getText().toString().equals("Uploaded")){
                                                    if(checkBox.isChecked()){
                                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                                .addOnCompleteListener(RegisterWithEmailActivity.this, new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if(task.isSuccessful()){
                                                                            user = mAuth.getCurrentUser();
                                                                            savedDetails();
                                                                        }else {
                                                                            Toast.makeText(RegisterWithEmailActivity.this, "Mail appears to be existing, please try with another mail", Toast.LENGTH_SHORT).show();
                                                                            tpEmail.setEnabled(true);
                                                                            tpUserName.setEnabled(true);
                                                                            tpPhoneNumber.setEnabled(true);
                                                                            tpPassword.setEnabled(true);
                                                                            tpAge.setEnabled(true);
                                                                            tpReferral.setEnabled(true);
                                                                            tpCity.setEnabled(true);
                                                                            checkBox.setEnabled(true);
                                                                            radioGroup.setEnabled(true);
                                                                            progressDialog.dismiss();


                                                                        }
                                                                    }
                                                                });
                                                    }else{
                                                        Toast.makeText(RegisterWithEmailActivity.this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    Toast.makeText(RegisterWithEmailActivity.this, "Please upload Id Proof", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbMale) {
                    gender = "Male";

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbFemale) {
                    gender = "Female";
                } else {
                    gender = "Others";
                }









            }
        });
        icard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Image"), GALLERY_PICK);
            }
        });


        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        if (ContextCompat.checkSelfPermission(RegisterWithEmailActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////                    takePictureButton.setEnabled(false);
//                            ActivityCompat.requestPermissions(RegisterWithEmailActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                        }else{
                            dispatchPictureTakenAction();
//                        }
            }
        });
    }

    private void savedDetails(){
        try{
            final StorageReference filepath = mStorageRef.child("Image").child(user.getUid()).child("Selfie");
//            databaseRef =
//            Task<Uri> uriTask
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] imageInByte = stream.toByteArray();
            Task<Uri> uriTask = filepath.putBytes(imageInByte).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return  filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        String device_token = FirebaseInstanceId.getInstance().getToken();
                        String time;
                        Calendar calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                            time = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM";
                        } else {
                            time = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM";

                        }
                        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                        Uri image_uri = task.getResult();
                        Map<String, Object> userDetails = new HashMap<>();
                        userDetails.put("email", email);
                        userDetails.put("fullName", userName);
                        userDetails.put("phoneNumber", phoneNumber);
                        userDetails.put("city", city);
                        userDetails.put("Age", age);
                        userDetails.put("gender", gender);
                        userDetails.put("noOfLogin", 0);
                        userDetails.put("friends", 0);
//                        userDetails.put("noOfLifts", 0);
                        userDetails.put("rating", 0);
                        userDetails.put("comments", 0);
                        userDetails.put("device_token", Objects.requireNonNull(device_token));
                        userDetails.put("profile_image", "default");
                        userDetails.put("timeOfJoining", time);
                        userDetails.put("dateOfJoining", date);
//                        userDetails.put("referralPoints", 0);
//                        userDetails.put("usedReferralPoints", 0);
                        userDetails.put("isLoggedIn", "no");
                        userDetails.put("id_proof", image_uri.toString());
                        userDetails.put("selfie", image_uri.toString());
//                        databaseRef.child("Referral_codes").child(referCode).setValue(user.getUid().toString());
//                        databaseRef.child("Users").child(prefrences).child(user.getUid()).child("wallet_Balance").setValue(0);
//                        referral_uid = (String) referralUid.getText();
                        DatabaseReference usersRef = databaseRef.child("Users").child(prefrences).child(user.getUid());
                        usersRef.setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Intent intent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailIdVolunteer.class);
                                                    startActivity(intent);
                                                    finish();
                                                    progressDialog.dismiss();


                                                }
                                            }
                                        });
                            }
                        });

                    }
                }
            });

        }catch (Exception e){
            Log.d("log", "Excep inside savedDetails");
            e.printStackTrace();
        }
    }



    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean validateName(String userName) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("^[6-9][0-9]{9}$");
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            progressDialog.setTitle("Setting the stage");
            progressDialog.setMessage("Let the show begin...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Uri imageUri = data.getData();
//            if (null != imageUri) {
//                Intent intent = CropImage.activity(imageUri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1,1)
//                        .getIntent(getApplicationContext());
//                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//            }
            if(null != imageUri){
                upload.setText("Uploaded");
                uploadClickable.setEnabled(false);
                progressDialog.dismiss();

            }else{
                uploadClickable.setEnabled(true);
                Toast.makeText(RegisterWithEmailActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }

        if(resultCode == RESULT_OK && requestCode == CAMERA_PICK){
            progressDialog.setTitle("Setting the stage");
            progressDialog.setMessage("Let the show begin.......");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Uri imageUri = data.getData();
            if(null != imageUri){
                selfieText.setText("Taken");
                progressDialog.dismiss();
            }else {
//                uploadClickable.setEnabled(true);
                Toast.makeText(RegisterWithEmailActivity.this, "Selfie not Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        }



//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                Uri resultUri = result.getUri();
//                File thumb_path=new File(resultUri.getPath());
//                Picasso.get().load(resultUri).into(imageView);
//                upload.setText("ID Proof uploaded");
//                progressDialog.dismiss();
//            }
//        }
    }

    private  void dispatchPictureTakenAction(){
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepic.resolveActivity(getPackageManager()) != null){
            File photofile = null;
            try {
                photofile = createPhotoFile();
                if(photofile != null){
                    pathToSelfie = photofile.getAbsolutePath();
                    startActivityForResult(takepic, CAMERA_PICK);
                }

            }catch (Exception e){
//                Log.d("myLogInsidePic", "Excep", e.toString());
            }
        }
    }

    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (Exception e){
            Log.d("myLog", "excep" + e.toString());
        }
        return image;

    }
}
