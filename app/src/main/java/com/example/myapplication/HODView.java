package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HODView extends AppCompatActivity {
    Button teacher, student, attendance,result;
    public static final String NAME = "NAME";
    TextView textView;
    String Name, branch;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    BottomNavigationView bootom;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodview);
        bootom = findViewById(R.id.bottomnavigation);
        Intent intents = getIntent();
        Name = intents.getStringExtra(NAME);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainter, HODHomeFragment.newInstance(Name)).commit();
        bootom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        temp = HODHomeFragment.newInstance(Name);
                        break;
                    case R.id.Attendance:
                        temp = AttendanceFragment.newInstance(Name);
                        break;
                    case R.id.Student:
                        temp = new NotificationFragment();
                        break;
                    case R.id.Add:
                        temp = new ADDFragment();
                        break;
                    case R.id.Result:
                        temp = new NotesFragment();
                        break;
                }

                assert temp != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.framecontainter, temp).commit();
                return true;
            }
        });
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.ADDTEACHER:
                        Intent intent = new Intent(HODView.this, AddTeacher.class);
                        startActivity(intent);
                        return true;
                    case R.id.ADDStudent:
                        Intent intent2 = new Intent(HODView.this, AddStudent.class);
                        startActivity(intent2);
                        return true;
                    case R.id.TAKEATTENDANCE:
                        Intent intent3 = new Intent(HODView.this, TakeAttendance.class);
                        intent3.putExtra(TakeAttendance.NAME, Name);
                        startActivity(intent3);
                        return true;
                    case R.id.Developers:
                        Intent intent4 = new Intent(HODView.this, Developer.class);
                        startActivity(intent4);
                        return true;
                    case R.id.TEACHERRECORD:
                        Intent intent5 = new Intent(HODView.this, TeacherRecord.class);
                        startActivity(intent5);
                        return true;
                    case R.id.MonthAttendance:
                        Intent intent6 = new Intent(HODView.this, MonthAttendance.class);
                        startActivity(intent6);
                        return true;
                    case R.id.updateSemester:
                        Intent intent7 = new Intent(HODView.this, UpdateSemester.class);
                        startActivity(intent7);
                        return true;

                    case R.id.Exit:
                    new AlertDialog.Builder(HODView.this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finishAffinity();
                                }
                            })
                    .setNegativeButton("No", null).show();
                }
                return false;
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
        new AlertDialog.Builder(this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishAffinity();
                            }
                        })
                .setNegativeButton("No", null).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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