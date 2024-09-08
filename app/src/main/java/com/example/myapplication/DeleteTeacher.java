package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteTeacher extends AppCompatActivity {
    TextView Tname,Tid,Tpassword;
    EditText UserID;

    private Button DELETE,searchs;

    DatabaseReference databaseStudent= FirebaseDatabase.getInstance().getReference("Teacher");

    String ID,NAME,PASSWORD,StuID="";

    CardView cardView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_teacher);
        DELETE=findViewById(R.id.SaveButton);
        UserID=findViewById(R.id.userID);
        searchs=findViewById(R.id.searchs);
        Tname = findViewById(R.id.TeacherNameField);
        Tid = findViewById(R.id.TeacherIDField);
        Tpassword =findViewById(R.id.TeacherPasswordField);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);


        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID=UserID.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(DeleteTeacher.this, "Enter ID", Toast.LENGTH_SHORT).show();
                } else {
                    databaseStudent.orderByChild("id").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean TeacherFound = false;
                            if (snapshot.exists()) {
                                cardView.setVisibility(View.VISIBLE);
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    NAME = dsp.child("name").getValue().toString();
                                    StuID = dsp.child("id").getValue().toString();
                                    PASSWORD = dsp.child("pass").getValue().toString();
                                    if (StuID.equals(ID)) {
                                        Tname.setText(NAME);
                                        Tid.setText(StuID);
                                        Tpassword.setText(PASSWORD);
                                        TeacherFound = true;

                                    }
                                }
                                if (TeacherFound) {
                                    // HOD found
                                } else {
                                    Toast.makeText(DeleteTeacher.this, "Teacher not found", Toast.LENGTH_SHORT).show();
                                    cardView.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(DeleteTeacher.this, "Teacher not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DeleteTeacher.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        databaseStudent.child(StuID).setValue(null);
                        Toast.makeText(getApplicationContext(), "Teacher removed successfully", Toast.LENGTH_LONG).show();
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