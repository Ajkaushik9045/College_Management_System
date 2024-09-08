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

public class UpdateTeacher extends AppCompatActivity {
    TextView Tname,Tid,Tpassword;
    EditText UserID;
    String tname,tid,tPassword;

    private Button UPDATE,searchs;

    DatabaseReference databaseStudent= FirebaseDatabase.getInstance().getReference("Teacher");
    String uniqueId;
    String ID,NAME,PASSWORD,StuID;
    ConstraintLayout constraintLayout;
    CardView cardView;

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        UPDATE=findViewById(R.id.SaveButton);

        UserID=findViewById(R.id.userID);
        searchs=findViewById(R.id.searchs);
        Tname = findViewById(R.id.TeacherNameField);
        Tid = findViewById(R.id.TeacherIDField);
        Tpassword =findViewById(R.id.TeacherPasswordField);
//        constraintLayout=findViewById(R.id.constraintLayout);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
//        constraintLayout.setVisibility(View.GONE);

        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = UserID.getText().toString();
                if(ID.isEmpty()){
                    Toast.makeText(UpdateTeacher.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }else {
                    databaseStudent.orderByChild("id").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                cardView.setVisibility(View.VISIBLE);
                                for (DataSnapshot dsp : snapshot.getChildren()) {

                                    NAME = dsp.child("name").getValue().toString();
                                    PASSWORD = dsp.child("pass").getValue().toString();
                                    StuID = dsp.child("id").getValue().toString();

                                    if (StuID.equals(ID)) {
                                        Tname.setText(NAME);
                                        Tid.setText(StuID);
                                        Tpassword.setText(PASSWORD);

                                    } else {
                                        cardView.setVisibility(View.GONE);
                                        Toast.makeText(UpdateTeacher.this, "Teacher Record not found", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } else {
                                cardView.setVisibility(View.GONE);
                                Toast.makeText(UpdateTeacher.this, "Teacher Record not found", Toast.LENGTH_SHORT).show();

                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

        });
        UPDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tname = Tname.getText().toString();
                tid = Tid.getText().toString();
                tPassword = Tpassword.getText().toString();

                if (!TextUtils.isEmpty(tname)) {
                    String[] nameParts = tname.split(" ");
                    StringBuilder capitalized = new StringBuilder();
                    for (String part : nameParts) {
                        if (!TextUtils.isEmpty(part)) {
                            capitalized.append(part.substring(0, 1).toUpperCase());
                            if (part.length() > 1) {
                                capitalized.append(part.substring(1).toLowerCase());
                            }
                            capitalized.append(" ");
                        }
                    }
                    tname = capitalized.toString().trim();


                    Teacher teacher = new Teacher(tname, tid, tPassword);
                    databaseStudent.child(tid).setValue(teacher);
                    Toast.makeText(getApplicationContext(), "Teacher update successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
//                    Toast.makeText(getApplicationContext(), "Teacher Record not found", Toast.LENGTH_LONG).show();
                }
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