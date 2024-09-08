package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class  HodRecord extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner HODlist;
    String item;
    TableLayout tableLayout;
    TableRow tableRow;
    String NAME,BRANCH,ID,PASSWORD;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    Button search;
    CardView cardView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_record);
        cardView=findViewById(R.id.cardview5);
        search=findViewById(R.id.searchs);
        HODlist=findViewById(R.id.spinnerTeacher);
        tableLayout=findViewById(R.id.Hodlist);
        cardView.setVisibility(View.GONE);

        HODlist.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Applied");
        categories.add("Computer");
        categories.add("Civil");
        categories.add("Electrical");
        categories.add("Electronics");
        categories.add("Mechanical");
        categories.add("Plastic");
        ArrayAdapter<String> teachadapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categories);
        teachadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HODlist.setAdapter(teachadapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(HodRecord.this);
                progressDialog.setMessage("Loading Data...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(false);
                progressDialog.getWindow().setGravity(Gravity.CENTER);

                progressDialog.show();
                tableLayout.removeViews(1,tableLayout.getChildCount()-1);
                DatabaseReference studRef=reference.child("HOD");

                studRef.orderByChild("branch").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            cardView.setVisibility(View.VISIBLE);
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                NAME = dsp.child("name").getValue().toString();
                                ID = dsp.child("id").getValue().toString();
                                BRANCH = dsp.child("branch").getValue().toString();
                                PASSWORD = dsp.child("password").getValue().toString();


                                TableRow tableRow = new TableRow(HodRecord.this);
                                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView ViewName = new TextView(HodRecord.this);
                                ViewName.setText(NAME);
                                ViewName.setPadding(8, 8, 8, 8);
                                tableRow.addView(ViewName);

                                TextView ViewBranch = new TextView(HodRecord.this);
                                ViewBranch.setText(BRANCH);
                                ViewBranch.setPadding(8, 8, 8, 8);
                                tableRow.addView(ViewBranch);

                                TextView ViewID = new TextView(HodRecord.this);
                                ViewID.setText(ID);
                                ViewID.setPadding(8, 8, 8, 8);
                                tableRow.addView(ViewID);

                                TextView ViewPassword = new TextView(HodRecord.this);
                                ViewPassword.setText(PASSWORD);
                                ViewPassword.setPadding(8, 8, 8, 8);
                                tableRow.addView(ViewPassword);

                                tableLayout.addView(tableRow);
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            cardView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(HodRecord.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);


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




