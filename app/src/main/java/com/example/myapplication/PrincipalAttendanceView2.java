package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalAttendanceView2 extends AppCompatActivity {
    public static final String NAME = "name";
    String name;

    Button take, delete, check,NSO;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_attendance_view2);
        take = findViewById(R.id.TAKEATTENDANCE);
        delete = findViewById(R.id.DELETEATTENDANCE);
        check = findViewById(R.id.CHECKATTENDANCE);
        NSO=findViewById(R.id.NSOSTUDENTS);
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalAttendanceView2.this, PrincipalTakeAttendance.class);
                intent.putExtra(TakeAttendance.NAME, name);
                startActivity(intent);
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalAttendanceView2.this, CheckAttendance.class);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalAttendanceView2.this, DeleteAttendance.class);
                startActivity(intent);
            }
        });
        NSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalAttendanceView2.this, PrincipalNsoStudent.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}