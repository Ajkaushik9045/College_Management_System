package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MonthAttendanceShow extends AppCompatActivity {
    public static String branch, semester, date, LIMIT;
    String branch1, semester1;
    String attendanceText = "1";
    TextView BRANCH, DATE, SEMESTER;
    String NAME, ROLLNO, BRANCH2, SEMESTER2, date2,total;
    Button download;
    String formattedPercentage;
    int totalattendance=1 ;
    private Map<String, Object> dataMap;
    boolean dataFound = false, isAttendanceFound = false;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    private TableLayout tableLayout;
    int TotalAttendance = 0;
    int limit = 0;
    float percentage=0;
    String PRESENT, ABSENT;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_attendance_show);
        tableLayout = findViewById(R.id.Studentlist);
        BRANCH = findViewById(R.id.BranchNAME);
        DATE = findViewById(R.id.DateNAME);
        SEMESTER = findViewById(R.id.SemesterNO);
        download = findViewById(R.id.button3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            branch1 = extras.getString("branch");
            semester1 = extras.getString("semester");
            String date1 = extras.getString("date");
            String[] dateParts = date1.split("/");
            String year = dateParts[2];
            String month = dateParts[1];
            String months = month + "/" + year;
            limit = getIntent().getIntExtra("LIMIT", 0);

            date2 = date1;

            BRANCH.setText("Branch - \n" + branch1);
            DATE.setText("Month - \n" + months);
            SEMESTER.setText("Semester -\n" + semester1);
        }
        ProgressDialog progressDialog = new ProgressDialog(MonthAttendanceShow.this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(branch1).exists()) {
                    DataSnapshot branchSnapshot = snapshot.child(branch1);
                    if (branchSnapshot.child(semester1).exists()) {
                        DataSnapshot semesterSnapshot = branchSnapshot.child(semester1);
                        if (semesterSnapshot.child("Total Attendance").exists()) {
                            total = semesterSnapshot.child("Total Attendance").getValue().toString();
                            totalattendance=Integer.parseInt(total);
                        } else {

                        }
                    } else {
                        Toast.makeText(MonthAttendanceShow.this, "Semester not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MonthAttendanceShow.this, "Branch not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
        reference.orderByChild("branch").equalTo(branch1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        BRANCH2 = dsp.child("branch").getValue().toString();
                        SEMESTER2 = Objects.requireNonNull(dsp.child("semester").getValue()).toString();
                        NAME = Objects.requireNonNull(dsp.child("name").getValue()).toString();
                        ROLLNO = dsp.child("roll").getValue().toString();
                        ABSENT = dsp.child("absent").getValue().toString();
                        PRESENT = dsp.child("present").getValue().toString();
                        int P = Integer.parseInt(PRESENT);
                        if(totalattendance!=0){
                        percentage = (float) P / totalattendance;
                        percentage = percentage * 100;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        formattedPercentage = decimalFormat.format(percentage);}
                        else{
                            percentage=0;
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            formattedPercentage = decimalFormat.format(percentage);
                        }
                        if (BRANCH2.equals(branch1) && SEMESTER2.equals(semester1)) {
                            dataFound = true;

                            TableRow studentRow = new TableRow(MonthAttendanceShow.this);
                            studentRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                            TextView ViewRollNo = new TextView(MonthAttendanceShow.this);
                            ViewRollNo.setText(ROLLNO);
                            ViewRollNo.setTextSize(20);
                            ViewRollNo.setPadding(8, 8, 20, 8);
                            studentRow.addView(ViewRollNo);

                            TextView ViewName = new TextView(MonthAttendanceShow.this);
                            ViewName.setText(NAME);
                            ViewName.setTextSize(20);
                            ViewName.setPadding(15, 8, 20, 8);
                            studentRow.addView(ViewName);

                            TextView ViewPresent = new TextView(MonthAttendanceShow.this);
                            ViewPresent.setText(PRESENT);
                            ViewPresent.setTextSize(20);
                            ViewPresent.setPadding(15, 8, 20, 8);
                            studentRow.addView(ViewPresent);

                            TextView totalattend = new TextView(MonthAttendanceShow.this);
                            totalattend.setText(String.valueOf(totalattendance));
                            totalattend.setTextSize(20);
                            totalattend.setPadding(15, 8, 20, 8);
                            studentRow.addView(totalattend);
//
//
                            TextView absent1 = new TextView(MonthAttendanceShow.this);
                            absent1.setText(formattedPercentage + "%");
                            absent1.setTextSize(20);
                            absent1.setPadding(15, 8, 20, 8);
                            studentRow.addView(absent1);

                            tableLayout.addView(studentRow);

                            String currentDate = date2;
                            String[] dateParts = currentDate.split("/");
                            String year = dateParts[2];
                            String month = dateParts[1];

                            int daysInMonth = getDaysInMonth(Integer.parseInt(year), Integer.parseInt(month));

                            for (int day = 1; day <= daysInMonth; day++) {
                                final int currentDay = day;

                                TableRow dateRow = new TableRow(MonthAttendanceShow.this);
                                dateRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                                TextView emptyView2 = new TextView(MonthAttendanceShow.this);
                                emptyView2.setText("");
                                emptyView2.setTextSize(14);
                                emptyView2.setPadding(15, 8, 20, 8);
                                dateRow.addView(emptyView2);

                                TextView emptyView3 = new TextView(MonthAttendanceShow.this);
                                emptyView3.setText("");
                                emptyView3.setTextSize(14);
                                emptyView3.setPadding(15, 8, 20, 8);
                                dateRow.addView(emptyView3);

                                TextView emptyView4 = new TextView(MonthAttendanceShow.this);
                                emptyView4.setText("");
                                emptyView4.setTextSize(14);
                                emptyView4.setPadding(15, 8, 20, 8);
                                dateRow.addView(emptyView4);

                                TextView emptyView = new TextView(MonthAttendanceShow.this);
                                emptyView.setText("");
                                emptyView.setTextSize(14);
                                emptyView.setPadding(15, 8, 20, 8);
                                dateRow.addView(emptyView);

                                TextView emptyView6 = new TextView(MonthAttendanceShow.this);
                                emptyView6.setText("");
                                emptyView6.setTextSize(14);
                                emptyView6.setPadding(15, 8, 20, 8);
                                dateRow.addView(emptyView6);

                                TextView ViewDate = new TextView(MonthAttendanceShow.this);
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
                                        final TextView periodView = new TextView(MonthAttendanceShow.this);
                                        periodView.setText(attendanceText);
                                        periodView.setTextSize(20);
                                        periodView.setPadding(15, 8, 20, 8);
                                        periodView.setTextColor(Color.BLUE);
                                        dateRow.addView(periodView);
                                    }
                                } else {
                                    for (int period = 1; period <= 7; period++) {
                                        final int currentPeriod = period;
                                        final TextView periodView = new TextView(MonthAttendanceShow.this);
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

                        }

                    }
                    progressDialog.dismiss();
                    if (!dataFound) {
                        progressDialog.dismiss();
                        Toast.makeText(MonthAttendanceShow.this, "Data not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDataToTextFile(BRANCH2,SEMESTER2);
            }
        });



    }

    private int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void exportDataToTextFile(String branchName, String semester) {
        try {
            StringBuilder data = new StringBuilder();

            // Append table headers
            TableRow headerRow = (TableRow) tableLayout.getChildAt(0);
            for (int i = 0; i < headerRow.getChildCount(); i++) {
                TextView textView = (TextView) headerRow.getChildAt(i);
                String header = textView.getText().toString();
                data.append(header).append("\t");
            }
            data.append("\n");

            // Append table data
            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                TableRow row = (TableRow) tableLayout.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    TextView textView = (TextView) row.getChildAt(j);
                    String cellData = textView.getText().toString();
                    data.append(cellData).append("\t");
                }
                data.append("\n");
            }

            // Create a text file
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
            String currentDateTime = dateFormat.format(new Date());
            String fileName = "Month_Attendance_ "+branchName + "_" + semester + "_" + currentDateTime + ".txt";
            File file = new File(getExternalFilesDir(null), fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(data.toString());
            writer.flush();
            writer.close();

            // Show a toast message with the file path
            Toast.makeText(MonthAttendanceShow.this, "Saved at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MonthAttendanceShow.this, "Error exporting data. Please try again.", Toast.LENGTH_SHORT).show();
        }
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
