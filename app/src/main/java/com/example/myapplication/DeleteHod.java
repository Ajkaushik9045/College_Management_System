package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
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

public class DeleteHod extends AppCompatActivity {
    TextView Hname, Hid, HBranch, Hpassword;
    Button DELETE, searchs;
    EditText UserID;
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("HOD");
    String uniqueId;
    String ID, NAME, BRANCH, HODID, HODPASS;
    ConstraintLayout constraintLayout;
    CardView cardView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_hod);
        UserID = findViewById(R.id.userID);
        searchs = findViewById(R.id.searchs);
        constraintLayout = findViewById(R.id.constraintLayout);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);

        Hname = findViewById(R.id.nameText1);
        Hid = findViewById(R.id.IDText);
        HBranch = findViewById(R.id.DEPTText);
        Hpassword = findViewById(R.id.PasswordText);
        DELETE = findViewById(R.id.button);

        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = UserID.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(DeleteHod.this, "Enter ID", Toast.LENGTH_SHORT).show();
                } else {
                    databaseStudent.orderByChild("id").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean hodFound = false;
                            if (snapshot.exists()) {
                                cardView.setVisibility(View.VISIBLE);
                                constraintLayout.setVisibility(View.VISIBLE);
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    NAME = dsp.child("name").getValue().toString();
                                    BRANCH = dsp.child("branch").getValue().toString();
                                    HODID = dsp.child("id").getValue().toString();
                                    HODPASS = dsp.child("password").getValue().toString();
                                    if (HODID.equals(ID)) {
                                        Hname.setText(NAME);
                                        Hid.setText(HODID);
                                        HBranch.setText(BRANCH);
                                        Hpassword.setText(HODPASS);
                                        hodFound = true;
                                    }
                                }
                                if (hodFound) {
                                    // HOD found
                                } else {
                                    Toast.makeText(DeleteHod.this, "HOD not found", Toast.LENGTH_SHORT).show();
                                    cardView.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(DeleteHod.this, "HOD not found", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DeleteHod.this, "Error retrieving HOD data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(UserID.getText().toString())) {
                    String id = UserID.getText().toString();
                    if (id.equals(HODID)) {
                        databaseStudent.child(id).setValue(null);
                        Toast.makeText(getApplicationContext(), "HOD removed successfully", Toast.LENGTH_LONG).show();
                        cardView.setVisibility(View.GONE);
                        return;

                    }}
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