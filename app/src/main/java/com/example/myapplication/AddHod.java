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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddHod extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   DatabaseReference databaseHOD;

    private Button SAVE;
    private EditText HODNAME;
    private Spinner SPINNER;
    private EditText HODID;

    private EditText HODPASSWORD;

    String Id,Name,Password,Branch;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hod);
        databaseHOD = FirebaseDatabase.getInstance().getReference("HOD");
        SAVE=findViewById(R.id.SaveButton);
        HODNAME=findViewById(R.id.EnterHodNameField);
        HODID=findViewById(R.id.EditHODIDEnter);
        HODPASSWORD=findViewById(R.id.EnterHodPassword);
        SPINNER=findViewById(R.id.Departmentspinner);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Select_Dept,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER.setAdapter(adapter);
        SPINNER.setOnItemSelectedListener(this);
        SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Branch=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedString =HODNAME.getText().toString();
                Id = HODID.getText().toString();
                Password = HODPASSWORD.getText().toString();
                String[] words = formattedString.split("\\s");
                StringBuilder result = new StringBuilder();

                for (String word : words) {
                    if (word.length() > 0) {
                        String formattedWord = formatWord(word);
                        result.append(formattedWord).append(" ");
                    }
                }
                Name = result.toString().trim();

                    if(Name.isEmpty()){
                        Toast.makeText(AddHod.this, "Enter HOD Name", Toast.LENGTH_SHORT).show();
                    } else if (Password.isEmpty()) {
                        Toast.makeText(AddHod.this, "Enter HOD Password", Toast.LENGTH_SHORT).show();

                    } else if(Branch.equals("Dept")){
                        Toast.makeText(AddHod.this, "Select Dept", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final HOD hod = new HOD(Name,Id,Password,Branch);
                        databaseHOD.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(Id).exists()) {
                                    Toast.makeText(getApplicationContext(), "Username Already Present", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseHOD.child(Id).setValue(hod);
                                    Toast.makeText(getApplicationContext(), "HOD added successfully", Toast.LENGTH_LONG).show();
                                    HODNAME.setText("");
                                    HODID.setText("");
                                    HODPASSWORD.setText("");


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });}

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





