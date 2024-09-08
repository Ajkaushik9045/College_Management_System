package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Api extends AppCompatActivity {
    DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    TextView textView;
    private Uri fileUri;
    int totalDataItems;
    int numNewData = 0;
    private Button pickFileButton, save;
    private ActivityResultLauncher<String> filePickerLauncher;
    ImageView imageView;
//    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
//        progressDialog = new ProgressDialog(this);
        save = findViewById(R.id.save_button);
        pickFileButton = findViewById(R.id.pick_file_button);
        textView = findViewById(R.id.textView3);
        imageView = findViewById(R.id.image_view);
        save.setVisibility(View.GONE);
        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                progressDialog.setMessage("Saving Data...");
//                progressDialog.setCancelable(false);
//                progressDialog.setIndeterminate(false);
//                progressDialog.getWindow().setGravity(Gravity.CENTER);
//                progressDialog.show();
                String filePath = getFileFromUri(fileUri);
                readFileAndSaveToFirebase(filePath);
            }
        });
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                fileUri = result;
                String filePath = getFileFromUri(fileUri);
                if (filePath != null) {
                    save.setVisibility(View.VISIBLE);
                    textView.setText(getFileNameFromPath(filePath)); // Set the file name in the TextView
                }
            }
        });

    }

    private String getFileNameFromPath(String filePath) {
        if (filePath != null) {
            int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1 && lastSeparatorIndex < filePath.length() - 1) {
                return filePath.substring(lastSeparatorIndex + 1);
            }
        }
        return "";
    }

    private void checkPermissionsAndOpenFilePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        filePickerLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Mime type for XLSX files
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileFromUri(Uri uri) {
        String filePath = null;
        if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        String fileName = cursor.getString(columnIndex);
                        File tempFile = new File(getCacheDir(), fileName);
                        tempFile.createNewFile();
                        try (InputStream inputStream = getContentResolver().openInputStream(uri); FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[4 * 1024]; // 4 KB buffer
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        }
                        filePath = tempFile.getAbsolutePath();
                    }
                }
            } catch (IOException e) {
//                progressDialog.dismiss();
                Log.e("MainActivity", "Error retrieving file: " + e.getMessage());
            }
        }
        return filePath;
    }


    private void readFileAndSaveToFirebase(String filePath) {
        File file = new File(filePath);

        try {
            DataReadListener listener = new DataReadListener() {
                @Override
                public void onDataRead(HashMap<String, HashMap<String, Object>> newDataMap) {
                    if (newDataMap != null) {
                        DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference().child("Student");
                        // Counter for the number of new data items
                        totalDataItems = newDataMap.size(); // Total number of data items in the Excel sheet

                        // Iterate over the newDataMap to check if roll number already exists
                        for (String rollNo : newDataMap.keySet()) {
                            databaseStudent.child(rollNo).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Roll number already exists, do not change
                                        totalDataItems--; // Reduce the totalDataItems count if the data item is already present
                                        checkCompletionAndShowToast(totalDataItems, numNewData);
                                    } else {
                                        // Roll number does not exist, add it to the database
                                        databaseStudent.child(rollNo).setValue(newDataMap.get(rollNo)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    numNewData++;
                                                    checkCompletionAndShowToast(totalDataItems, numNewData);
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle the onCancelled event if needed
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Api.this, "Error reading file", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            readExcelFileAndGetHashMap(file, listener, this);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Api.this, "Error reading file", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(Api.this, "Required headers not found in the Excel file.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void checkCompletionAndShowToast(int totalDataItems, int numNewData) {
        if (totalDataItems == 0) {
            if (numNewData > 0) {
                Toast.makeText(Api.this, numNewData + " data item(s) added successfully", Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(Api.this, "Data add successfully", Toast.LENGTH_SHORT).show();
            finish();
            }
        }
    }


    private HashMap<String, HashMap<String, Object>> readExcelFileAndGetHashMap(File file, DataReadListener listener, Context context) throws IOException {
        HashMap<String, HashMap<String, Object>> dataMap = new HashMap<>();
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet contains the data

        Row headerRow = sheet.getRow(0);

        boolean headersExist = checkHeadersExist(headerRow, "roll", "name", "branch", "semester", "absent", "present", "nsoAbsent");
        if (!headersExist) {
            workbook.close();
            fis.close();
//            progressDialog.dismiss();
            Toast.makeText(context, "Required headers not found in the Excel file.", Toast.LENGTH_SHORT).show();
            return dataMap;
        }
        int rollColumnIndex = getColumnIndex(headerRow, "roll");
        int nameColumnIndex = getColumnIndex(headerRow, "name");
        int branchColumnIndex = getColumnIndex(headerRow, "branch");
        int semesterColumnIndex = getColumnIndex(headerRow, "semester");
        int nsoAbsentColumnIndex = getColumnIndex(headerRow, "nsoAbsent");
        int presentColumnIndex = getColumnIndex(headerRow, "present");
        int absentColumnIndex = getColumnIndex(headerRow, "absent");

        // Retrieve existing data from Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Student");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashSet<String> existingRollNumbers = new HashSet<>();

                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String rollNumber = studentSnapshot.getKey();
                    existingRollNumbers.add(rollNumber);
                }

                HashMap<String, HashMap<String, Object>> newDataMap = new HashMap<>(); // Create a new data map for the updated data

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    String roll = getCellValueAsString(row.getCell(rollColumnIndex));

                    if (existingRollNumbers.contains(roll)) {
                        // Roll number already exists in the database, skip updating or deleting
                        continue;
                    }

                    // Rest of the code to process and store the data
                    String name = getCellValueAsString(row.getCell(nameColumnIndex));
                    String branch = getCellValueAsString(row.getCell(branchColumnIndex));
                    String semester = getCellValueAsString(row.getCell(semesterColumnIndex));
                    int nsoAbsent = Integer.parseInt(getCellValueAsString(row.getCell(nsoAbsentColumnIndex)));
                    int present = Integer.parseInt(getCellValueAsString(row.getCell(presentColumnIndex)));
                    int absent = Integer.parseInt(getCellValueAsString(row.getCell(absentColumnIndex)));

                    // Create a nested HashMap for the student's data
                    HashMap<String, Object> studentData = new HashMap<>();
                    studentData.put("roll", roll);
                    studentData.put("name", name);
                    studentData.put("branch", branch);
                    studentData.put("semester", semester);
                    studentData.put("nsoAbsent", nsoAbsent);
                    studentData.put("present", present);
                    studentData.put("absent", absent);

                    // Store the student data in the new data map with the roll number as the key
                    newDataMap.put(roll, studentData);
                }

                // Merge the existing data with the new data map
                dataMap.putAll(newDataMap);

                // Save the updated newDataMap to Firebase
                DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("Student");
                studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (String roll : newDataMap.keySet()) {
                            if (dataSnapshot.child(roll).exists()) {
                                // Roll number already exists, perform update if needed
                                // Handle the update logic here

                            } else {
                                // Roll number doesn't exist, perform addition
                                DatabaseReference databaseStudent = studentsRef.child(roll);
                                databaseStudent.setValue(newDataMap.get(roll));
                            }

                            listener.onDataRead(newDataMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error, if any
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });


        workbook.close();
        fis.close();

        return dataMap;
    }

    public interface DataReadListener {
        void onDataRead(HashMap<String, HashMap<String, Object>> newDataMap);
    }


    private boolean checkHeadersExist(Row headerRow, String... headers) {
        for (String header : headers) {
            boolean found = false;
            for (Cell cell : headerRow) {
                if (cell != null && getCellValueAsString(cell).equalsIgnoreCase(header)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private int getColumnIndex(Row headerRow, String header) {
        for (Cell cell : headerRow) {
            if (cell != null && getCellValueAsString(cell).equalsIgnoreCase(header)) {
                return cell.getColumnIndex();
            }
        }
        throw new IllegalArgumentException("Header '" + header + "' not found in the Excel file.");
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "0"; // Handle empty cells by returning a default value, such as "0".
        }

        if (cell.getCellType() == CellType.STRING) {
            return capitalizeWords(cell.getStringCellValue().trim());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (cell.getNumericCellValue() == Math.floor(cell.getNumericCellValue())) {
                return String.valueOf((long) cell.getNumericCellValue());
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        } else {
            return "0"; // Handle non-numeric cells by returning a default value.
        }
    }





    private String capitalizeWords(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalize = true;
        for (char ch : input.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                capitalize = true;
            } else if (capitalize) {
                ch = Character.toTitleCase(ch);
                capitalize = false;
            }
            result.append(ch);
        }
        return result.toString();
    }

    private boolean checkHeadersExist(Set<String> headerSet, String... headers) {
        for (String header : headers) {
            if (!headerSet.contains(header)) {
                return false;
            }
        }
        return true;
    }

}
