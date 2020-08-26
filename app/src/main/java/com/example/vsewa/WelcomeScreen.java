package com.example.vsewa;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        //Welcome Screen when app starts
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeScreenIntent = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(welcomeScreenIntent);
                finish();
            }
        }, 3000);
    }
}
