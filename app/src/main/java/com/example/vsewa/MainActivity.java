package com.example.vsewa;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vsewa.Authentication.LoginWithEmailIdNeedy;
import com.example.vsewa.Authentication.LoginWithEmailIdVolunteer;
import com.example.vsewa.Authentication.RegisterWithEmailActivity;

public class MainActivity extends AppCompatActivity {
    private TextView switchTotv, switchTotvButton, welcomeScreenSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btVolunteer = findViewById(R.id.btVolunteer);
        Button btNeedy = findViewById(R.id.btNeedy);
        welcomeScreenSignUp = findViewById(R.id.tvWelcomeScreenSignUp);

        welcomeScreenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, RegisterWithEmailActivity.class);
                startActivity(signUpIntent);
                finish();
            }
        });



        btVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volunteerLoginIntent = new Intent(MainActivity.this, LoginWithEmailIdVolunteer.class);
                startActivity(volunteerLoginIntent);
                finish();
            }
        });

        btNeedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent needyLoginIntent = new Intent(MainActivity.this, LoginWithEmailIdNeedy.class);
                startActivity(needyLoginIntent);
                finish();
            }
        });

    }
}
