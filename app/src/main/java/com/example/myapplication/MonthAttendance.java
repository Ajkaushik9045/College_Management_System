package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MonthAttendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner BRANCH;
    private Spinner SEMESTER;
    private Button CHECK;
    private TextView DatePicker;
    String Date="Date";
    int limit=0;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_attendance);
        BRANCH=findViewById(R.id.Branchspinner);
        SEMESTER=findViewById(R.id.Semesterspinner);
        DatePicker=findViewById(R.id.datepicker);

        CHECK=findViewById(R.id.CheckButton);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Select_Department,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BRANCH.setAdapter(adapter);
        BRANCH.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.select_Semester,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SEMESTER.setAdapter(adapter1);
        SEMESTER.setOnItemSelectedListener(this);

//        FOR SELECTING BRANCH THROUGH SPINNER
        BRANCH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Branch=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SEMESTER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Semester=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(MonthAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        Date="01"+"/"+month+"/"+year;
                        if(month==1){
                            limit=31;
                        } else if (month==2&&year%4==0) {
                            limit=29;
                        } else if (month==2&&year%4!=0) {
                            limit=28;
                        } else if (month==3) {
                            limit=31;
                        }else if (month==4) {
                            limit=30;
                        }else if (month==5) {
                            limit=31;
                        }else if (month==6) {
                            limit=30;
                        }else if (month==7) {
                            limit=31;
                        }else if (month==8) {
                            limit=31;
                        }else if (month==9) {
                            limit=30;
                        }else if (month==10) {
                            limit=31;
                        }else if (month==11) {
                            limit=30;
                        }else if (month==12) {
                            limit=31;
                        }
                        DatePicker.setText(Date);
                    }
                } ,year,month,day);
                datePickerDialog.show();
            }
        });

        CHECK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected values from spinners

                String selectedBranch = BRANCH.getSelectedItem().toString();
                String selectedSemester = SEMESTER.getSelectedItem().toString();
                if (selectedBranch.equals("Branch")) {
                    Toast.makeText(MonthAttendance.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (selectedSemester.equals("Sem")) {
                    Toast.makeText(MonthAttendance.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else if (Date.equals("Date")) {
                    Toast.makeText(MonthAttendance.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an intent to launch the showattendance activity
                    Intent intent = new Intent(MonthAttendance.this, MonthAttendanceShow.class);

                    // Pass the selected values as extras
                    intent.putExtra("branch", selectedBranch);
                    intent.putExtra("semester", selectedSemester);
                    intent.putExtra("date", Date);
                    intent.putExtra("LIMIT",limit);

                    // Start the showattendance activity with the intent
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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



