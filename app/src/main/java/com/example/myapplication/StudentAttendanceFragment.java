package com.example.myapplication;

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

public class StudentAttendanceFragment extends Fragment {
    TextView textView, DatePicker;
    public static final String NAMES = "NAME", ROLL = "ROLL";
    public static final String Branch = "BRANCH", SEMESTER = "SEMESTER";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("BRANCH");
    float percentage = 0;
    Button Search;
    //    String NAME, ROLLNO, BRANCH2, SEMESTER2, roll, name, attendanceText, Present, branch1, semester1, total;
    TableLayout tableLayout;
    String Date = "date";
    int totalattendance = 0, nso = 0;
    CardView cardView;
    String naming,branch,semester,rollno,Name;
    TextView NAME,BRANCH,SEMESTER1,ROLLNO;
    ImageButton fees,attendance,result;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentAttendanceFragment() {
        // Required empty public constructor
    }


    public static StudentAttendanceFragment newInstance(String rollno,String name) {
        StudentAttendanceFragment fragment = new StudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, rollno);
        args.putString(ARG_PARAM2, name);
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
        return inflater.inflate(R.layout.fragment_student_attendance, container, false);
    }
}