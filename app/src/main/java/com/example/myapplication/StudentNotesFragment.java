package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class StudentNotesFragment extends Fragment {


    private ListView listView;
    private List<String> fileNames;
    private List<String> fileUrls;
    private DownloadManager downloadManager;
    private NotificationManagerCompat notificationManager;

    private ProgressDialog loadingDialog;
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            showDownloadCompleteNotification(downloadId);
        }
    };
    private StorageReference storageRef;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public StudentNotesFragment() {
        // Required empty public constructor
    }

    public static StudentNotesFragment newInstance(String param1, String param2) {
        StudentNotesFragment fragment = new StudentNotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_notes, container, false);

        listView = rootView.findViewById(R.id.list_view);
        fileNames = new ArrayList<>();
        fileUrls = new ArrayList<>();
        downloadManager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        notificationManager = NotificationManagerCompat.from(requireContext());
        storageRef = FirebaseStorage.getInstance().getReference().child("Notes");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                downloadFile(position);
            }
        });

        // Create and show the loading dialog
        loadingDialog = new ProgressDialog(requireContext());
        loadingDialog.setMessage("Loading files...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Load the files
        refreshFiles();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(downloadCompleteReceiver);
    }

    private void refreshFiles() {
        fileNames.clear();
        fileUrls.clear();

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            String fileName = item.getName();
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (fileName != null) {
                                        fileNames.add(fileName);
                                        fileUrls.add(uri.toString());
                                    }
                                    sortFilesByDate();
                                    showFileList();
                                    loadingDialog.dismiss(); // Dismiss the loading dialog
                                }
                            });
                        }

                        if (listResult.getItems().isEmpty()) {
                            // No files found
                            showNoFilesFoundMessage(true);
                        } else {
                            showNoFilesFoundMessage(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to load files.", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss(); // Dismiss the loading dialog
                    }
                });
    }

    private void showNoFilesFoundMessage(boolean show) {
        if (!isAdded()) {
            // Fragment is not attached to an activity, do nothing
            return;
        }

        TextView noFilesTextView = requireActivity().findViewById(R.id.text_no_files_found);
        noFilesTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        loadingDialog.dismiss();
    }


    private void sortFilesByDate() {
        List<StudentNotesFragment.FileItem> fileItems = new ArrayList<>();
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            String fileUrl = fileUrls.get(i);
            StudentNotesFragment.FileItem fileItem = new StudentNotesFragment.FileItem(fileName, fileUrl);
            fileItems.add(fileItem);
        }

        Collections.sort(fileItems, new Comparator<StudentNotesFragment.FileItem>() {
            @Override
            public int compare(StudentNotesFragment.FileItem fileItem1, StudentNotesFragment.FileItem fileItem2) {
                return fileItem1.getDateTime().compareTo(fileItem2.getDateTime());
            }
        });

        fileNames.clear();
        fileUrls.clear();

        for (StudentNotesFragment.FileItem fileItem : fileItems) {
            fileNames.add(fileItem.getFileName());
            fileUrls.add(fileItem.getFileUrl());
        }
    }

    private class FileItem {
        private String fileName;
        private String fileUrl;

        public FileItem(String fileName, String fileUrl) {
            this.fileName = fileName;
            this.fileUrl = fileUrl;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public LocalDateTime getDateTime() {
            return LocalDateTime.now();
        }
    }

    private void showFileList() {
        if (!isAdded()) {
            // Fragment is not attached to an activity, do nothing
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.customlistview, fileNames) {
            @NonNull
            @Override
            public View getView(final int position, @NonNull View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(requireContext());
                    convertView = inflater.inflate(R.layout.customlistview, parent, false);
                }

                TextView fileNameTextView = convertView.findViewById(R.id.text_file_name);
                ImageView deleteIconImageView = convertView.findViewById(R.id.icon_delete);
                deleteIconImageView.setVisibility(View.GONE);
                String fileName = fileNames.get(position);
                fileNameTextView.setText(fileName);
                fileNameTextView.setTextColor(Color.parseColor("#0000FF"));

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }

    private void downloadFile(int position) {
        String fileName = fileNames.get(position);
        String fileUrl = fileUrls.get(position);

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        storageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle("Downloading File");
                        request.setDescription(fileName);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                        downloadManager.enqueue(request);
                        Toast.makeText(requireContext(), "Downloading file: " + fileName, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to retrieve download URL.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showDownloadCompleteNotification(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                int fileUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String downloadedFilePath = cursor.getString(fileUriIndex);

                // Remove the "file://" prefix from the downloaded file path
                String filePath = Uri.parse(downloadedFilePath).getPath();

                // Use FileProvider to create a content URI for the file
                Uri contentUri = FileProvider.getUriForFile(requireContext(), "com.example.myapplication.fileprovider", new File(filePath));

                showNotification("Download Complete", "File downloaded successfully.", contentUri.toString());
            }
        }
        cursor.close();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String message, String contentUriString) {
        createNotificationChannel(); // Create the notification channel

        // Rest of your code remains the same
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(contentUriString), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Add read permission to the intent

        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle the missing permission.
            return;
        }

        notificationManager.notify(1, builder.build());
    }
}
