package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTeacher extends AppCompatActivity {

    private Button SAVE;
    private EditText TEACHERNAME;
    private EditText TEACHERID;
    private EditText TEACHERPASSWORD;
    DatabaseReference databaseTeacher = FirebaseDatabase.getInstance().getReference("Teacher");;
    String name,id,Password;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        SAVE=findViewById(R.id.SaveButton);
        TEACHERNAME=findViewById(R.id.EnterTeacherNameField);
        TEACHERID=findViewById(R.id.EnterTeacherIDField);
        TEACHERPASSWORD=findViewById(R.id.EnterTeacherPasswordField);

        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedString =TEACHERNAME.getText().toString();
                id = TEACHERID.getText().toString();
                Password = TEACHERPASSWORD.getText().toString();
                String[] words = formattedString.split("\\s");
                StringBuilder result = new StringBuilder();

                for (String word : words) {
                    if (word.length() > 0) {
                        String formattedWord = formatWord(word);
                        result.append(formattedWord).append(" ");
                    }
                }

                name = result.toString().trim();
                if (id.isEmpty()) {
                    Toast.makeText(AddTeacher.this, "Enter Teacher ID", Toast.LENGTH_SHORT).show();
                } else if(name.isEmpty()){
                        Toast.makeText(AddTeacher.this, "Enter Teacher Name", Toast.LENGTH_SHORT).show();
                } else if (Password.isEmpty()) {
                        Toast.makeText(AddTeacher.this, "Enter Teacher Password", Toast.LENGTH_SHORT).show();
                } else {
                        final   Teacher teacher =new Teacher(name ,id ,Password);
                        databaseTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(id).exists()) {
                                    Toast.makeText(getApplicationContext(), "Username Already Present", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseTeacher.child(id).setValue(teacher);
                                    TEACHERID.setText("");
                                    TEACHERNAME.setText("");
                                    TEACHERPASSWORD.setText("");
                                    Toast.makeText(getApplicationContext(), "Teacher added successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
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