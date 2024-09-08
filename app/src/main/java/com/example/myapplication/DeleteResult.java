package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
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

public class DeleteResult extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner Branchlist;
    Spinner Semesterlist;
    private Button search;

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Student");
    String Branch,Semester,StuBranch,StuSemester;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_result);
        Branchlist=findViewById(R.id.spinnerBranch);
        Semesterlist=findViewById(R.id.spinnerStud);
        search=findViewById(R.id.searchs);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Branchlist.setAdapter(adapter);
        Branchlist.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Semesterlist.setAdapter(adapter1);
        Semesterlist.setOnItemSelectedListener(this);

//        FOR SELECTING BRANCH THROUGH SPINNER
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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(DeleteResult.this, "Select the Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(DeleteResult.this, "Select the Semester", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(DeleteResult.this);
                    progressDialog.setMessage("Deleting Result...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.show();
                    reference.orderByChild("branch").equalTo(Branch).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                for (DataSnapshot branchSnapshot : snapshot.getChildren()) {
                                    if (branchSnapshot.child("semester").getValue(String.class).equals(Semester)) {
                                        DatabaseReference marksReference = branchSnapshot.child("marks").getRef();
                                        marksReference.removeValue();
                                    }
                                }
                                Toast.makeText(DeleteResult.this, "Result Delete successful", Toast.LENGTH_SHORT).show();
                                finish();
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            error.toException().printStackTrace();
                        }
                    });

                }
            }
        });


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
}