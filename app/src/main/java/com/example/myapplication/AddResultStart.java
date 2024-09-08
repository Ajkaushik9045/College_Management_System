package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class AddResultStart extends AppCompatActivity {
    public static final String BRANCH1 = "BRANCH", SEMESTER1 = "SEMESTER", SUBJECT1 = "SUBJECT", MARKS1 = "MARKS", SESSIONAL1 = "SESSIONAL";
    String Branch, Semester, Subject, Marks, Sessional, name, roll, sem;
    int mark;
    TextView MARKS, SESSIONAL, SEMESTER, SUBJECT, BRANCH;
    TableLayout tableLayout;
    Button save;
    HashMap<String, Integer> resultData = new HashMap<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");

    //    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result_start);
        BRANCH = findViewById(R.id.DateNAME);
        MARKS = findViewById(R.id.BranchNAME);
        SEMESTER = findViewById(R.id.TeacherProfile);
        SESSIONAL = findViewById(R.id.SemesterNO);
        SUBJECT = findViewById(R.id.periodNumber);
        tableLayout = findViewById(R.id.Studentlist);
        save = findViewById(R.id.button3);

        Intent intent = getIntent();
        Branch = intent.getStringExtra(BRANCH1);
        Semester = intent.getStringExtra(SEMESTER1);
        Subject = intent.getStringExtra(SUBJECT1);
        Marks = intent.getStringExtra(MARKS1);
        Sessional = intent.getStringExtra(SESSIONAL1);
        BRANCH.setText("Branch \n" + Branch);
        SEMESTER.setText("Semester\n" + Semester);
        SUBJECT.setText("Subject: " + Subject);
        MARKS.setText("Marks\n" + Marks);
        SESSIONAL.setText("Sessional\n" + Sessional);
        String[] parts = Subject.split(" - ");
        Subject = parts[0];
        String subjectDetails = parts[1];
        Subject=Subject.toUpperCase();
        mark = Integer.parseInt(Marks);
        reference.orderByChild("branch").equalTo(Branch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    name = dsp.child("name").getValue(String.class);
                    roll = dsp.child("roll").getValue(String.class);
                    sem = dsp.child("semester").getValue(String.class);
                    String[] words = name.split("\\s");
                    String names = words[0];
                    if (sem.equals(Semester)) {
                        TableRow row = new TableRow(AddResultStart.this);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView viewName = new TextView(AddResultStart.this);
                        viewName.setText(roll);
                        viewName.setTextSize(20);
                        viewName.setPadding(8, 8, 20, 8);
                        row.addView(viewName);

                        TextView viewID = new TextView(AddResultStart.this);
                        viewID.setText(names);
                        viewID.setTextSize(14);
                        viewID.setPadding(15, 8, 20, 8);
                        row.addView(viewID);

                        EditText viewIDs = new EditText(AddResultStart.this);
                        viewIDs.setText("");
                        viewIDs.setInputType(InputType.TYPE_CLASS_NUMBER);
                        viewIDs.setTextSize(14);
                        viewIDs.setPadding(15, 8, 20, 8);
                        row.addView(viewIDs);
                        tableLayout.addView(row);
                        String marksInput = viewIDs.getText().toString().trim();
                        int marks = 0;
                        if (!marksInput.isEmpty()) {
                            marks = Integer.parseInt(marksInput);
                        }
                        int maxMarks = Integer.parseInt(Marks);
                        if (marks > maxMarks) {
                            viewIDs.setText(String.valueOf(maxMarks));
                            marks = maxMarks;
                        }

                        resultData.put(roll, marks);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference resultReference = FirebaseDatabase.getInstance().getReference().child("Student");
                resultReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String rollNumber : resultData.keySet()) {
                            int marks = resultData.get(rollNumber);

                            // Save the roll number and marks to the "Result" node
                            DatabaseReference rollReference = resultReference.child(rollNumber);
                            rollReference.child("marks").child(Sessional).child(Subject).setValue(marks);
                        }

                        // Show a success message or perform any other required actions
                        Toast.makeText(AddResultStart.this, "Result saved successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
            }
        });


    }
}