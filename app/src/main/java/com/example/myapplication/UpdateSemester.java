package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateSemester extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner SPINNER1, SPINNER2, SPINNER3;

    String Semester;
    String ID, NAME, BRANCH1, StuID, StuSemester;
    int num;

    String SEMESTER;
    Button update;
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    String Branch, semester1, semester2;
    int count = 0, count2=0, totalstudent;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("Student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_semester);

        SPINNER1 = findViewById(R.id.Branchspinner);
        SPINNER2 = findViewById(R.id.Fromspinner);
        SPINNER3 = findViewById(R.id.Tospinner);
        update = findViewById(R.id.ChangeButton);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER1.setAdapter(adapter);
        SPINNER1.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER2.setAdapter(adapter1);
        SPINNER2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER3.setAdapter(adapter2);
        SPINNER3.setOnItemSelectedListener(this);
        SPINNER1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SPINNER2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester1 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SPINNER3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester2 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(UpdateSemester.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (semester1.equals("Sem")) {
                    Toast.makeText(UpdateSemester.this, "Select From Semester", Toast.LENGTH_SHORT).show();
                } else if (semester2.equals("Sem")) {
                    Toast.makeText(UpdateSemester.this, "Select to Semester", Toast.LENGTH_SHORT).show();
                } else if (semester2.equals(semester1)) {
                    Toast.makeText(UpdateSemester.this, "Select Alternative Semester", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSemester.this);
                    builder.setTitle("Confirm Update Semester");
                    builder.setMessage("Are you sure you want to Update Semester?");

                    // Add "Yes" button
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog progressDialog = new ProgressDialog(UpdateSemester.this);
                            progressDialog.setMessage("Update Semester And Deleting Attendance...");
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(false);
                            progressDialog.getWindow().setGravity(Gravity.CENTER);
                            progressDialog.show();
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(Branch).exists()) {
                                        DataSnapshot branchSnapshot = snapshot.child(Branch);
                                        if (branchSnapshot.child(semester1).exists()) {
                                            DataSnapshot semesterSnapshot = branchSnapshot.child(semester1);
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
                                    totalstudent = (int) snapshot.getChildrenCount();
                                    count = 0;
                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                        String ssemester = dsp.child("semester").getValue(String.class);
                                        if (ssemester != null && ssemester.equals(semester1)) {
                                            dsp.getRef().child("semester").setValue(semester2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        count++;
                                                        if (count == totalstudent) {
                                                            if (count == 0) {
                                                                Toast.makeText(getApplicationContext(), "No students found for selected branch and semester", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(UpdateSemester.this, "Semester Update successfully", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            totalstudent--;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseStudent.orderByChild("branch").equalTo(Branch).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    num = (int) snapshot.getChildrenCount();
                                    boolean isfound = false;
                                    count2 = 0;
                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                        BRANCH1 = dsp.child("branch").getValue().toString();
                                        StuSemester = dsp.child("semester").getValue().toString();
                                        String key = dsp.getKey();
                                        if (StuSemester.equals(semester1)) {
                                            isfound = true;
                                            databaseStudent.child(key).child("absent").setValue(0);
                                            databaseStudent.child(key).child("nsoAbsent").setValue(0);
                                            databaseStudent.child(key).child("present").setValue(0);
                                            databaseStudent.child(key).child("attendance").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        count2++;
                                                        if (count2 == num) {
                                                            if (count2 == 0) {
                                                                Toast.makeText(getApplicationContext(), "No Attendance found for selected branch and semester", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(UpdateSemester.this, "Attendance Delete successfully", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            num--;
                                        }
                                    }
                                    if (!isfound) {
                                        Toast.makeText(UpdateSemester.this, "Data not found ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });


                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked "No", do nothing
                            dialog.dismiss();
                        }
                    });

                    // Show the dialog box
                    AlertDialog dialog = builder.create();
                    dialog.show();
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