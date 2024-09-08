package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddResult extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner BRANCH, SEMESTER, SUBJECT, MARKS, SESSIONAL;
    String Branch = "Branch", Semester = "Sem", Subject = "Subject", Marks = "Marks", Sessional = "Sessional";
    ArrayList<String> subjectList = new ArrayList<>();
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);
        BRANCH = findViewById(R.id.spinnerBranch);
        SEMESTER = findViewById(R.id.spinnerStud);
        SUBJECT = findViewById(R.id.subjectname);
        MARKS = findViewById(R.id.Marks);
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
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SUBJECT.setAdapter(adapter4);
        SUBJECT.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.select_Marks, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MARKS.setAdapter(adapter2);
        MARKS.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.select_Session, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SESSIONAL.setAdapter(adapter3);
        SESSIONAL.setOnItemSelectedListener(this);
        BRANCH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch = adapterView.getItemAtPosition(i).toString();
                if (Branch.equals("Branch")) {
                    // Handle when Branch is not selected
                    SUBJECT.setEnabled(false);
                    MARKS.setEnabled(false);
                    SESSIONAL.setEnabled(false);
                    search.setEnabled(false);
                } else if (Semester.equals("Sem")) {
                    // Handle when Semester is not selected
                    SUBJECT.setEnabled(false);
                    MARKS.setEnabled(false);
                    SESSIONAL.setEnabled(false);
                    search.setEnabled(false);
                } else {
                    databaseStudent.child(Branch).child(Semester).child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            subjectList.clear();
                            boolean subjectFound = false;
                            for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                String subjectCode = subjectSnapshot.child("Code").getValue(String.class);
                                String subjectName = subjectSnapshot.child("Subject").getValue(String.class);

//                                if (subjectCode == null && subjectName == null) {
//                                    subjectFound = false;
//                                }else{
                                String subjectInfo = subjectCode + " - " + subjectName;
                                subjectList.add(subjectInfo);
                                subjectFound = true;
//                                }
                            }
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(AddResult.this, android.R.layout.simple_spinner_item, subjectList);
                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            SUBJECT.setAdapter(adapter4);
                            SUBJECT.setEnabled(true);
                            MARKS.setEnabled(true);
                            SESSIONAL.setEnabled(true);
                            search.setEnabled(true);
                            if (!subjectFound) {
                                Toast.makeText(AddResult.this, "Subject not found", Toast.LENGTH_SHORT).show();
                                SUBJECT.setEnabled(false);
                                MARKS.setEnabled(false);
                                SESSIONAL.setEnabled(false);
                                search.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SEMESTER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Semester = adapterView.getItemAtPosition(i).toString();
                if (Branch.equals("Branch")) {
                    // Handle when Branch is not selected
                    SUBJECT.setEnabled(false);
                    MARKS.setEnabled(false);
                    SESSIONAL.setEnabled(false);
                    search.setEnabled(false);
                } else if (Semester.equals("Sem")) {
                    // Handle when Semester is not selected
                    SUBJECT.setEnabled(false);
                    MARKS.setEnabled(false);
                    SESSIONAL.setEnabled(false);
                    search.setEnabled(false);
                } else {
                    databaseStudent.child(Branch).child(Semester).child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            subjectList.clear();
                            boolean subjectFound = false;
                            for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                String subjectCode = subjectSnapshot.child("Code").getValue(String.class);
                                String subjectName = subjectSnapshot.child("Subject").getValue(String.class);

//                                if (subjectCode == null && subjectName == null) {
//                                    subjectFound = false;
//                                }else{
                                String subjectInfo = subjectCode + " - " + subjectName;
                                subjectList.add(subjectInfo);
                                subjectFound = true;
//                                }
                            }
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(AddResult.this, android.R.layout.simple_spinner_item, subjectList);
                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            SUBJECT.setAdapter(adapter4);
                            SUBJECT.setEnabled(true);
                            MARKS.setEnabled(true);
                            SESSIONAL.setEnabled(true);
                            search.setEnabled(true);
                            if (!subjectFound) {
                                Toast.makeText(AddResult.this, "Subject not found", Toast.LENGTH_SHORT).show();
                                SUBJECT.setEnabled(false);
                                MARKS.setEnabled(false);
                                SESSIONAL.setEnabled(false);
                                search.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SUBJECT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subject = adapterView.getItemAtPosition(i).toString();
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
        MARKS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Marks = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(AddResult.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(AddResult.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else if (Marks.equals("Marks")) {
                    Toast.makeText(AddResult.this, "Select Marks", Toast.LENGTH_SHORT).show();
                } else if (Sessional.equals("Sessional")) {
                    Toast.makeText(AddResult.this, "Select Sessional", Toast.LENGTH_SHORT).show();
                } else {
                    if (SUBJECT.isEnabled() && MARKS.isEnabled() && SESSIONAL.isEnabled()) {

                        Intent intent = new Intent(AddResult.this, AddResultStart.class);
                        intent.putExtra(AddResultStart.BRANCH1, Branch);
                        intent.putExtra(AddResultStart.SEMESTER1, Semester);
                        intent.putExtra(AddResultStart.SUBJECT1, Subject);
                        intent.putExtra(AddResultStart.MARKS1, Marks);
                        intent.putExtra(AddResultStart.SESSIONAL1, Sessional);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddResult.this, "Please select Branch and Semester first", Toast.LENGTH_SHORT).show();
                    }
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