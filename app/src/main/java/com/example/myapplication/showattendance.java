package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
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
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class showattendance extends AppCompatActivity {
    public static String branch, semester, date;
    String  branch1,semester1;
    int presentCounter = 0;
    TextView BRANCH, DATE, SEMESTER,PRESENT,ABSENT;
    String NAME, ROLLNO, BRANCH2, SEMESTER2,date2;
    String Present,Absent;
    String attendanceText,total;
    Button download;
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    private TableLayout tableLayout;
    int totalattendance ;
    float percentage=0;
    private List<String> attendanceList;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showattendance);
        tableLayout = findViewById(R.id.Studentlist);
        BRANCH = findViewById(R.id.BranchNAME);
        DATE = findViewById(R.id.DateNAME);
        SEMESTER = findViewById(R.id.SemesterNO);
        download = findViewById(R.id.button3);

        // Retrieve the extras from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get the values using the keys
            branch1 = extras.getString("branch");
            semester1 = extras.getString("semester");
            String date1 = extras.getString("date");
            date2 = date1;

            BRANCH.setText("Branch - \n" + branch1);
            DATE.setText("Date - \n" + date1);
            SEMESTER.setText("Semester\n - " + semester1);
        }
        ProgressDialog progressDialog = new ProgressDialog(showattendance.this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setIndeterminate(false);
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
                        Toast.makeText(showattendance.this, "Semester not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(showattendance.this, "Branch not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    boolean isfound = false;
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        BRANCH2 = dsp.child("branch").getValue().toString();
                        SEMESTER2 = Objects.requireNonNull(dsp.child("semester").getValue()).toString();
                        NAME = Objects.requireNonNull(dsp.child("name").getValue()).toString();
                        ROLLNO = dsp.child("roll").getValue().toString();
                        Present = dsp.child("present").getValue().toString();
                        Absent = dsp.child("absent").getValue().toString();
                        try {
                            int P = Integer.parseInt(Present);
                            percentage = (float) P / totalattendance;
                            percentage = percentage * 100;
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            String formattedPercentage = decimalFormat.format(percentage);
                            if (BRANCH2.equals(branch1)) {
                                if (SEMESTER2.equals(semester1)) {
                                    isfound = true;


                                    TableRow row = new TableRow(showattendance.this);
                                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                    TextView ViewName = new TextView(showattendance.this);
                                    ViewName.setText(ROLLNO);
                                    ViewName.setTextSize(20);
                                    ViewName.setPadding(8, 8, 20, 8);
                                    row.addView(ViewName);

                                    TextView ViewID = new TextView(showattendance.this);
                                    ViewID.setText(NAME);
                                    ViewID.setTextSize(20);
                                    ViewID.setPadding(15, 8, 20, 8);
                                    row.addView(ViewID);

                                    TextView present1 = new TextView(showattendance.this);
                                    present1.setText(Present);
                                    present1.setTextSize(20);
                                    present1.setPadding(15, 8, 20, 8);
                                    row.addView(present1);

                                    TextView present2 = new TextView(showattendance.this);
                                    present2.setText(Absent);
                                    present2.setTextSize(20);
                                    present2.setPadding(15, 8, 20, 8);
                                    row.addView(present2);

                                    TextView absent1 = new TextView(showattendance.this);
                                    absent1.setText(formattedPercentage + "%");
                                    absent1.setTextSize(20);
                                    absent1.setPadding(15, 8, 20, 8);
                                    row.addView(absent1);


                                    String currentDate = date2;
                                    String[] dateParts = currentDate.split("/");
                                    String year = dateParts[2];
                                    String month = dateParts[1];
                                    String day = dateParts[0];

                                    String currentDate1 = date2;
                                    String[] dateParts1 = currentDate.split("/");
                                    int year1 = Integer.parseInt(dateParts[2]);
                                    int month1 = Integer.parseInt(dateParts[1]);
                                    int day1 = Integer.parseInt(dateParts[0]);


                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(year1, month1 - 1, day1);
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    presentCounter = 0;
                                    if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
                                        // Inside the loop where you handle attendance for seven periods
                                        for (int period = 1; period <= 7; period++) {
                                            final int currentPeriod = period;

                                            DatabaseReference periodRef = FirebaseDatabase.getInstance().getReference()
                                                    .child("Student")
                                                    .child(ROLLNO)
                                                    .child("attendance")
                                                    .child(year)
                                                    .child(month)
                                                    .child(day)
                                                    .child(String.valueOf(currentPeriod));

                                            periodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Boolean attendance = dataSnapshot.getValue(Boolean.class);

                                                    if (attendance != null && attendance) {
                                                        // Handle "Present" attendance
                                                        attendanceText = "P";
                                                        TextView periodView = new TextView(showattendance.this);
                                                        periodView.setText(attendanceText);
                                                        periodView.setTextSize(20);
                                                        periodView.setTextColor(Color.GREEN);
                                                        periodView.setPadding(15, 8, 20, 8);
                                                        row.addView(periodView);

                                                        // Increment the "Present" counter
                                                        presentCounter++;
                                                    } else if (attendance != null && !attendance) {
                                                        // Handle "Absent" attendance
                                                        attendanceText = "A";
                                                        TextView periodView = new TextView(showattendance.this);
                                                        periodView.setText(attendanceText);
                                                        periodView.setTextSize(20);
                                                        periodView.setTextColor(Color.RED);
                                                        periodView.setPadding(15, 8, 20, 8);
                                                        row.addView(periodView);
                                                    } else {
                                                        // Handle other cases
                                                        attendanceText = "";
                                                        TextView periodView = new TextView(showattendance.this);
                                                        periodView.setText(attendanceText);
                                                        periodView.setTextSize(20);
                                                        periodView.setTextColor(Color.RED);
                                                        periodView.setPadding(15, 8, 20, 8);
                                                        row.addView(periodView);
                                                    }

                                                    // Check if it's the last period (7th period)
                                                    if (currentPeriod == 7) {
                                                        // Create a TextView to display the "Present" count
                                                        TextView presentCountView = new TextView(showattendance.this);
                                                        presentCountView.setText(String.valueOf(presentCounter));
                                                        presentCountView.setTextSize(20);
                                                        presentCountView.setPadding(15, 8, 20, 8);
                                                        row.addView(presentCountView);
                                                        presentCounter=0;
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle onCancelled event if needed
                                                }
                                            });
                                        }

                                    } else {
                                        attendanceText = "H";  // Set attendance text to "H" for Saturday and Sunday
                                        for (int period = 1; period <= 7; period++) {
                                            final int currentPeriod = period;

                                            DatabaseReference periodRef = FirebaseDatabase.getInstance().getReference()
                                                    .child("Student")
                                                    .child(ROLLNO)
                                                    .child("attendance")
                                                    .child(year)
                                                    .child(month)
                                                    .child(day)
                                                    .child(String.valueOf(currentPeriod));

                                            periodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    TextView periodView = new TextView(showattendance.this);
                                                    periodView.setText(attendanceText);
                                                    periodView.setTextSize(14);
                                                    periodView.setPadding(15, 8, 20, 8);
                                                    row.addView(periodView);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle onCancelled event if needed
                                                }
                                            });
                                        }
                                    }

//

                                    tableLayout.addView(row);

                                }
                            }
                        }catch(Exception e){
                            Toast.makeText(showattendance.this, "No attendance found ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                        progressDialog.dismiss();
                    }
                    if (!isfound) {
                        progressDialog.dismiss();
                        Toast.makeText(showattendance.this, "Data not found ", Toast.LENGTH_SHORT).show();
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
                // Create a StringBuilder to store the text data
                StringBuilder data = new StringBuilder();

                // Add table headers to the data StringBuilder
                data.append("Roll No\t\tName\t\tPresent\t\tAbsent\t\tPeriod 1\t\tPeriod 2\t\tPeriod 3\t\tPeriod 4\t\tPeriod 5\t\tPeriod 6\t\tPeriod 7\n");

                // Iterate over the table rows
                int rowCount = tableLayout.getChildCount();
                for (int i = 1; i < rowCount; i++) { // Start from 1 to skip the header row
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    int cellCount = row.getChildCount();

                    // Iterate over the cells in each row
                    for (int j = 0; j < cellCount; j++) {
                        View view = row.getChildAt(j);

                        // Get the text from TextViews in the row
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            data.append(textView.getText().toString()).append("\t\t");
                        }
                    }
                    data.append("\n");
                }

                // Save the data to a text file
                saveDataToFile(data.toString());
            }
        });
    }
    private void saveDataToFile(String data) {
        try {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormatSymbols.getInstance().getShortMonths()[calendar.get(Calendar.MONTH)] +
                    calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR);

            // Define the file name with the current date
            String fileName = "attendance_" + currentDate + ".txt";

            // Get the external storage directory
            File externalDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            if (externalDir != null) {
                // Create the file in the external storage directory
                File file = new File(externalDir, fileName);

                // Create a FileWriter to write data to the file
                FileWriter writer = new FileWriter(file);
                writer.append(data);
                writer.flush();
                writer.close();

                // Show a success message to the user
                Toast.makeText(showattendance.this, "Saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                // External storage directory is null, show an error message
                Toast.makeText(showattendance.this, "External storage not available", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error message to the user
            Toast.makeText(showattendance.this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
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





