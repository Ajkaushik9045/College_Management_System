package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ResultRecordShow extends AppCompatActivity {
    public static final String BRANCH1 = "BRANCH", SEMESTER1 = "SEMESTER", SESSIONAL1 = "SESSIONAL";
    TextView Branch,Semester,Sessional;
    String branch,semester,sessional,roll,name,sem;
    TableLayout tableLayout;
    ArrayList<String> subjectList = new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference databaseStudent2 = FirebaseDatabase.getInstance().getReference("BRANCH");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_record_show);
        Branch=findViewById(R.id.DateNAME);
        Semester=findViewById(R.id.SemesterNO);
        Sessional=findViewById(R.id.BranchNAME);
        tableLayout = findViewById(R.id.Studentlist);
        Intent intent= getIntent();
        branch=intent.getStringExtra(BRANCH1);
        semester=intent.getStringExtra(SEMESTER1);
        sessional=intent.getStringExtra(SESSIONAL1);
        Branch.setText("Branch \n"+branch);
        Semester.setText("Semester \n"+semester);
        Sessional.setText("Sessional No. \n"+sessional);
        databaseStudent2.child(branch).child(semester).child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectList.clear();
                TableRow headerRow = new TableRow(ResultRecordShow.this);
                headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView viewName = new TextView(ResultRecordShow.this);
                viewName.setText("Roll No");
                viewName.setTextSize(20);
                viewName.setTextColor(Color.BLACK);
                viewName.setTypeface(null, Typeface.BOLD);
                viewName.setPadding(8, 8, 20, 8);
                headerRow.addView(viewName);

                TextView viewID = new TextView(ResultRecordShow.this);
                viewID.setText("Name");
                viewID.setTextColor(Color.BLACK);
                viewID.setTypeface(null, Typeface.BOLD);
                viewID.setTextSize(20);
                viewID.setPadding(15, 8, 20, 8);
                headerRow.addView(viewID);

                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    String subjectCode = subjectSnapshot.child("Code").getValue(String.class);
                    String subjectName = subjectSnapshot.child("Subject").getValue(String.class);

                    String subjectInfo = subjectCode + " - " + subjectName;
                    subjectList.add(subjectInfo);

                    TextView columnHeader = new TextView(ResultRecordShow.this);
                    columnHeader.setText(subjectName); // Set subject name as column header
                    columnHeader.setTextSize(20);
                    columnHeader.setTextColor(Color.BLACK);
                    columnHeader.setTypeface(null, Typeface.BOLD);
                    columnHeader.setPadding(15, 8, 20, 8);
                    headerRow.addView(columnHeader);
                }

                tableLayout.addView(headerRow);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancellation
            }
        });

        reference.orderByChild("branch").equalTo(branch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout.removeViews(0, tableLayout.getChildCount() - 1);

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    roll = dsp.child("roll").getValue().toString();
                    name = dsp.child("name").getValue().toString();
                    sem = dsp.child("semester").getValue().toString();
                    if (sem.equals(semester)) {
                        TableRow row = new TableRow(ResultRecordShow.this);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView viewName = new TextView(ResultRecordShow.this);
                        viewName.setText(roll);
                        viewName.setTextSize(20);
                        viewName.setPadding(8, 8, 20, 8);
                        row.addView(viewName);

                        TextView viewID = new TextView(ResultRecordShow.this);
                        viewID.setText(name);
                        viewID.setTextSize(14);
                        viewID.setPadding(15, 8, 20, 8);
                        row.addView(viewID);

                        DataSnapshot marksNode = dsp.child("marks");
                        for (DataSnapshot marksChild : marksNode.getChildren()) {
                            String subjectCode = marksChild.getKey(); // Get the subject code
                            Object marksValueObject = marksChild.getValue(); // Get the marks value as an object

                            String marksValue = String.valueOf(marksValueObject); // Convert marks value to a string

                            // Check if marksValue is a map
                            if (marksValueObject instanceof Map) {
                                Map<String, Object> marksMap = (Map<String, Object>) marksValueObject;
                                marksValue = String.valueOf(marksMap.getOrDefault(subjectCode, 0));
                            }

                            TextView marksView = new TextView(ResultRecordShow.this);
                            marksView.setText(String.valueOf(marksValue));
                            marksView.setTextSize(14);
                            marksView.setPadding(15, 8, 20, 8);
                            row.addView(marksView);
                        }

                        tableLayout.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancellation
            }
        });

    }
}