package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

public class StudentView extends AppCompatActivity {
    TextView textView, DatePicker;
    public static final String NAMES = "NAME", ROLL = "ROLL";
    public static final String Branch = "BRANCH", SEMESTER = "SEMESTER";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    float percentage = 0;
    Button Search;
    //    String NAME, ROLLNO, BRANCH2, SEMESTER2, roll, name, attendanceText, Present, branch1, semester1, total;
    TableLayout tableLayout;
    String Date = "date";
    int totalattendance = 0, nso = 0;
    CardView cardView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    BottomNavigationView bootom;
    String naming, branch, semester, rollno, Name;
    TextView NAME, BRANCH, SEMESTER1, ROLLNO;
    ImageButton fees, attendance, result;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        bootom = findViewById(R.id.bottomnavigation);
        Intent intent = getIntent();
        naming = intent.getStringExtra(NAMES);
        branch = intent.getStringExtra(Branch);
        semester = intent.getStringExtra(SEMESTER);
        rollno = intent.getStringExtra(ROLL);

        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainter, StudentHomeFragment.newInstance(naming, rollno, branch, semester)).commit();
        bootom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        temp = StudentHomeFragment.newInstance(naming, branch, semester, rollno);
                        break;
//                    case R.id.Attendance:
//                        temp = StudentAttendanceFragment.newInstance(rollno, naming);
//                        break;
                    case R.id.Student:
                        temp = new StudentNotificationFragment();
                        break;

                    case R.id.Result:
                        temp = new StudentNotesFragment();
                        break;
                }

                assert temp != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.framecontainter, temp).commit();
                return true;
            }
        });


//        drawerLayout = findViewById(R.id.my_drawer_layout);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//
//        // to make the Navigation drawer icon always appear on the action bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        navigationView = findViewById(R.id.navigationView);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                // Handle navigation item clicks here
//                switch (item.getItemId()) {
//
//                    case R.id.MonthAttendance:
//                        Intent intent = new Intent(StudentView.this, StudentMonthAttendance.class);
//                        intent.putExtra(StudentMonthAttendance.NAMES, naming);
//                        intent.putExtra(StudentMonthAttendance.ROLL, rollno);
//                        startActivity(intent);
//                        return true;
//                    case R.id.Developers:
//                        Intent intent4 = new Intent(StudentView.this, Developer.class);
//                        startActivity(intent4);
//                        return true;
//
//                    case R.id.Exit:
//                        new AlertDialog.Builder(StudentView.this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finishAffinity();
//                            }
//                        }).setNegativeButton("No", null).show();
//                }
//                return false;
//            }
//        });
//    }
//
//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onBackPressed() {
//
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            new AlertDialog.Builder(this).setMessage("Are you sure Want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finishAffinity();
//                }
//            }).setNegativeButton("No", null).show();
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
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