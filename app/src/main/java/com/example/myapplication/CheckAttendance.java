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

public class CheckAttendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner BRANCH;
    private Spinner SEMESTER;
    private Button CHECK;
    private TextView DatePicker;
    String Date="date";
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
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
                DatePickerDialog datePickerDialog=new DatePickerDialog(CheckAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        Date=day+"/"+month+"/"+year;
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

               if(selectedBranch.equals("Branch")){
                   Toast.makeText(CheckAttendance.this, "Select Branch", Toast.LENGTH_SHORT).show();
               } else if (selectedSemester.equals("Sem")) {
                   Toast.makeText(CheckAttendance.this, "Select Semester", Toast.LENGTH_SHORT).show();
               } else if (Date.equals("date")) {
                   Toast.makeText(CheckAttendance.this, "Select Date", Toast.LENGTH_SHORT).show();
               }else {
                   Intent intent = new Intent(CheckAttendance.this, showattendance.class);

                   // Pass the selected values as extras
                   intent.putExtra("branch", selectedBranch);
                   intent.putExtra("semester", selectedSemester);
                   intent.putExtra("date", Date);

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