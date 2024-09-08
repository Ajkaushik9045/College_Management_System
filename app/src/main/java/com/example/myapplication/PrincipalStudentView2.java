package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PrincipalStudentView2 extends AppCompatActivity {
    Button add,delete,update,record,deletebatch;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_student_view2);
        add=findViewById(R.id.ADD);
        update=findViewById(R.id.UPDATE);
        delete=findViewById(R.id.DELETE);
        record=findViewById(R.id.RECORD);
        deletebatch=findViewById(R.id.DeleteBatch);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PrincipalStudentView2.this,AddStudent.class);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PrincipalStudentView2.this,DeleteStudent.class);
                startActivity(intent);
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PrincipalStudentView2.this,PrincipalStudentRecord.class);
                startActivity(intent);
            }
        });
        deletebatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PrincipalStudentView2.this,DeleteBatch.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}