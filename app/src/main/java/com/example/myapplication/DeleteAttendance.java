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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteAttendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner BRANCH,SEMESTER;
    Button SAVE;
    String Branch,Semester;
    String ID,NAME,BRANCH1,StuID,StuSemester;
    int num,count;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    DatabaseReference databaseStudent= FirebaseDatabase.getInstance().getReference("Student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_attendance);

        SAVE=findViewById(R.id.SaveButton);
        BRANCH=findViewById(R.id.BranchSpinner);
        SEMESTER=findViewById(R.id.SemesterSpinner);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BRANCH.setAdapter(adapter1);
        BRANCH.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SEMESTER.setAdapter(adapter2);
        SEMESTER.setOnItemSelectedListener(this);
        BRANCH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SEMESTER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Semester=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Branch.equals("Branch")){
                    Toast.makeText(DeleteAttendance.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(DeleteAttendance.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else  {
                    ProgressDialog progressDialog = new ProgressDialog(DeleteAttendance.this);
                    progressDialog.setMessage("Deleting Attendance...");
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




                    databaseStudent.orderByChild("branch").equalTo(Branch).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            num= (int) snapshot.getChildrenCount();
                            boolean isfound=false;
                            for (DataSnapshot dsp : snapshot.getChildren()) {
                                BRANCH1 = dsp.child("branch").getValue().toString();
                                StuSemester = dsp.child("semester").getValue().toString();
                                String key = dsp.getKey();
                                if(StuSemester.equals(Semester)){
                                    isfound=true;
                                    databaseStudent.child(key).child("absent").setValue(0);
                                    databaseStudent.child(key).child("nsoAbsent").setValue(0);
                                    databaseStudent.child(key).child("present").setValue(0);
                                    databaseStudent.child(key).child("attendance").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                count++;
                                                if(count == num) {
                                                    if(count == 0) {
                                                        Toast.makeText(getApplicationContext(), "No Attendance found for selected branch and semester", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(DeleteAttendance.this, "Attendance Delete successfully", Toast.LENGTH_SHORT).show();
                                                      progressDialog.dismiss();
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                                else {
                                    num--;
                                }
                            }
                            if(!isfound){
                                Toast.makeText(DeleteAttendance.this, "Data not found ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


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