package com.example.myapplication;

import android.annotation.SuppressLint;
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

public class AddSubject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText Subject, Code;
    Spinner SPINNER1, SPINNER2;
    String Branch = "Branch", Semester = "Sem";
    Button save;
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference().child("BRANCH");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        Subject = findViewById(R.id.EditStudentNameField);
        Code = findViewById(R.id.EditStudentRollField);
        SPINNER1 = findViewById(R.id.Branchspinner);
        SPINNER2 = findViewById(R.id.Semesterspinner);
        save = findViewById(R.id.SaveButton);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Branch.equals("Branch")) {
                    Toast.makeText(AddSubject.this, "Select Branch", Toast.LENGTH_SHORT).show();
                } else if (Semester.equals("Sem")) {
                    Toast.makeText(AddSubject.this, "Select Semester", Toast.LENGTH_SHORT).show();
                } else {
                    String formattedString = Subject.getText().toString();

                    String[] words = formattedString.split("\\s");
                    StringBuilder result = new StringBuilder();

                    for (String word : words) {
                        if (word.length() > 0) {
                            String formattedWord = formatWord(word);
                            result.append(formattedWord).append(" ");
                        }
                    }

                    String subject = result.toString().trim();
                    String code = Code.getText().toString().toUpperCase();

                    databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(Branch).child(Semester).child("Subject").child(code).exists()) {
                                Toast.makeText(getApplicationContext(), "Code Already Present", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseStudent.child(Branch).child(Semester).child("Subject").child(code).child("Subject").setValue(subject);
                                databaseStudent.child(Branch).child(Semester).child("Subject").child(code).child("Code").setValue(code);
                                Toast.makeText(getApplicationContext(), "Subject added successfully", Toast.LENGTH_LONG).show();
                               Subject.setText("");
                                Code.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



//                    if(Branch.equals("Computer")&& Semester.equals("1st")){
//                        Computer1.add(subject + " - " + code);
//                    } else if (Branch.equals("Computer")&& Semester.equals("2nd")) {
//                        Computer2.add(subject + " - " + code);
//                    }else if (Branch.equals("Computer")&& Semester.equals("3rd")) {
//                        Computer3.add(subject + " - " + code);
//                    }else if (Branch.equals("Computer")&& Semester.equals("4th")) {
//                        Computer4.add(subject + " - " + code);
//                    }else if (Branch.equals("Computer")&& Semester.equals("5th")) {
//                        Computer5.add(subject + " - " + code);
//                    }else if (Branch.equals("Computer")&& Semester.equals("6th")) {
//                        Computer6.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("6th")) {
//                        Civil6.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("5th")) {
//                        Civil5.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("4th")) {
//                        Civil4.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("3rd")) {
//                        Civil3.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("2nd")) {
//                        Civil2.add(subject + " - " + code);
//                    }else if (Branch.equals("Civil")&& Semester.equals("1st")) {
//                        Civil1.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("6th")) {
//                        Electrical6.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("5th")) {
//                        Electrical5.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("4th")) {
//                        Electrical4.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("3rd")) {
//                        Electrical3.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("2nd")) {
//                        Electrical2.add(subject + " - " + code);
//                    }else if (Branch.equals("Electrical")&& Semester.equals("1st")) {
//                        Electrical1.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("6th")) {
//                        Electronics6.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("5th")) {
//                        Electronics5.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("4th")) {
//                        Electronics4.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("3rd")) {
//                        Electronics3.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("2nd")) {
//                        Electronics2.add(subject + " - " + code);
//                    }else if (Branch.equals("Electronics")&& Semester.equals("1st")) {
//                        Electronics1.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("6th")) {
//                        Mechanical6.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("5th")) {
//                        Mechanical5.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("4th")) {
//                        Mechanical4.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("3rd")) {
//                        Mechanical3.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("2nd")) {
//                        Mechanical2.add(subject + " - " + code);
//                    }else if (Branch.equals("Mechanical")&& Semester.equals("1st")) {
//                        Mechanical1.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("6th")) {
//                        Plastic6.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("5th")) {
//                        Plastic5.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("4th")) {
//                        Plastic4.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("3rd")) {
//                        Plastic3.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("2nd")) {
//                        Plastic2.add(subject + " - " + code);
//                    }else if (Branch.equals("Plastic")&& Semester.equals("1st")) {
//                        Plastic1.add(subject + " - " + code);
//                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static String formatWord(String word) {
        if (word.length() <= 1) {
            return word.toUpperCase();
        }

        String firstLetter = word.substring(0, 1).toUpperCase();
        String restOfWord = word.substring(1).toLowerCase();

        return firstLetter + restOfWord;
    }
//    public ArrayList<String> getComputer1() {
//        return Computer1;
//    } public ArrayList<String> getComputer2() {
//        return Computer2;
//    } public ArrayList<String> getComputer3() {
//        return Computer3;
//    } public ArrayList<String> getComputer4() {
//        return Computer4;
//    } public ArrayList<String> getComputer5() {
//        return Computer5;
//    } public ArrayList<String> getComputer6() {
//        return Computer6;
//    } public ArrayList<String> getCivil1() {
//        return Civil1;
//    }public ArrayList<String> getCivil2() {
//        return Civil2;
//    }public ArrayList<String> getCivil3() {
//        return Civil3;
//    }public ArrayList<String> getCivil4() {
//        return Civil4;
//    }public ArrayList<String> getCivil5() {
//        return Civil5;
//    }public ArrayList<String> getCivil6() {
//        return Civil6;
//    }public ArrayList<String> getElectrical1() {
//        return Electrical1;
//    }public ArrayList<String> getElectrical2() {
//        return Electrical2;
//    }public ArrayList<String> getElectrical3() {
//        return Electrical3;
//    }public ArrayList<String> getElectrical4() {
//        return Electrical4;
//    }public ArrayList<String> getElectrical5() {
//        return Electrical5;
//    }public ArrayList<String> getElectrical6() {
//        return Electrical6;
//    }public ArrayList<String> getElectronics1() {
//        return Electronics1;
//    }public ArrayList<String> getElectronics2() {
//        return Electronics2;
//    }public ArrayList<String> getElectronics3() {
//        return Electronics3;
//    }public ArrayList<String> getElectronics4() {
//        return Electronics4;
//    }public ArrayList<String> getElectronics5() {
//        return Electronics5;
//    }public ArrayList<String> getElectronics6() {
//        return Electronics6;
//    }public ArrayList<String> getMechanical1() {
//        return Mechanical1;
//    }public ArrayList<String> getMechanical2() {
//        return Mechanical2;
//    }public ArrayList<String> getMechanical3() {
//        return Mechanical3;
//    }public ArrayList<String> getMechanical4() {
//        return Mechanical4;
//    }public ArrayList<String> getMechanical5() {
//        return Mechanical5;
//    }public ArrayList<String> getMechanical6() {
//        return Mechanical6;
//    }public ArrayList<String> getPlastic1() {
//        return Plastic1;
//    }public ArrayList<String> getPlastic2() {
//        return Plastic2;
//    }public ArrayList<String> getPlastic3() {
//        return Plastic3;
//    }public ArrayList<String> getPlastic4() {
//        return Plastic4;
//    }public ArrayList<String> getPlastic5() {
//        return Plastic5;
//    }public ArrayList<String> getPlastic6() {
//        return Plastic6;
//    }

}