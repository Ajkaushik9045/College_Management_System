package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class StartAttendance extends AppCompatActivity {
    public static String DATE1 = "DATE";
    public static String SEMESTER1 = "SEMESTER";
    public static String PERIOD1 = "PERIOD";
    public static String BRANCH1 = "BRANCH";
    public static final String NAMES = "NAME";
    public static final String past1 = "Period";
    private TextView TEACHER, PERIOD, SEMESTER, DATE;
    int totalattendance = 0;
    boolean exists = false;


    HashMap<String, Boolean> attendance = new HashMap<>(); // Create a HashMap to store attendance
    HashMap<String, Boolean> attendance1 = new HashMap<>();
    String NAME, ROLLNO, BRANCH2, SEMESTER2;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    private TableLayout tableLayout;
    String year, month, day;
    int Tpresent;
    String total = "nulls";
    int nso;
    private ProgressDialog progressDialog1;
    String previousPeriod;
    String semester, period, branch, name, date, previous1;
    boolean datanotfound = false;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_attendance);

        tableLayout = findViewById(R.id.Studentlist);
        TextView BRANCH = findViewById(R.id.BranchNAME);
        TEACHER = findViewById(R.id.TeacherProfile);
        SEMESTER = findViewById(R.id.SemesterNO);
        PERIOD = findViewById(R.id.periodNumber);
        DATE = findViewById(R.id.DateNAME);


        Intent intent = getIntent();
        date = intent.getStringExtra(DATE1);
        branch = intent.getStringExtra(BRANCH1);
        period = intent.getStringExtra(PERIOD1);
        semester = intent.getStringExtra(SEMESTER1);
        name = intent.getStringExtra(NAMES);
        previous1 = intent.getStringExtra(past1);
        previousPeriod = previous1;


        TEACHER.setText("Teacher - \n" + name);
        BRANCH.setText("Branch - \n" + branch);
        PERIOD.setText("Period - \n" + period);
        DATE.setText("Date - \n" + date);
        SEMESTER.setText("Semester\n - " + semester);
        ProgressDialog progressDialog = new ProgressDialog(StartAttendance.this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        String[] dateParts = date.split("/");
        String year2 = dateParts[2];
        String month2 = dateParts[1];
        String day2 = dateParts[0];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year2), Integer.parseInt(month2) - 1, Integer.parseInt(day2));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            finish();
            progressDialog.dismiss();
            Toast.makeText(this, "Saturday Holiday", Toast.LENGTH_SHORT).show();
            return;
        } else if (dayOfWeek == Calendar.SUNDAY) {
            progressDialog.dismiss();
            finish();
            Toast.makeText(this, "Sunday Holiday", Toast.LENGTH_SHORT).show();
            return;
        } else {
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(branch).exists()) {
                        DataSnapshot branchSnapshot = snapshot.child(branch);
                        if (branchSnapshot.child(semester).exists()) {
                            DataSnapshot semesterSnapshot = branchSnapshot.child(semester);
                            if (semesterSnapshot.child("Total Attendance").exists()) {
                                String currentDate = date;
                                String[] dateParts = currentDate.split("/");
                                year = dateParts[2];
                                month = dateParts[1];
                                day = dateParts[0];
                                total = semesterSnapshot.child("Total Attendance").getValue().toString();
                                DatabaseReference attendanceRef1 = semesterSnapshot.child("date").child(year).child(month).child(day).child(period).getRef();
                                attendanceRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Boolean isPresent = dataSnapshot.getValue(Boolean.class);
                                        if (isPresent != null) {

                                            exists = isPresent;

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle onCancelled event
                                    }
                                });
                                totalattendance = Integer.parseInt(total);
                            } else {
                                // Handle the case when "Total Attendance" doesn't exist
                            }
                        } else {
                            // Handle the case when the semester doesn't exist
                        }
                    } else {
                        // Handle the case when the branch doesn't exist
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });

            reference.orderByChild("branch").equalTo(branch).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            BRANCH2 = Objects.requireNonNull(dsp.child("branch").getValue()).toString();
                            SEMESTER2 = Objects.requireNonNull(dsp.child("semester").getValue()).toString();
                            NAME = Objects.requireNonNull(dsp.child("name").getValue()).toString();
                            ROLLNO = Objects.requireNonNull(dsp.child("roll").getValue()).toString();
                            nso = dsp.child("nsoAbsent").getValue(Integer.class);
                            Tpresent = dsp.child("present").getValue(Integer.class);
                            String[] words = NAME.split("\\s");
                            String name = words[0];
                            if (BRANCH2.equals(branch) && SEMESTER2.equals(semester)) {
                                datanotfound = true;
                                TableRow row = new TableRow(StartAttendance.this);
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView viewName = new TextView(StartAttendance.this);
                                viewName.setText(ROLLNO);
                                viewName.setTextSize(20);
                                viewName.setPadding(8, 8, 20, 8);
                                row.addView(viewName);

                                TextView viewID = new TextView(StartAttendance.this);
                                viewID.setText(name);
                                viewID.setTextSize(14);
                                viewID.setPadding(15, 8, 20, 8);
                                row.addView(viewID);

                                final String rollNo = ROLLNO; // Declare a final variable capturing the current student's roll number

                                CheckBox checkBoxPresent = new CheckBox(StartAttendance.this);
                                checkBoxPresent.setId(R.id.attendanceCheckBox);
                                checkBoxPresent.setText("Present");
                                checkBoxPresent.setTextSize(14);
                                checkBoxPresent.setPadding(8, 8, 8, 8);
                                row.addView(checkBoxPresent);

                                if (nso >= 98) {
                                    checkBoxPresent.setTextColor(Color.RED);
//                                    setRowColor(row, Color.RED);

                                    viewName.setTextColor(Color.RED); // Set the color of the name text to red
                                    viewID.setTextColor(Color.RED);
                                    checkBoxPresent.setEnabled(false);  // Disable the checkbox
                                }
                                // Get attendance date
                                String currentDate = date;
                                String[] dateParts = currentDate.split("/");
                                year = dateParts[2];
                                month = dateParts[1];
                                day = dateParts[0];

                                DatabaseReference attendanceRef1 = dsp.child("attendance").child(year).child(month).child(day).child(period).getRef();
                                attendanceRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Boolean isPresent = dataSnapshot.getValue(Boolean.class);
                                        if (isPresent != null && isPresent == true) {

                                            attendance1.put(rollNo, isPresent);

                                        } else if (isPresent != null) {
                                            attendance1.put(rollNo, Boolean.FALSE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle onCancelled event
                                    }
                                });
                                progressDialog.dismiss();

                                // Calculate the difference in days between the chosen date and the current date
                                long differenceInDays = calculateDifferenceInDays(year, month, day);

                                // Only retrieve attendance data from Firebase if the chosen date is within the range of 3 days from the current date
                                if (differenceInDays <= 1 && differenceInDays >= -1) {
                                    // Retrieve the attendance for the current student
                                    DatabaseReference attendanceRef = dsp.child("attendance").child(year).child(month).child(day).child(previousPeriod).getRef();
                                    attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Boolean isPresent = dataSnapshot.getValue(Boolean.class);
                                            if (isPresent != null) {
                                                if (!previous1.equals(period)) {
                                                    if (attendance1.containsKey(rollNo) && Boolean.TRUE.equals(attendance1.get(rollNo))) {
                                                        // Attendance is already true, make the checkbox checked and unclickable
                                                        checkBoxPresent.setChecked(true);
                                                        attendance.put(rollNo, true);
                                                        checkBoxPresent.setEnabled(false);
                                                    } else {
                                                        checkBoxPresent.setChecked(isPresent);
                                                        attendance.put(rollNo, isPresent);
                                                    }
                                                } else {
                                                    checkBoxPresent.setChecked(isPresent);
                                                }
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle onCancelled event
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(StartAttendance.this, "Choose a date within 2 days of the current day.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }


                                checkBoxPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (attendance1.containsKey(rollNo) && Boolean.TRUE.equals(attendance1.get(rollNo))) {
                                            // Attendance is already true, make the checkbox checked and unclickable
                                            checkBoxPresent.setChecked(true);
                                            checkBoxPresent.setEnabled(false);
                                            attendance.put(rollNo, true);
                                        } else {
                                            attendance.put(rollNo, isChecked);
                                            checkBoxPresent.setChecked(isChecked);
                                            checkBoxPresent.setEnabled(true);
                                        }
                                    }
                                });
                                tableLayout.addView(row);
                            } else {
                                datanotfound = false;

                                progressDialog.dismiss();

                            }
                        }
                        if (datanotfound == false) {
                            Toast.makeText(StartAttendance.this, "Data not found", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }

                    } catch (Exception e) {


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event
                }

            });
        }
// Method to calculate the difference in days between two dates
        Button saveAttendanceButton = findViewById(R.id.button3);
        saveAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(StartAttendance.this);
                builder.setTitle("Confirm Attendance");
                builder.setMessage("Are you sure you want to save the attendance?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog1 = new ProgressDialog(StartAttendance.this);
                        progressDialog1.setMessage("Saving attendance...");
                        progressDialog1.setCancelable(false);
                        progressDialog1.getWindow().setGravity(Gravity.CENTER);
                        progressDialog1.setIndeterminate(false);
                        progressDialog1.show();

                        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Student");
                        DatabaseReference studentRef2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
                        String currentDate = getIntent().getStringExtra(DATE1);
                        String period = getIntent().getStringExtra(PERIOD1);

                        if (currentDate != null && period != null) {
                            studentRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        String[] dateParts = currentDate.split("/");
                                        String year = dateParts[2];
                                        String month = dateParts[1];
                                        String day = dateParts[0];
                                        if (exists == true) {
                                            studentRef2.child(branch).child(semester).child("Total Attendance").setValue(totalattendance);

                                        } else {
                                            exists = true;
                                            totalattendance = totalattendance + 1;
                                            studentRef2.child(branch).child(semester).child("Total Attendance").setValue(totalattendance);
                                            studentRef2.child(branch).child(semester).child("date").child(year).child(month).child(day).child(period).setValue(Boolean.TRUE);

                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {

                                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                            String studentKey = studentSnapshot.getKey();
                                            boolean isPresent = attendance.get(studentKey) != null && Boolean.TRUE.equals(attendance.get(studentKey));

                                            String x = Objects.requireNonNull(studentSnapshot.child("present").getValue()).toString();
                                            String y = Objects.requireNonNull(studentSnapshot.child("absent").getValue()).toString();
                                            String z = Objects.requireNonNull(studentSnapshot.child("nsoAbsent").getValue()).toString();
                                            int newTPresent = Integer.parseInt(x);
                                            int newTAbsent = Integer.parseInt(y);
                                            int newnsoAbsent = Integer.parseInt(z);

                                            if (attendance1.containsKey(studentKey) && Boolean.TRUE.equals(attendance1.get(studentKey))) {
                                                newTPresent--;
                                                newnsoAbsent--;
                                            }
                                            if (attendance1.containsKey(studentKey) && Boolean.FALSE.equals(attendance1.get(studentKey))) {
                                                newTAbsent--;
                                                newnsoAbsent--;
                                            }

                                            if (isPresent) {
                                                newTPresent++;
                                                newnsoAbsent = 0;
                                            } else {
                                                newTAbsent++;
                                                newnsoAbsent++;
                                            }

                                            studentSnapshot.getRef().child("present").setValue(newTPresent);
                                            studentSnapshot.getRef().child("absent").setValue(newTAbsent);
                                            studentSnapshot.getRef().child("nsoAbsent").setValue(newnsoAbsent);
                                            String[] dateParts = currentDate.split("/");
                                            String year = dateParts[2];
                                            String month = dateParts[1];
                                            String day = dateParts[0];

                                            studentSnapshot.getRef().child("attendance").child(year).child(month).child(day).child(period).setValue(isPresent);

                                        }

                                        Toast.makeText(StartAttendance.this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Toast.makeText(StartAttendance.this, "Failed to save attendance", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle onCancelled event
                                    Toast.makeText(StartAttendance.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(StartAttendance.this, "Invalid date or period", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


// ...

    private long calculateDifferenceInDays(String year, String month, String day) {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1; // Note: Calendar month starts from 0
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // Convert the date parts to integers
        int selectedYear = Integer.parseInt(year);
        int selectedMonth = Integer.parseInt(month);
        int selectedDay = Integer.parseInt(day);

        // Calculate the difference in days
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth - 1, selectedDay); // Note: Calendar month starts from 0
        long diffInMillis = selectedDate.getTimeInMillis() - currentDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog1 != null && progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }
    }

    private void setRowColor(TableRow row, int color) {
        row.setBackgroundColor(color);
    }


}
