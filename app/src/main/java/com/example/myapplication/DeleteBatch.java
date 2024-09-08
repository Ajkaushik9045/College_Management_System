package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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

public class DeleteBatch extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner Branchlist;
    Spinner Semesterlist;
    private Button search;

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    String Branch,Semester,StuBranch,StuSemester;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_batch);
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
                    Toast.makeText(DeleteBatch.this, "Select the Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(DeleteBatch.this, "Select the Semester", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(DeleteBatch.this);
                    progressDialog.setMessage("Deleting Batch...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.show();
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(Branch).exists()) {
                                DataSnapshot branchSnapshot = snapshot.child(Branch);
                                if (branchSnapshot.child(Semester).exists()) {
                                    DataSnapshot semesterSnapshot = branchSnapshot.child(Semester);
                                    if (semesterSnapshot.child("Total Attendance").exists()) {
                                        semesterSnapshot.getRef().child("date").setValue(null);
                                        semesterSnapshot.getRef().child("Total Attendance").setValue(0);
                                    }

                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });


                    reference.orderByChild("branch").equalTo(Branch).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            try {
                                for (DataSnapshot branchSnapshot : snapshot.getChildren()) {
                                    if (branchSnapshot.child("semester").getValue(String.class).equals(Semester)) {
                                        branchSnapshot.getRef().removeValue();
                                    }
                                }
                                Toast.makeText(DeleteBatch.this, "Batch delete successful", Toast.LENGTH_SHORT).show();
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