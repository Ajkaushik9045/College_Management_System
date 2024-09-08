package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionDesignation extends AppCompatActivity {

Button principle,hod,teacher,student,developers;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_designation);
        principle=findViewById(R.id.principle);
        hod=findViewById(R.id.hod);
        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.student);
        developers=findViewById(R.id.Developers);
        Intent loginPage=new Intent(SelectionDesignation.this,MainActivity.class);

            principle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginPage.putExtra(MainActivity.ACCOUNT,"PRINCIPAL");
                    startActivity(loginPage);
                }
            });
            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginPage.putExtra(MainActivity.ACCOUNT,"TEACHER");
                    startActivity(loginPage);
                }
            });
            hod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginPage.putExtra(MainActivity.ACCOUNT,"HOD");
                    startActivity(loginPage);
                }
            });
            student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loginPage.putExtra(MainActivity.ACCOUNT,"STUDENT");
                    startActivity(loginPage);
                }
            });
            developers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2=new Intent(SelectionDesignation.this,Developer.class);
                    startActivity(intent2);
                }
            });
//        student.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2=new Intent(SelectionDesignation.this,PrincipleView.class);
//                startActivity(intent2);
//            }
//        });

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishAffinity();
                            }
                        })
                .setNegativeButton("No", null).show();
    }
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.developersoption,menu);
//        return true;
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//       if(id==R.id.Developers){
//            Intent intent = new Intent(SelectionDesignation.this, Help.class);
//            startActivity(intent );
//            return true;
//        }
//
//        else if(id==R.id.Exit) {
//
//            new AlertDialog.Builder(this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new
//                            DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    finishAffinity();
//                                }
//                            })
//                    .setNegativeButton("No", null).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
