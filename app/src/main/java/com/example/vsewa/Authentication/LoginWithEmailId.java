package com.example.vsewa.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vsewa.MainActivity;
import com.example.vsewa.NavigationButton.BottomNavigatioActivity;
import com.example.vsewa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;

public class LoginWithEmailId extends AppCompatActivity {
    private EditText passwd;
    private TextInputEditText emailId;
    private TextView switchTotv, switchTotvButton;

    private Button login;
    private FirebaseAuth mAuth;
    private TextView signup;
    private TextView forgotPassword;
    ProgressDialog dialog;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%!'\"~`^&*.,:{}()<>]).{6,20})";
    private Matcher matcher;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email_id);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.show();
        switchTotv = findViewById(R.id.tvSwitchNeedyVolunteer);
//        switchTotvButton = findViewById(R.id.tvSwitchAtLoginPage);
        switchTotv.setText("vsewa");
//        switchTotvButton.setText("Needy");

        emailId = findViewById(R.id.username);
        passwd= findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.tvSignup);
        forgotPassword = findViewById(R.id.tvFrogotPassword);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait,Logging in........");

//        switchTotvButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent loginNeedyIntent = new Intent(LoginWithEmailId.this, LoginWithEmailId.class);
//                startActivity(loginNeedyIntent);
//            }
//        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(LoginWithEmailId.this, ForgotPassword.class);
                startActivity(forgotPasswordIntent);
                finish();
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCancelable(false);
                dialog.show();

//                passwd.setErrorEnabled(false);
//                emailId.setErrorEnabled(false);


                String email_t=emailId.getEditableText().toString();
                String password_t=passwd.getEditableText().toString();

                if(email_t.equals("")) {
                    emailId.setError("Enter an email address");
//                    emailId.setErrorEnabled(true);
                    dialog.dismiss();
                }
                else if(!validateEmail(email_t)){
                    emailId.setError("Incorrect email address");
//                    emailId.setErrorEnabled(true);
                    dialog.dismiss();
                }
                else if(!validatePassword(password_t)){
                    passwd.setError("Invalid password");
//                    passwd.setErrorEnabled(true);
                    dialog.dismiss();

                }
                else if(validateEmail(email_t)&&validatePassword(password_t)){


                    mAuth.signInWithEmailAndPassword(email_t, password_t)
                            .addOnCompleteListener(LoginWithEmailId.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                                        final String current_user =mAuth.getCurrentUser().getUid();
                                        String device_token= FirebaseInstanceId.getInstance().getToken();
                                        databaseReference.child("Users").child(current_user).child("device_token").setValue(device_token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseReference.child("Users").child(current_user).child("isLoggedIn").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialog.dismiss();
                                                        Intent i=new Intent(LoginWithEmailId.this, BottomNavigatioActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information

                                    }
                                    // ...
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                passwd.setError("Invalid password");
//                                passwd.setErrorEnabled(true);
                                dialog.dismiss();
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                emailId.setError("Incorrect email address");
//                                emailId.setErrorEnabled(true);
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginWithEmailId.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(LoginWithEmailId.this, "Unknown error",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginWithEmailId.this, RegisterWithEmailActivity.class);
                startActivity(i);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
