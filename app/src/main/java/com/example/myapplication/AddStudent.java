package com.example.myapplication;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText STUDENTROLL, STUDENTNAME;

    private Button SAVE;
    private Spinner SPINNER1, SPINNER2;
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");

    String Name,  Roll,  Semester, Branch;
    int Present = 0, Absent = 0,total_attendance=0 ,nsoAbsent;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        SAVE = findViewById(R.id.SaveButton);
        STUDENTNAME = findViewById(R.id.EditStudentNameField);
        STUDENTROLL = findViewById(R.id.EditStudentRollField);
        SPINNER1 = findViewById(R.id.Branchspinner);
        SPINNER2 = findViewById(R.id.Semesterspinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER1.setAdapter(adapter);
        SPINNER1.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.select_Semester, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER2.setAdapter(adapter1);
        SPINNER2.setOnItemSelectedListener(this);

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
                Semester = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedString = STUDENTNAME.getText().toString();
                Roll = STUDENTROLL.getText().toString();


                String[] words = formattedString.split("\\s");
                StringBuilder result = new StringBuilder();

                for (String word : words) {
                    if (word.length() > 0) {
                        String formattedWord = formatWord(word);
                        result.append(formattedWord).append(" ");
                    }
                }

                Name = result.toString().trim();

                result.setLength(0);  // Reset the StringBuilder for reuse

                String[] branchWords = Branch.split("\\s");

                for (String word : branchWords) {
                    if (word.length() > 0) {
                        String formattedWord = formatWord(word);
                        result.append(formattedWord).append(" ");
                    }
                }
                Branch = result.toString().trim();

                if (!TextUtils.isEmpty(STUDENTROLL.getText().toString())) {
                    if (Semester.equals("Sem")) {
                        Toast.makeText(AddStudent.this, "Select Semester", Toast.LENGTH_SHORT).show();
                    } else if (Branch.equals("Branch")) {
                        Toast.makeText(AddStudent.this, "Select Branch", Toast.LENGTH_SHORT).show();
                    } else {
                        final Students hod = new Students(Name, Roll, Branch, Semester, Present, Absent,nsoAbsent);
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(Branch).exists()) {
                                    DataSnapshot branchSnapshot = snapshot.child(Branch);
                                    if (branchSnapshot.child(Semester).exists()) {
                                        DataSnapshot semesterSnapshot = branchSnapshot.child(Semester);
                                        if (semesterSnapshot.child("Total Attendance").exists()) {

                                        } else {

                                            semesterSnapshot.getRef().child("Total Attendance").setValue(total_attendance);
                                        }
                                    } else {
                                        branchSnapshot.getRef().child(Semester).child("Total Attendance").setValue(total_attendance);
                                    }
                                } else {
                                    snapshot.getRef().child(Branch).child(Semester).child("Total Attendance").setValue(total_attendance);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled event
                            }
                        });


                            databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(Roll).exists()) {
                                        Toast.makeText(getApplicationContext(), "Username Already Present", Toast.LENGTH_SHORT).show();
                                    } else {
                                        databaseStudent.child(Roll).setValue(hod);
                                        Toast.makeText(getApplicationContext(), "student added successfully", Toast.LENGTH_LONG).show();
                                        STUDENTNAME.setText("");
                                        STUDENTROLL.setText("");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "fields cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static String formatWord(String word) {
        if (word.length() <= 1) {
            return word.toUpperCase();
        }

        String firstLetter = word.substring(0, 1).toUpperCase();
        String restOfWord = word.substring(1).toLowerCase();

        return firstLetter + restOfWord;
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
