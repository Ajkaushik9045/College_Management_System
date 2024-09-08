package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DeleteStudent extends AppCompatActivity {
    TextView Sname,Sid,SBranch,Ssemester;
    private Button DELETE,searchs;
    EditText UserID;
    String ID,NAME,BRANCH,StuID,StuSemester,uniqueId;

    DatabaseReference databaseStudent= FirebaseDatabase.getInstance().getReference("Student");
ConstraintLayout constraintLayout;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);
        UserID=findViewById(R.id.userID);
        searchs=findViewById(R.id.searchs);
        constraintLayout=findViewById(R.id.constraintLayout3);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);



        DELETE=findViewById(R.id.SaveButton);
        Sname = findViewById(R.id.NameField);
        Sid =  findViewById(R.id.IDField);
        SBranch = findViewById(R.id.BranchField);
        Ssemester = findViewById(R.id.SemesterField);
        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID=UserID.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(DeleteStudent.this, "Enter Roll no.", Toast.LENGTH_SHORT).show();
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
                                }
                                if (TeacherFound) {
                                    // HOD found
                                } else {
                                    Toast.makeText(DeleteStudent.this, "Student not found", Toast.LENGTH_SHORT).show();
                                    cardView.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(DeleteStudent.this, "Student not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DeleteStudent.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        DELETE.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {

                    uniqueId = UserID.getText().toString();
                        databaseStudent.child(uniqueId).setValue(null);
                        Toast.makeText(getApplicationContext(), "Student removed successfully", Toast.LENGTH_LONG).show();
                        cardView.setVisibility(View.GONE);



            }

        });
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