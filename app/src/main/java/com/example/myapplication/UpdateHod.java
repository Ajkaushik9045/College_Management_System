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

public class UpdateHod extends AppCompatActivity {
    TextView Hname,Hid,HBranch,Hpassword;
    String hid,hname,hpassword,hbranch;
    private Button UPDATE,searchs;
    EditText UserID;
    DatabaseReference databaseStudent= FirebaseDatabase.getInstance().getReference("HOD");
    String uniqueId;
    String ID,NAME,BRANCH,HODID,HODPASS;
    ConstraintLayout constraintLayout;
    CardView cardView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hod);
        UserID=findViewById(R.id.userID);
        searchs=findViewById(R.id.searchs);
        constraintLayout=findViewById(R.id.constraintLayout);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);


        Hname = findViewById(R.id.nameText1);
        Hid = findViewById(R.id.IDText);
        HBranch =findViewById(R.id.DEPTText);
        Hpassword =findViewById(R.id.PasswordText);
        UPDATE=findViewById(R.id.button);

        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = UserID.getText().toString();
                if (ID.isEmpty()) {
                    Toast.makeText(UpdateHod.this, "Enter ID", Toast.LENGTH_SHORT).show();
                } else {


                    databaseStudent.orderByChild("id").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    cardView.setVisibility(View.VISIBLE);
                                    constraintLayout.setVisibility(View.VISIBLE);
                                    NAME = dsp.child("name").getValue().toString();
                                    BRANCH = dsp.child("branch").getValue().toString();
                                    HODID = dsp.child("id").getValue().toString();
                                    HODPASS = dsp.child("password").getValue().toString();
                                }
                                if (HODID.equals(ID)) {
                                    Hname.setText(NAME);
                                    Hid.setText(HODID);
                                    HBranch.setText(BRANCH);
                                    Hpassword.setText(HODPASS);
                                } else {
                                    Hname.setText("");
                                    Hid.setText("");
                                    HBranch.setText("");
                                    Hpassword.setText("");
                                    Toast.makeText(getApplicationContext(), "HOD record not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Hname.setText("");
                                Hid.setText("");
                                HBranch.setText("");
                                Hpassword.setText("");
                                Toast.makeText(getApplicationContext(), "HOD record not found", Toast.LENGTH_SHORT).show();
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
                hname = Hname.getText().toString().trim();
                hid = Hid.getText().toString();
                hpassword = Hpassword.getText().toString();
                hbranch = HBranch.getText().toString().trim();

                // Capitalize the first letter of hname
                if (!TextUtils.isEmpty(hname)) {
                    String[] nameParts = hname.split(" ");
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
                    hname = capitalized.toString().trim();
                }

                // Capitalize the first letter of hbranch until the first space
                if (!TextUtils.isEmpty(hbranch)) {
                    int spaceIndex = hbranch.indexOf(" ");
                    if (spaceIndex > 0) {
                        hbranch = hbranch.substring(0, spaceIndex).substring(0, 1).toUpperCase() + hbranch.substring(0, spaceIndex).substring(1).toLowerCase() + hbranch.substring(spaceIndex);
                    } else {
                        hbranch = hbranch.substring(0, 1).toUpperCase() + hbranch.substring(1).toLowerCase();
                    }
                }

                if (!TextUtils.isEmpty(Hid.getText().toString())) {
                    if (hbranch.equals("Dept")) {
                        Toast.makeText(UpdateHod.this, "Select Dept", Toast.LENGTH_SHORT).show();
                    } else {
                        if (hbranch.equalsIgnoreCase("Computer") || hbranch.equalsIgnoreCase("Civil") || hbranch.equalsIgnoreCase("Plastic") || hbranch.equalsIgnoreCase("Applied") || hbranch.equalsIgnoreCase("Electrical") || hbranch.equalsIgnoreCase("Mechanical") || hbranch.equalsIgnoreCase("Electronics")) {
                            final HOD hod = new HOD(hname, hid, hpassword, hbranch);
                            databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    databaseStudent.child(hid).setValue(hod);
                                    Hname.setText("");
                                    Hid.setText("");
                                    HBranch.setText("");
                                    Hpassword.setText("");
                                    Toast.makeText(getApplicationContext(), "HOD update successfully", Toast.LENGTH_LONG).show();

                                    // Hide the cardView
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle database cancellation error if needed
                                }
                            });
                        } else {
                            Toast.makeText(UpdateHod.this, "Invalid Dept", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        cardView.setVisibility(View.GONE);


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