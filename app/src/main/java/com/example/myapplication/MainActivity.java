package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
public class MainActivity extends AppCompatActivity {
    EditText username, password;
    TextView textView;
    Button button;
    TextView forgotPasswordButton;
    CheckBox checkBox;
    String UserID, Password;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private boolean showPassword = false;
    public static final String ACCOUNT = "ACCOUNT";

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("HOD");
    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Teacher");
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox = findViewById(R.id.checkBox);
        textView = findViewById(R.id.loginText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.Password);
        forgotPasswordButton = findViewById(R.id.button4);
        button = findViewById(R.id.button);
        UserID = username.getText().toString();
        Password = password.getText().toString();
        Intent intent = getIntent();
        String account = intent.getStringExtra(ACCOUNT);
        textView.setText(account);
        forgotPasswordButton.setVisibility(View.GONE);
        if (account.equals("STUDENT")) {
            username.setVisibility(View.GONE);
            password.setHint("Enter your Roll no.");
        }
        if (account.equals("PRINCIPAL")) {
            forgotPasswordButton.setVisibility(View.VISIBLE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPassword = isChecked;
                updatePasswordVisibility();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserID = username.getText().toString().trim();
                Password = password.getText().toString().trim();

                if (account.equals("TEACHER") || account.equals("HOD")) {
                    if (UserID.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter UserName", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (account.equals("PRINCIPAL")) {
                    if (UserID.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (account.equals("STUDENT")) {
                    if (Password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter your Roll no", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Logging in...");
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.getWindow().setGravity(Gravity.CENTER);
                progressDialog.show();

                if (account.equals("TEACHER")) {
                    reference2.orderByChild("id").equalTo(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean TeacherFound = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    String ids = dsp.child("id").getValue().toString();
                                    String password1 = dsp.child("pass").getValue().toString();
                                    String Names = dsp.child("name").getValue().toString();
                                    String[] words = Names.split("\\s");
                                    String name = words[0];
                                    if (ids.equals(UserID)) {
                                        TeacherFound = true;
                                        if (password1.equals(Password)) {
                                            progressDialog.dismiss();
                                            Intent intent1 = new Intent(MainActivity.this, teacherView.class);
                                            intent1.putExtra(HODView.NAME, name);
                                            startActivity(intent1);
                                            username.setText("");
                                            password.setText("");
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Check Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            if (!TeacherFound) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (account.equals("HOD")) {
                    reference.orderByChild("id").equalTo(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean TeacherFound = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    String ids = dsp.child("id").getValue().toString();
                                    String password1 = dsp.child("password").getValue().toString();
                                    String Names = dsp.child("name").getValue().toString();
                                    String[] words = Names.split("\\s");
                                    String name = words[0];
                                    if (ids.equals(UserID)) {
                                        TeacherFound = true;
                                        if (password1.equals(Password)) {
                                            progressDialog.dismiss();
                                            Intent intent1 = new Intent(MainActivity.this, HODView.class);
                                            intent1.putExtra(HODView.NAME, name);
                                            startActivity(intent1);
                                            username.setText("");
                                            password.setText("");
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Check Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            if (!TeacherFound) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (account.equals("STUDENT")) {
                    reference1.orderByChild("roll").equalTo(Password).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean TeacherFound = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    String ids = dsp.child("roll").getValue().toString();
                                    String password1 = dsp.child("roll").getValue().toString();
                                    String Names = dsp.child("name").getValue().toString();
                                    String BRANCH2 = dsp.child("branch").getValue().toString();
                                    String SEMESTER2 = Objects.requireNonNull(dsp.child("semester").getValue()).toString();
//                                    String[] words = Names.split("\\s");
//                                    Names = words[0];
                                    if (password1.equals(Password)) {
                                        TeacherFound = true;

                                        progressDialog.dismiss();
                                        Intent intent1 = new Intent(MainActivity.this, StudentView.class);
                                        intent1.putExtra(StudentView.NAMES, Names);
                                        intent1.putExtra(StudentView.ROLL, ids);
                                        intent1.putExtra(StudentView.Branch, BRANCH2);
                                        intent1.putExtra(StudentView.SEMESTER, SEMESTER2);
                                        startActivity(intent1);
                                        username.setText("");
                                        password.setText("");
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Check Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if (!TeacherFound) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error retrieving Teacher data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (account.equals("PRINCIPAL")) {
                    auth.signInWithEmailAndPassword(UserID, Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent1 = new Intent(MainActivity.this, PrincipleView.class);
                                intent1.putExtra(PrincipleView.NAME, "Principal");
                                startActivity(intent1);
                                username.setText("");
                                password.setText("");
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Check Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                forgotPasswordButton.setTextColor(Color.BLUE);
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Sending password reset email...");
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.getWindow().setGravity(Gravity.CENTER);
                progressDialog.show();

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Exception exception = task.getException();

                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(MainActivity.this, "No account found with this email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void updatePasswordVisibility() {
        // Check if the showPassword variable is true
        if (showPassword) {
            // If true, show the password as plain text
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // If false, show the password as dots
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        password.setSelection(password.getText().length());
    }
}
