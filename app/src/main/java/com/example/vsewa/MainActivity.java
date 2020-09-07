package com.example.vsewa;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.vsewa.Authentication.LoginWithEmailId;
import com.example.vsewa.Authentication.RegisterWithEmailActivity;
import com.example.vsewa.NavigationButton.BottomNavigatioActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView switchTotv, switchTotvButton, welcomeScreenSignUp;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
            }
        }, 1000);

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), BottomNavigatioActivity.class);
            startActivity(intent);
        }
//        Button btVolunteer = findViewById(R.id.btVolunteer);
        TextView login = findViewById(R.id.tvWelocmeScreenLogin);
        welcomeScreenSignUp = findViewById(R.id.tvWelcomeScreenSignUp);

        welcomeScreenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, RegisterWithEmailActivity.class);
                startActivity(signUpIntent);
                finish();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volunteerLoginIntent = new Intent(MainActivity.this, LoginWithEmailId.class);
                startActivity(volunteerLoginIntent);
                finish();
            }
        });

//        btNeedy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent needyLoginIntent = new Intent(MainActivity.this, LoginWithEmailId.class);
//                startActivity(needyLoginIntent);
//                finish();
//            }
//        });

    }
}
