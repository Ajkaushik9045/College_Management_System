package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResultRecord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner BRANCH, SEMESTER, SESSIONAL;
    String Branch = "Branch", Semester = "Sem", Subject = "Subject", Marks = "Marks", Sessional = "Sessional";
    ArrayList<String> subjectList = new ArrayList<>();
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    Button search;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_record);
        BRANCH = findViewById(R.id.spinnerBranch);
        SEMESTER = findViewById(R.id.spinnerStud);
        SESSIONAL = findViewById(R.id.sessionalnumber);
        search = findViewById(R.id.searchs);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BRANCH.setAdapter(adapter);
        BRANCH.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SEMESTER.setAdapter(adapter1);
        SEMESTER.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.select_Session, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SESSIONAL.setAdapter(adapter3);
        SESSIONAL.setOnItemSelectedListener(this);
        BRANCH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SEMESTER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Semester = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        SESSIONAL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Sessional = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(ResultRecord.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(ResultRecord.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else if (Sessional.equals("Sessional")) {
                    Toast.makeText(ResultRecord.this, "Select Sessional", Toast.LENGTH_SHORT).show();
                } else {


                        Intent intent = new Intent(ResultRecord.this, ResultRecordShow.class);
                        intent.putExtra(ResultRecordShow.BRANCH1, Branch);
                        intent.putExtra(ResultRecordShow.SEMESTER1, Semester);
                        intent.putExtra(ResultRecordShow.SESSIONAL1, Sessional);
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
}