package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PrincipalNsoStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner Branchlist, Semesterlist;
    TableLayout tableLayout;
    Button search, save;
    String NAME, ROLLNO, BRANCH, SEMESTER;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    String Branch, Semester;
    int nso = 0;
    CardView cardView;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    boolean isDataFound = false;
    HashMap<String, Boolean> attendance = new HashMap<>(); // Create a HashMap to store attendance
    HashMap<String, Boolean> attendance1 = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_nso_student);
        Branchlist = findViewById(R.id.spinnerBranch);
        Semesterlist = findViewById(R.id.spinnerStud);
        search = findViewById(R.id.searchs);
        tableLayout = findViewById(R.id.Studentlist);
        cardView = findViewById(R.id.cardView2);
        cardView.setVisibility(View.GONE);
        save = findViewById(R.id.saves);
        save.setVisibility(View.GONE);
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
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    Toast.makeText(PrincipalNsoStudent.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    Toast.makeText(PrincipalNsoStudent.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(PrincipalNsoStudent.this);
                    progressDialog.setMessage("Loading Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();

                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    reference.orderByChild("semester").equalTo(Semester).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                cardView.setVisibility(View.GONE);
                                save.setVisibility(View.GONE);
                                try {
                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                        BRANCH = dsp.child("branch").getValue().toString();
                                        SEMESTER = dsp.child("semester").getValue().toString();
                                        NAME = dsp.child("name").getValue().toString();
                                        ROLLNO = dsp.child("roll").getValue().toString();
                                        nso = dsp.child("nsoAbsent").getValue(Integer.class);
                                        if (BRANCH.equals(Branch) && SEMESTER.equals(Semester) && nso > 97) {
                                            isDataFound = true;
                                            save.setVisibility(View.VISIBLE);
                                            cardView.setVisibility(View.VISIBLE);
                                            TableRow tableRow = new TableRow(PrincipalNsoStudent.this);
                                            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                            TextView ViewName = new TextView(PrincipalNsoStudent.this);
                                            ViewName.setText(ROLLNO);
                                            ViewName.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewName);


                                            TextView ViewID = new TextView(PrincipalNsoStudent.this);
                                            ViewID.setText(NAME);
                                            ViewID.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewID);

                                            TextView ViewBranch = new TextView(PrincipalNsoStudent.this);
                                            ViewBranch.setText(BRANCH);
                                            ViewBranch.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewBranch);

                                            TextView ViewPassword = new TextView(PrincipalNsoStudent.this);
                                            ViewPassword.setText(SEMESTER);
                                            ViewPassword.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewPassword);

                                            CheckBox checkBoxPresent = new CheckBox(PrincipalNsoStudent.this);
                                            checkBoxPresent.setId(R.id.attendanceCheckBox);
                                            checkBoxPresent.setText("Re Admission");
                                            checkBoxPresent.setTextSize(14);
                                            checkBoxPresent.setPadding(8, 8, 8, 8);
                                            tableRow.addView(checkBoxPresent);
                                            tableLayout.addView(tableRow);
                                            // Inside the search.setOnClickListener method, after setting the onCheckedChangeListener for checkBoxPresent

                                            checkBoxPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (attendance1.containsKey(ROLLNO) && attendance1.get(ROLLNO)) {
                                                        // Attendance is already true, make the checkbox checked and unclickable
                                                        checkBoxPresent.setChecked(true);
                                                        checkBoxPresent.setEnabled(false);
                                                        attendance.put(ROLLNO, true);
                                                    } else {
                                                        attendance.put(ROLLNO, isChecked);
                                                        checkBoxPresent.setChecked(isChecked);
                                                        checkBoxPresent.setEnabled(true);

                                                        // Update the nsoAbsent value to zero in the database
                                                        if (isChecked) {
                                                            DatabaseReference studentRef = reference.child(ROLLNO);
                                                            studentRef.child("nsoAbsent").setValue(0);
                                                        }
                                                    }
                                                }
                                            });
                                        }

                                        progressDialog.dismiss();
                                    }
                                    if (!isDataFound) {
                                        Toast.makeText(PrincipalNsoStudent.this, "Data not found", Toast.LENGTH_SHORT).show();
                                        cardView.setVisibility(View.GONE);
                                        save.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(PrincipalNsoStudent.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PrincipalNsoStudent.this, "Student data not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                                save.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(PrincipalNsoStudent.this);
                progressDialog.setMessage("Saving Data...");
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setGravity(Gravity.CENTER);
                progressDialog.setIndeterminate(false);
                progressDialog.show();

                // Iterate over the tableLayout to get the checkbox states and populate the attendance HashMap
                for (int i = 1; i < tableLayout.getChildCount(); i++) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    String rollNo = ((TextView) row.getChildAt(0)).getText().toString();
                    CheckBox checkBox = (CheckBox) row.getChildAt(row.getChildCount() - 1);
                    boolean isChecked = checkBox.isChecked();
                    attendance.put(rollNo, isChecked);
                }

                // Iterate over the attendance HashMap and update the nsoAbsent values in the database
                for (String rollNo : attendance.keySet()) {
                    boolean isChecked = attendance.get(rollNo);
                    if (isChecked) {
                        DatabaseReference studentRef = reference.child(rollNo);
                        studentRef.child("nsoAbsent").setValue(0);
                    }
                }

                Toast.makeText(PrincipalNsoStudent.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
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