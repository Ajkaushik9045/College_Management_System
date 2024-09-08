package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.Objects;

public class ManageAttendance extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    TextView DatePicker, DatePicker2;
    EditText rollNo;
    Button Search;
    String NAME, ROLLNO, BRANCH2, SEMESTER2, roll, name, attendanceText, PRESENT;
    private TableLayout tableLayout;
    String Date1 = "date", Date2 = "date";
    int TotalAttendance = 0, limit = 0;
    CardView cardView;
    ConstraintLayout constraintLayout;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    boolean isAttendanceFound = false, dataFound;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance);
        DatePicker = findViewById(R.id.RollFields);
        DatePicker2 = findViewById(R.id.RollField);
        rollNo = findViewById(R.id.editText);
        roll = rollNo.getText().toString();

        Search = findViewById(R.id.searchs);
        tableLayout = findViewById(R.id.Hodlist);
        constraintLayout = findViewById(R.id.constraintLayout6);
        constraintLayout.setVisibility(View.VISIBLE);
        cardView = findViewById(R.id.cardview5);
        cardView.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ManageAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Date1 = "01" + "/" + month + "/" + year;
                        if (month == 1) {
                            limit = 31;
                        } else if (month == 2 && year % 4 == 0) {
                            limit = 29;
                        } else if (month == 2 && year % 4 != 0) {
                            limit = 28;
                        } else if (month == 3) {
                            limit = 31;
                        } else if (month == 4) {
                            limit = 30;
                        } else if (month == 5) {
                            limit = 31;
                        } else if (month == 6) {
                            limit = 30;
                        } else if (month == 7) {
                            limit = 31;
                        } else if (month == 8) {
                            limit = 31;
                        } else if (month == 9) {
                            limit = 30;
                        } else if (month == 10) {
                            limit = 31;
                        } else if (month == 11) {
                            limit = 30;
                        } else if (month == 12) {
                            limit = 31;
                        }
                        DatePicker.setText(Date1);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        DatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ManageAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Date2 = day + "/" + month + "/" + year;
                        DatePicker2.setText(Date2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roll = rollNo.getText().toString();
                if (roll.isEmpty()) {
                    Toast.makeText(ManageAttendance.this, "Enter Roll No.", Toast.LENGTH_SHORT).show();
                    cardView.setVisibility(View.GONE);
                } else if (Date1.equals("date")) {
                    Toast.makeText(ManageAttendance.this, "Select Start Date", Toast.LENGTH_SHORT).show();
                    cardView.setVisibility(View.GONE);
                } else if (Date2.equals("date")) {
                    Toast.makeText(ManageAttendance.this, "Select End Date", Toast.LENGTH_SHORT).show();
                    cardView.setVisibility(View.GONE);
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(ManageAttendance.this);
                    progressDialog.setMessage("Loading Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();


                    reference.orderByChild("roll").equalTo(roll).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    BRANCH2 = dsp.child("branch").getValue().toString();
                                    SEMESTER2 = Objects.requireNonNull(dsp.child("semester").getValue()).toString();
                                    NAME = Objects.requireNonNull(dsp.child("name").getValue()).toString();
                                    ROLLNO = dsp.child("roll").getValue().toString();
                                    PRESENT = dsp.child("present").getValue().toString();

                                    if (ROLLNO.equals(roll)) {
                                        dataFound = true;
                                        cardView.setVisibility(View.VISIBLE);
                                        TableRow studentRow = new TableRow(ManageAttendance.this);
                                        studentRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                        TextView ViewRollNo = new TextView(ManageAttendance.this);
                                        ViewRollNo.setText(ROLLNO);
                                        ViewRollNo.setTextSize(20);
                                        ViewRollNo.setPadding(8, 8, 20, 8);
                                        studentRow.addView(ViewRollNo);

                                        TextView ViewName = new TextView(ManageAttendance.this);
                                        ViewName.setText(NAME);
                                        ViewName.setTextSize(20);
                                        ViewName.setPadding(15, 8, 20, 8);
                                        studentRow.addView(ViewName);

                                        TextView ViewPresent = new TextView(ManageAttendance.this);
                                        ViewPresent.setText(PRESENT);
                                        ViewPresent.setTextSize(20);
                                        ViewPresent.setPadding(15, 8, 20, 8);
                                        studentRow.addView(ViewPresent);


                                        tableLayout.addView(studentRow);

                                        String currentDate = Date1;
                                        String[] dateParts = currentDate.split("/");
                                        String year = dateParts[2];
                                        String month = dateParts[1];

                                        int currentmonth = Integer.parseInt(month);
                                        int currentyear = Integer.parseInt(year);
                                        String lastDate = Date2;
                                        String[] dateParts2 = lastDate.split("/");
                                        String lastyear = dateParts2[2];
                                        String lastmonth = dateParts2[1];
                                        int lastmonth2 = Integer.parseInt(lastmonth);
                                        int lastyear2 = Integer.parseInt(lastyear);

                                        while (!(currentyear == lastyear2 )&&(currentmonth == lastmonth2)) {
                                            int daysInMonth = getDaysInMonth(currentyear, currentmonth);

                                            for (int day = 1; day <= daysInMonth; day++) {
                                                final int currentDay = day;


                                                TableRow dateRow = new TableRow(ManageAttendance.this);
                                                dateRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                                                TextView emptyView2 = new TextView(ManageAttendance.this);
                                                emptyView2.setText("");
                                                emptyView2.setTextSize(14);
                                                emptyView2.setPadding(15, 8, 20, 8);
                                                dateRow.addView(emptyView2);

                                                TextView emptyView3 = new TextView(ManageAttendance.this);
                                                emptyView3.setText("");
                                                emptyView3.setTextSize(14);
                                                emptyView3.setPadding(15, 8, 20, 8);
                                                dateRow.addView(emptyView3);

                                                TextView emptyView4 = new TextView(ManageAttendance.this);
                                                emptyView4.setText("");
                                                emptyView4.setTextSize(14);
                                                emptyView4.setPadding(15, 8, 20, 8);
                                                dateRow.addView(emptyView4);


                                                TextView ViewDate = new TextView(ManageAttendance.this);
                                                ViewDate.setText(currentDay + "/" + month + "/" + year);
                                                ViewDate.setTextSize(20);
                                                ViewDate.setPadding(15, 8, 20, 8);
                                                dateRow.addView(ViewDate);

                                                String year1 = String.valueOf(year);
                                                String month1 = String.valueOf(month);
                                                String day1 = String.valueOf(currentDay);

                                                // Check if the current day is a Saturday or Sunday
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.set(Integer.parseInt(year1), Integer.parseInt(month1) - 1, Integer.parseInt(day1));
                                                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                                                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {

                                                    attendanceText = "H";
                                                    // Set attendance text to "H" for Saturday and Sunday
                                                    for (int period = 1; period <= 7; period++) {
                                                        final int currentPeriod = period;
                                                        final TextView periodView = new TextView(ManageAttendance.this);
                                                        periodView.setText(attendanceText);
                                                        periodView.setTextSize(20);
                                                        periodView.setPadding(15, 8, 20, 8);
                                                        periodView.setTextColor(Color.BLUE);
                                                        dateRow.addView(periodView);
                                                    }
                                                } else {
                                                    for (int period = 1; period <= 7; period++) {
                                                        final int currentPeriod = period;
                                                        final TextView periodView = new TextView(ManageAttendance.this);
                                                        periodView.setText(attendanceText);
                                                        periodView.setTextSize(20);
                                                        periodView.setPadding(15, 8, 20, 8);
                                                        DatabaseReference periodRef = FirebaseDatabase.getInstance().getReference()
                                                                .child("Student")
                                                                .child(ROLLNO)
                                                                .child("attendance")
                                                                .child(year1)
                                                                .child(month1)
                                                                .child(day1)
                                                                .child(String.valueOf(currentPeriod));

                                                        periodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Boolean attendance = dataSnapshot.getValue(Boolean.class);
                                                                if (attendance != null && attendance) {
                                                                    attendanceText = "P";
                                                                    periodView.setTextColor(Color.GREEN); // Set the color of the name text to red
                                                                    isAttendanceFound = true;
                                                                } else if (attendance != null && !attendance) {
                                                                    attendanceText = "A";
                                                                    periodView.setTextColor(Color.RED);
                                                                    isAttendanceFound = true;
                                                                } else {
                                                                    attendanceText = "";

                                                                }
                                                                periodView.setText(attendanceText); // Set the text for periodView
                                                                dateRow.addView(periodView);

                                                                if (currentPeriod == 7) {
                                                                    if (!isAttendanceFound) {
                                                                        dateRow.setVisibility(View.GONE); // Hide the date row if attendance is not found in any period
                                                                    }
                                                                    isAttendanceFound = false; // Reset the flag for the next date row
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                // Handle onCancelled event if needed
                                                            }
                                                        });
                                                    }
                                                }

                                                tableLayout.addView(dateRow);

                                            }
                                            if (currentmonth == 12) {
                                                currentmonth = 1;
                                                currentyear++;
                                            } else {
                                                currentmonth++;
                                            }
                                        }

                                    }

                                }
                                progressDialog.dismiss();
                                if (!dataFound) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ManageAttendance.this, "Data not found", Toast.LENGTH_SHORT).show();
//                                    finish();
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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



