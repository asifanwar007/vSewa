package com.example.vsewa.Authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vsewa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;

public class LoginWithEmailIdNeedy extends AppCompatActivity {
    private EditText passwd;
    private TextInputEditText emailId;

    private Button login;
    private FirebaseAuth mAuth;
    private TextView signup;
    private TextView forgotPassword;
    ProgressBar progressBar;
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

        emailId = findViewById(R.id.username);
        passwd= findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.tvSignup);
        forgotPassword = findViewById(R.id.tvFrogotPassword);
        dialog = new

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String password = passwd.getText().toString();



            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);

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
