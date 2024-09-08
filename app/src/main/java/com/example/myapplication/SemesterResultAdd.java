package com.example.myapplication;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SemesterResultAdd extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST_CODE = 1;
    private Uri pdfUri;
    private TextView textView;
    private EditText editText;
    private ProgressDialog progressDialog;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_result_add);

        textView = findViewById(R.id.textView3);
        editText = findViewById(R.id.textView);
        editText.setEnabled(false);

        Button pickFileButton = findViewById(R.id.pick_file_button);
        pickFileButton.setOnClickListener(v -> openFileChooser());

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveData());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST_CODE);
        editText.setEnabled(true);
    }

    private void saveData() {
        if (pdfUri != null) {
            String filename = editText.getText().toString();
            if (!filename.isEmpty()) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference pdfRef = storageRef.child("pdfs/" + filename + ".pdf");

                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading File");
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
                    uploadTask.cancel();
                    progressDialog.dismiss();
                    Toast.makeText(this, "File upload canceled.", Toast.LENGTH_SHORT).show();
                });

                uploadTask = pdfRef.putFile(pdfUri);
                uploadTask.addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int) progress);
                }).addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("pdfs");
                        String pdfId = databaseRef.push().getKey();
                        databaseRef.child(pdfId).setValue(uri.toString());
                        Toast.makeText(this, "PDF file uploaded successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to retrieve the download URL.", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to upload the PDF file.", Toast.LENGTH_SHORT).show();
                });

                progressDialog.show();
            } else {
                Toast.makeText(this, "Please enter a file name.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No PDF file selected.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            textView.setText(pdfUri.toString());
        } else {
            Toast.makeText(this, "Failed to pick the PDF file.", Toast.LENGTH_SHORT).show();
            editText.setEnabled(false);
        }
    }
}
