package com.example.vsewa.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vsewa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button verify;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Matcher matcher;
    ProgressDialog dialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        if(getSupportActionBar()!=null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setTitle("Forgot Password");
//        }

        email=findViewById(R.id.tvForgotPasswordEmailId);
        verify=findViewById(R.id.btnresetPassword);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Verifying Email...");
        dialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                email.setErrorEnabled(false);

                dialog.show();
                String sEmail=email.getText().toString();

                if(validateEmail(sEmail)){

                    //reset pass
                    firebaseAuth.sendPasswordResetEmail(sEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        dialog.cancel();
//
                                        Toast.makeText(ForgotPassword.this, "Please reset password using link sent to your email", Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(ForgotPassword.this,LoginWithEmailIdVolunteer.class);
                                        startActivity(intent);
                                    }
//                            FirebaseAuth auth = FirebaseAuth.getInstance();
//
//                            if(Objects.requireNonNull(task.getResult()).getSignInMethods().size()>=1)
//                            {
//                                auth.sendPasswordResetEmail(sEmail)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    dialog.cancel();
//
//                                                    Toast.makeText(ForgotPassword.this, "Please reset password using link sent to your email", Toast.LENGTH_SHORT).show();
//                                                    Intent intent =new Intent(ForgotPassword.this,LoginWithEmailActivity.class);
//                                                    startActivity(intent);
//                                                }
//                                            }
//                                        });
//                            }
                                    else{
                                        dialog.cancel();
//                                        email.setErrorEnabled(true);
                                        email.setError("Email id doesn't exists");
                                    }

                                }
                            });

                }
                else{
                    dialog.cancel();
                    email.setError("Please enter a valid email");
//                    email.setErrorEnabled(true);
                }
            }
        });



    }

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}