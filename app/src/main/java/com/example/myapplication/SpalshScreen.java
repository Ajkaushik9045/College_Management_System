package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.window.SplashScreen;

public class SpalshScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spalsh_screen);



        Intent homepage= new Intent(SpalshScreen.this,SelectionDesignation.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(homepage);
                finish();
            }
        },500);


    }
}