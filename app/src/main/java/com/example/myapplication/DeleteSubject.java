package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteSubject extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText  Code;
    TextView Subject;
    Spinner SPINNER1, SPINNER2;
    String Branch = "Branch", Semester = "Sem",code="code",subName;
    Button save,search;
    ConstraintLayout constraintLayout;
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_subject);
        Subject = findViewById(R.id.EditStudentNameField);
        Code = findViewById(R.id.EditStudentRollField);
        SPINNER1 = findViewById(R.id.Branchspinner);
        SPINNER2 = findViewById(R.id.Semesterspinner);
        save = findViewById(R.id.SaveButton);
        search=findViewById(R.id.Searchs);
        constraintLayout=findViewById(R.id.constraintLayout8);
        constraintLayout.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER1.setAdapter(adapter);
        SPINNER1.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER2.setAdapter(adapter1);
        SPINNER2.setOnItemSelectedListener(this);

        SPINNER1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch = adapterView.getItemAtPosition(i).toString();
                constraintLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SPINNER2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Semester = adapterView.getItemAtPosition(i).toString();
                constraintLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = Code.getText().toString().toUpperCase();
                if (Branch.equals("Branch")) {
                    constraintLayout.setVisibility(View.GONE);
                    Toast.makeText(DeleteSubject.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    constraintLayout.setVisibility(View.GONE);
                    Toast.makeText(DeleteSubject.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else if (code.isEmpty()) {
                    constraintLayout.setVisibility(View.GONE);
                    Toast.makeText(DeleteSubject.this, "Enter Subject Code", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(DeleteSubject.this);
                    progressDialog.setMessage("Loading Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    databaseStudent.child(Branch).child(Semester).child("Subject").orderByChild("Code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean subjectFound = false;
                            for (DataSnapshot dsp: snapshot.getChildren()) {
                                subName = dsp.child("Subject").getValue().toString();
                                String subCode = dsp.child("Code").getValue().toString();
                                if (code.equals(subCode)) {
                                    Subject.setText(subName);
                                    subjectFound = true;
                                    break;
                                }
                            }
                            progressDialog.dismiss();
                            if (!subjectFound) {
                                constraintLayout.setVisibility(View.GONE);
                                Toast.makeText(DeleteSubject.this, "Subject not found", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                constraintLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            constraintLayout.setVisibility(View.GONE);
                            Toast.makeText(DeleteSubject.this, "Failed to search for subject", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(DeleteSubject.this);
                progressDialog.setMessage("Deleting Subject...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                databaseStudent.child(Branch).child(Semester).child("Subject").orderByChild("Code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean subjectFound = false;
                        for (DataSnapshot dsp: snapshot.getChildren()) {
                            String subCode = dsp.child("Code").getValue().toString();
                            if (code.equals(subCode)) {
                                dsp.getRef().removeValue();
                                subjectFound = true;
                                break;
                            }
                        }
                        progressDialog.dismiss();
                        if (subjectFound) {
                            Toast.makeText(DeleteSubject.this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
                            constraintLayout.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(DeleteSubject.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DeleteSubject.this, "Failed to delete subject", Toast.LENGTH_SHORT).show();
                    }
                });
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
