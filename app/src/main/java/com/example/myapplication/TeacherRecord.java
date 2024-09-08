package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherRecord extends AppCompatActivity {
    private TableLayout tableLayout;
    String NAME,ID,PASSWORD;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Teacher");

    CardView cardView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_record);
        tableLayout=findViewById(R.id.Teacherlist);

        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);
        tableLayout.setVisibility(View.GONE);
        ProgressDialog progressDialog = new ProgressDialog(TeacherRecord.this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        tableLayout.removeViews(1,tableLayout.getChildCount()-1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                cardView.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    NAME=dsp.child("name").getValue().toString();
                    ID=dsp.child("id").getValue().toString();
                    PASSWORD=dsp.child("pass").getValue().toString();


                    TableRow tableRow=new TableRow(TeacherRecord.this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

                    TextView ViewName= new TextView(TeacherRecord.this);
                    ViewName.setText(NAME);
                    ViewName.setPadding(8,8,8,8);
                    tableRow.addView(ViewName);


                    TextView ViewID= new TextView(TeacherRecord.this);
                    ViewID.setText(ID);
                    ViewID.setPadding(8,8,8,8);
                    tableRow.addView(ViewID);

                    TextView ViewPassword= new TextView(TeacherRecord.this);
                    ViewPassword.setText(PASSWORD);
                    ViewPassword.setPadding(8,8,8,8);
                    tableRow.addView(ViewPassword);

                    tableLayout.addView(tableRow);
                    progressDialog.dismiss();
                }}
                else {
                    cardView.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(TeacherRecord.this, "Teacher Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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