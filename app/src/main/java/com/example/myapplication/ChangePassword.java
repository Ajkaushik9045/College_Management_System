package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText email,oldcode,newcode,confirmcode;
    Button save;
    String Email,Password,newPassword,ConfirmPassword;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        email=findViewById(R.id.editTextTextEmailAddress);
        oldcode=findViewById(R.id.editTextTextPersonName4);
        newcode=findViewById(R.id.editTextTextPersonName3);
        confirmcode=findViewById(R.id.editTextTextPersonName2);
        save=findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        Email=email.getText().toString();
        Password=oldcode.getText().toString();
        newPassword=newcode.getText().toString();
        int length=newPassword.length();
        ConfirmPassword=confirmcode.getText().toString();
        if(Email.isEmpty()){
            Toast.makeText(ChangePassword.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(ChangePassword.this, "Enter old Password", Toast.LENGTH_SHORT).show();
        } else if (newPassword.isEmpty()) {
            Toast.makeText(ChangePassword.this, "Enter new Password", Toast.LENGTH_SHORT).show();
        }else if (ConfirmPassword.isEmpty()){
            Toast.makeText(ChangePassword.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (length<6) {
            Toast.makeText(ChangePassword.this, "Password should be 6 characters", Toast.LENGTH_SHORT).show();
        } else if(!newPassword.equals(ConfirmPassword)) {
            Toast.makeText(ChangePassword.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
        }else {
            AuthCredential credential = EmailAuthProvider.getCredential(Email,Password);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update the principal's password with the new password
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ChangePassword.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePassword.this, "Failed to re-authenticate", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
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