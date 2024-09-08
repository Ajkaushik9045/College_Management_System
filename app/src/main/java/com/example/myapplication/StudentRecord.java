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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentRecord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner Branchlist;
    Spinner Semesterlist;
    TableLayout tableLayout;
    Button search, save;
    String NAME, ROLLNO, BRANCH, SEMESTER;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    String Branch, Semester;
    ConstraintLayout constraintLayout;
    CardView cardView;
    int nums = 1;
    int nsoAbsent = 0;
    String sno = "1";
    HashMap<String, Boolean> attendance = new HashMap<>();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    boolean isDataFound = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_record);
        Branchlist = findViewById(R.id.spinnerBranch);
        Semesterlist = findViewById(R.id.spinnerStud);
        search = findViewById(R.id.searchs);
        tableLayout = findViewById(R.id.Studentlist);
//        save = findViewById(R.id.search);
//        save.setVisibility(View.GONE);
        cardView = findViewById(R.id.cardView2);
        cardView.setVisibility(View.GONE);

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
                    Toast.makeText(StudentRecord.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    Toast.makeText(StudentRecord.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(StudentRecord.this);
                    progressDialog.setMessage("Loading Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    sno = "1";
                    nums=1;
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    reference.orderByChild("semester").equalTo(Semester).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                cardView.setVisibility(View.GONE);
//                                save.setVisibility(View.GONE);
                                try {
                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                        BRANCH = dsp.child("branch").getValue().toString();
                                        SEMESTER = dsp.child("semester").getValue().toString();
                                        NAME = dsp.child("name").getValue().toString();
                                        ROLLNO = dsp.child("roll").getValue().toString();
                                        nsoAbsent = dsp.child("nsoAbsent").getValue(Integer.class);
                                        if (BRANCH.equals(Branch) && SEMESTER.equals(Semester)) {
                                            isDataFound = true;
//                                            save.setVisibility(View.VISIBLE);
                                            cardView.setVisibility(View.VISIBLE);
                                            TableRow tableRow = new TableRow(StudentRecord.this);
                                            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                            TextView snumber = new TextView(StudentRecord.this);
                                            snumber.setText(String.valueOf(sno));
                                            snumber.setPadding(8, 8, 8, 8);
                                            tableRow.addView(snumber);

                                            TextView ViewName = new TextView(StudentRecord.this);
                                            ViewName.setText(ROLLNO);
                                            ViewName.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewName);


                                            TextView ViewID = new TextView(StudentRecord.this);
                                            ViewID.setText(NAME);
                                            ViewID.setPadding(8, 8, 8, 8);
                                            tableRow.addView(ViewID);

//                                            TextView ViewBranch = new TextView(StudentRecord.this);
//                                            ViewBranch.setText(BRANCH);
//                                            ViewBranch.setPadding(8, 8, 8, 8);
//                                            tableRow.addView(ViewBranch);
//
//                                            TextView ViewPassword = new TextView(StudentRecord.this);
//                                            ViewPassword.setText(SEMESTER);
//                                            ViewPassword.setPadding(8, 8, 8, 8);
//                                            tableRow.addView(ViewPassword);

//                                            CheckBox checkBoxPresent = new CheckBox(StudentRecord.this);
//                                            checkBoxPresent.setId(R.id.attendanceCheckBox);
//                                            checkBoxPresent.setText("NSO");
//                                            checkBoxPresent.setTextSize(14);
//                                            checkBoxPresent.setPadding(8, 8, 8, 8);
//                                            tableRow.addView(checkBoxPresent);
//                                            if (nsoAbsent > 97) {
//                                                checkBoxPresent.setChecked(true);
//                                                checkBoxPresent.setEnabled(false);
//                                            } else {
//                                                checkBoxPresent.setEnabled(true);
//                                                checkBoxPresent.setChecked(false);
//                                            }

                                            nums++;
                                            sno = String.valueOf(nums);
                                            tableLayout.addView(tableRow);
                                        }
                                        progressDialog.dismiss();
                                    }
                                    if (!isDataFound) {
                                        Toast.makeText(StudentRecord.this, "Data not found", Toast.LENGTH_SHORT).show();
                                        cardView.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    cardView.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(StudentRecord.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StudentRecord.this, "Student data not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                                progressDialog.dismiss();
//                                save.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ProgressDialog progressDialog = new ProgressDialog(StudentRecord.this);
//                progressDialog.setMessage("Saving Data...");
//                progressDialog.setCancelable(false);
//                progressDialog.getWindow().setGravity(Gravity.CENTER);
//                progressDialog.setIndeterminate(false);
//                progressDialog.show();
//
//                // Iterate over the tableLayout to get the checkbox states and populate the attendance HashMap
//                for (int i = 1; i < tableLayout.getChildCount(); i++) {
//                    TableRow row = (TableRow) tableLayout.getChildAt(i);
//                    String rollNo = ((TextView) row.getChildAt(1)).getText().toString(); // Assuming roll number is at index 1
//                    CheckBox checkBox = (CheckBox) row.getChildAt(row.getChildCount() - 1);
//                    boolean isChecked = checkBox.isChecked();
//                    attendance.put(rollNo, isChecked);
//                }
//
//                // Iterate over the attendance HashMap and update the nsoAbsent values in the database
//                for (String rollNo : attendance.keySet()) {
//                    boolean isChecked = attendance.get(rollNo);
//                    if (isChecked) {
//                        DatabaseReference studentRef = reference.child(rollNo);
//                        studentRef.child("nsoAbsent").setValue(98); // Set nsoAbsent to 1 for the selected roll number
//                    }
//                }
//
//                Toast.makeText(StudentRecord.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                finish();
//            }
//        });


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