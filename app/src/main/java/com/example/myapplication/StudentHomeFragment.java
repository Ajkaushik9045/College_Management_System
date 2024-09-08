package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentHomeFragment extends Fragment {

    TextView textView, DatePicker;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    float percentage = 0;
    Button Search;
    TableLayout tableLayout;
    String Date = "date";
    int totalattendance = 0, nso = 0;
    CardView cardView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    String naming, branch, semester, rollno, Name;
    TextView NAME, BRANCH, SEMESTER1, ROLLNO;
    ImageButton fees, attendance, result;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment newInstance(String Name, String Rollno, String Branch, String Semester) {
        StudentHomeFragment fragment = new StudentHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Name);
        args.putString(ARG_PARAM2, Rollno);
        args.putString(ARG_PARAM3, Branch);
        args.putString(ARG_PARAM4, Semester);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);
        NAME = view.findViewById(R.id.textView4);
        BRANCH = view.findViewById(R.id.branchName);
        SEMESTER1 = view.findViewById(R.id.semesternumber);
        ROLLNO = view.findViewById(R.id.Sbrnumber);
        fees = view.findViewById(R.id.fees);
        attendance = view.findViewById(R.id.attendance);
        result = view.findViewById(R.id.result);

        // Rest of your code here

        NAME.setText(mParam1);
        BRANCH.setText(mParam3);
        SEMESTER1.setText(mParam4);
        ROLLNO.setText(mParam2);

        fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = "https://www.onlinesbi.sbi/sbicollect/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(intent);
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudentResult.class);
                startActivity(intent);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),StudentMonthAttendance.class);
                intent.putExtra(StudentMonthAttendance.NAMES,mParam1);
                intent.putExtra(StudentMonthAttendance.ROLL,mParam2);
                startActivity(intent);
            }
        });
        return view;
    }
}
