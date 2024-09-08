package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class PrincipalTakeAttendance  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner Branchlist;
    String Attendance;
    Spinner Semesterlist, PeriodNumber;
    CheckBox lastAttedance;

    private Button search;
    public static final String NAME = "NAME";
    private TextView DatePicker, TeacherName;
    int Lab;
    String previous1;
    TextView lab;


    ArrayList listTeacher = new ArrayList<>();
    //    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Student");
    String Branch, Semester, Period, Date = "Date", Name;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_take_attendance);
        Branchlist = findViewById(R.id.spinnerBranch);
        Semesterlist = findViewById(R.id.spinnerStud);
        PeriodNumber = findViewById(R.id.spinnerPeriod);
        DatePicker = findViewById(R.id.DatePicker);
        search = findViewById(R.id.searchs);
        lastAttedance = findViewById(R.id.lastCheckBox);

        Intent intent = getIntent();
        Name = intent.getStringExtra(NAME);
//        TeacherName.setText(TName);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Branchlist.setAdapter(adapter);
        Branchlist.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Semesterlist.setAdapter(adapter1);
        Semesterlist.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.select_Period, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PeriodNumber.setAdapter(adapter2);
        PeriodNumber.setOnItemSelectedListener(this);


        Branchlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Semesterlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Semester = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        PeriodNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Period = adapterView.getItemAtPosition(i).toString();
                previous1 = Period;

                if (Period.equals("Period 1")) {
                    lastAttedance.setEnabled(false);
                    lastAttedance.setChecked(false);
                } else {
                    lastAttedance.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        PeriodNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Period = adapterView.getItemAtPosition(i).toString();
                previous1 = Period;

                if (Period.equals("1")) {
                    lastAttedance.setEnabled(false);
                    lastAttedance.setChecked(false);
                } else {
                    lastAttedance.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PrincipalTakeAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Date = day + "/" + month + "/" + year;
                        DatePicker.setText(Date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(PrincipalTakeAttendance.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(PrincipalTakeAttendance.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(Period)) { // Check if Period is numeric
                    Toast.makeText(PrincipalTakeAttendance.this, "Select a valid Period", Toast.LENGTH_SHORT).show();
                } else if (Date.equals("Date")) {
                    Toast.makeText(PrincipalTakeAttendance.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    lastAttedance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                // Check if Period is numeric before performing calculations
                                if (isNumeric(Period)) {
                                    previous1 = String.valueOf(Integer.parseInt(Period) - 1);
                                } else {
//                                    Toast.makeText(TakeAttendance.this, "Select a valid Period", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                previous1 = Period;
                            }
                        }
                    });

                    Intent intent = new Intent(PrincipalTakeAttendance.this, PrincipalStartAttendance.class);
                    intent.putExtra(PrincipalStartAttendance.BRANCH1, Branch);
                    intent.putExtra(PrincipalStartAttendance.SEMESTER1, Semester);
                    intent.putExtra(PrincipalStartAttendance.PERIOD1, Period);
                    intent.putExtra(PrincipalStartAttendance.DATE1, Date);
                    intent.putExtra(PrincipalStartAttendance.NAMES, Name);
                    intent.putExtra(PrincipalStartAttendance.past1, previous1);

                    Branchlist.setSelection(0);
                    Semesterlist.setSelection(0);
                    PeriodNumber.setSelection(0);
                    lastAttedance.setChecked(false);
                    DatePicker.setText("Select Date");
                    startActivity(intent);
                }
            }
        });
    }

    // Add this method to check if a string is numeric
    private boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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