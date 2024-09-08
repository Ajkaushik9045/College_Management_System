package com.example.myapplication;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateResult extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView Sname, Sid, SBranch, Ssemester;
    private Button DELETE, searchs;
    EditText UserID, MARK;
    ArrayList<String> subjectList = new ArrayList<>();
    String ID, NAME, BRANCH, StuID, StuSemester, uniqueId, Subject, Sessional,subjectDetails;
    Spinner SESSIONAL, SUBJECT;
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
    DatabaseReference databaseStudent2 = FirebaseDatabase.getInstance().getReference("BRANCH");
    ConstraintLayout constraintLayout;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    CardView cardView;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_result);
        UserID = findViewById(R.id.userID);
        searchs = findViewById(R.id.searchs);
        constraintLayout = findViewById(R.id.constraintLayout3);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
        SESSIONAL = findViewById(R.id.Sessionalspinner);
        SUBJECT = findViewById(R.id.Markspinner);

        DELETE = findViewById(R.id.SaveButton);
        Sname = findViewById(R.id.NameField);
        Sid = findViewById(R.id.IDField);
        SBranch = findViewById(R.id.BranchField);
        Ssemester = findViewById(R.id.SemesterField);
        MARK = findViewById(R.id.Marksss);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.select_Session, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SESSIONAL.setAdapter(adapter3);
        SESSIONAL.setOnItemSelectedListener(this);
        SUBJECT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subjectInfo = adapterView.getItemAtPosition(i).toString();
                String[] parts = subjectInfo.split(" - ");
                Subject = parts[0]; //
                Subject=Subject.toUpperCase();
                subjectDetails = parts[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SESSIONAL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Sessional = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = UserID.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(UpdateResult.this, "Enter Roll no.", Toast.LENGTH_SHORT).show();
                } else {


                    databaseStudent.orderByChild("roll").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean TeacherFound = false;
                            if (snapshot.exists()) {
                                cardView.setVisibility(View.VISIBLE);
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    NAME = dsp.child("name").getValue().toString();
                                    BRANCH = dsp.child("branch").getValue().toString();
                                    StuID = dsp.child("roll").getValue().toString();
                                    StuSemester = dsp.child("semester").getValue().toString();
                                }
                                if (StuID.equals(ID)) {
                                    Sname.setText(NAME);
                                    Sid.setText(StuID);
                                    SBranch.setText(BRANCH);
                                    Ssemester.setText(StuSemester);
                                    TeacherFound = true;
                                    databaseStudent2.child(BRANCH).child(StuSemester).child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            subjectList.clear();
                                            boolean subjectFound = false;
                                            for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                                String subjectCode = subjectSnapshot.child("Code").getValue(String.class);
                                                String subjectName = subjectSnapshot.child("Subject").getValue(String.class);

//                                if (subjectCode == null && subjectName == null) {
//                                    subjectFound = false;
//                                }else{
                                                String subjectInfo = subjectCode + " - " + subjectName;
                                                subjectList.add(subjectInfo);
                                                subjectFound = true;
//                                }
                                            }
                                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(UpdateResult.this, android.R.layout.simple_spinner_item, subjectList);
                                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            SUBJECT.setAdapter(adapter4);

                                            if (!subjectFound) {
                                                Toast.makeText(UpdateResult.this, "Subject not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                if (TeacherFound) {
                                    // HOD found
                                } else {
                                    Toast.makeText(UpdateResult.this, "Student not found", Toast.LENGTH_SHORT).show();
                                    cardView.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(UpdateResult.this, "Student not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UpdateResult.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String markText = MARK.getText().toString();
                if (!markText.isEmpty()) {
                    num = Integer.parseInt(markText);
                } else {
//                    markText=null;
                    num = 0;
                }
                if (Sessional.equals("Sessional")) {
                    Toast.makeText(UpdateResult.this, "Select Sessional  Number", Toast.LENGTH_SHORT).show();
                } else if (markText.isEmpty()) {
                    Toast.makeText(UpdateResult.this, "Enter the Marks", Toast.LENGTH_SHORT).show();
                } else {

                    databaseStudent.child(ID).child("marks").child(Sessional).child(Subject).setValue(num)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateResult.this, "Marks saved successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateResult.this, "Failed to save marks", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}