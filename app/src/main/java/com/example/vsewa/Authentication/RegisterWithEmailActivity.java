package com.example.vsewa.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsewa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    private String email,password,phoneNumber,userName, firstName, lastName, gender,age,city, pathToSelfie;
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
//    ArrayList<String> change=new ArrayList<>();
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private NoInternetDialog noInternetDialog;
    private ImageView imageView;
    private Uri imageUri, imageUriSelfie;
    private Bitmap imageBitmap, icardImageBitmap;

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
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
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
//        change.add("Select Type");

//        Button buttonNeedy = findViewById(R.id.btSignUpNeedy);
//        Button buttonVolunteer = findViewById(R.id.btSignUpVolunteer);
        TextView login = findViewById(R.id.btSignUpNeedy);
//        TextView buttonVolunteer = findViewById(R.id.btSignUpVolunteer);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NeedyIntent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailId.class);
                startActivity(NeedyIntent);
                finish();
            }
        });

//        buttonVolunteer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent volunteerIntent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailId.class);
//                startActivity(volunteerIntent);
//                finish();
//            }
//        });

        tpCity = findViewById(R.id.spinnerCity);
//        tpSwitchPrefrece = findViewById(R.id.spinnerSwitch);
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

//        databaseRef.child("Prefrences").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data:dataSnapshot.getChildren()){
//                    change.add((String) data.getValue());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                change.add("Data ref cancelled");
//            }
//        });
//        ArrayAdapter<String> SwitchPrefrecesAdapter = new ArrayAdapter<>(
//                RegisterWithEmailActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                change);
//        SwitchPrefrecesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        tpSwitchPrefrece.setAdapter(SwitchPrefrecesAdapter);
//
//        tpSwitchPrefrece.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                prefrences = change.get(i);
//                tpSwitchPrefrece.setSelection(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


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
                                            if(upload.getText().toString().equals("Uploaded")){
                                                if(checkBox.isChecked()){
                                                    progressDialog.setMessage("Progress Dialog Checked");
                                                    mAuth.createUserWithEmailAndPassword(email, password)
                                                            .addOnCompleteListener(RegisterWithEmailActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if(task.isSuccessful()){
                                                                        progressDialog.setMessage("Hello brother");
                                                                        user = mAuth.getCurrentUser();
                                                                        savedDetails();
                                                                    }else {
                                                                        Toast.makeText(RegisterWithEmailActivity.this, "Mail appears to be existing, please try with another mail", Toast.LENGTH_SHORT).show();
                                                                        tpEmail.setEnabled(true);
//                                                                            tpUserName.setEnabled(true);
                                                                        tpPhoneNumber.setEnabled(true);
                                                                        tpPassword.setEnabled(true);
                                                                        tpAge.setEnabled(true);
//                                                                            tpReferral.setEnabled(true);
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
                        if (ContextCompat.checkSelfPermission(RegisterWithEmailActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    takePictureButton.setEnabled(false);
                            ActivityCompat.requestPermissions(RegisterWithEmailActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                        }else{
                            dispatchPictureTakenAction();
                        }
            }
        });
    }

    private void savedDetails(){
//        progressDialog.setMessage("inside save details");
        try{

//            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
//            Bitmap bitmap = bitmapDrawable.getBitmap();
            final String currentTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] imageInByte = stream.toByteArray();
            final StorageReference Ref = mStorageRef.child("Users").child(user.getUid()).child(currentTimeStamp + "_selfie");
            Task<Uri> uriTask = Ref.putBytes(imageInByte).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return Ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                progressDialog.setMessage("Slefie Uploaded");
                                imageUriSelfie = task.getResult();
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                icardImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                final byte[] imageInByte = stream.toByteArray();
                                final StorageReference Ref1 = mStorageRef.child("Users").child(user.getUid()).child(currentTimeStamp + "_id");
//                                Ref1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        progressDialog.setMessage("icard uploaded");
//                                        imageUri = Ref1.getDownloadUrl();
//                                    }
//                                });
                                Task<Uri> uriTask1 = Ref1.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful()){
                                            throw task.getException();
                                        }
                                        return Ref1.getDownloadUrl();
                                    }
                                })
                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.setMessage("Icard Uploaded");
                                                    Log.d("icardpic", "error");
                                                    imageUri = task.getResult();
                                                    String device_token = FirebaseInstanceId.getInstance().getToken();
                                                    String time;
                                                    Calendar calendar = Calendar.getInstance();
                                                    if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                                                        time = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM";
                                                    } else {
                                                        time = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM";

                                                    }
                                                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                                    progressDialog.setMessage("inside save details..........");
                                                    Map<String, Object> userDetails = new HashMap<>();
                                                    userDetails.put("email", email);
                                                    userDetails.put("fullName", userName);
                                                    userDetails.put("phoneNumber", phoneNumber);
                                                    userDetails.put("Hostels", city);
                                                    userDetails.put("Age", age);
                                                    userDetails.put("gender", gender);
                                                    userDetails.put("noOfLogin", 0);
                                                    userDetails.put("friends", 0);
                                                    userDetails.put("rating", 0);
                                                    userDetails.put("comments", 0);
                                                    progressDialog.setMessage("TOken ke pass");
                                                    userDetails.put("device_token", Objects.requireNonNull(device_token));
                                                    userDetails.put("profile_image", "default");
                                                    userDetails.put("timeOfJoining", time);
                                                    userDetails.put("dateOfJoining", date);
                                                    userDetails.put("isLoggedIn", "no");
                                                    progressDialog.setMessage("Id proof ke pass");
                                                    userDetails.put("id_proof", imageUriSelfie.toString());
                                                    progressDialog.setMessage("selfie ke pass");
                                                    userDetails.put("selfie", imageUriSelfie.toString());
                                                    progressDialog.setMessage("Setting Database");
                                                    DatabaseReference usersRef = databaseRef.child("Users").child(user.getUid());
                                                    usersRef.setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            user.sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                progressDialog.setMessage("User invitation send");
                                                                                Log.d("email sent", "Email sent to the respective user");
//                                                                                Toast.makeText(RegisterWithEmailActivity.this, "Please Confirm your Email First", Toast.LENGTH_LONG).show();
                                                                                Intent intent = new Intent(RegisterWithEmailActivity.this, LoginWithEmailId.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(RegisterWithEmailActivity.this, "Error in uploading Icard", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(RegisterWithEmailActivity.this, "Error in Uploading selfie Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(RegisterWithEmailActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
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
            progressDialog.setTitle("Setting the stage 1st wala");
            progressDialog.setMessage("Let the show begin...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            imageUri = data.getData();
//            File path =
//            if (null != imageUri) {
//                Intent intent = CropImage.activity(imageUri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1,1)
//                        .getIntent(getApplicationContext());
//                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//            }
            Bundle extras = data.getExtras();
            if(null != imageUri){
                icardImageBitmap = (Bitmap) BitmapFactory.decodeFile(imageUri.toString());
                upload.setText("Uploaded");
                uploadClickable.setEnabled(false);
                progressDialog.dismiss();

            }else{
                uploadClickable.setEnabled(true);
                Toast.makeText(RegisterWithEmailActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        } else if(resultCode == RESULT_OK && requestCode == CAMERA_PICK){
            progressDialog.setTitle("Setting the stage");
//            imageUriSelfie = data.getData();
            progressDialog.setMessage("Uploading Selfie" + imageUriSelfie);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Bundle extras = data.getExtras();


//            Bitmap bitmap = BitmapFactory.decodeFile(pathToSelfie);
//            imageUriSelfie = data.getData();
            if(null != extras){
                imageBitmap = (Bitmap) extras.get("data");
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
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "new image");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
//        imageUriSelfie=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takepic.putExtra(MediaStore.EXTRA_OUTPUT, imageUriSelfie);


        if(takepic.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takepic, CAMERA_PICK);
//            File photofile = null;
//            try {
//
//                photofile = createPhotoFile();
//                imageUriSelfie=Uri.fromFile(photofile);
//                if(photofile != null){
//                    pathToSelfie = photofile.getAbsolutePath();
//                    Uri photoUri = FileProvider.getUriForFile(RegisterWithEmailActivity.this, "Asif", pathToSelfie);
//                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(takepic, CAMERA_PICK);
//                }
//
//            }catch (Exception e){
////                Log.d("myLogInsidePic", "Excep", e.toString());
//            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
