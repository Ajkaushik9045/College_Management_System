package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HODHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageButton teacher, student, attendance, result;
    public static final String NAME = "NAME";
    TextView textView;
    String Name, branch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HODHomeFragment() {
        // Required empty public constructor
    }

    public static HODHomeFragment newInstance(String name) {
        HODHomeFragment fragment = new HODHomeFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Name = getArguments().getString(NAME);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_h_o_d_home, container, false);

        teacher = rootView.findViewById(R.id.TEACHERVIEW);
        student = rootView.findViewById(R.id.STUDENTVIEW);
        attendance = rootView.findViewById(R.id.ATTENDANCEVIEW);
        textView = rootView.findViewById(R.id.textView);
        result = rootView.findViewById(R.id.imageButton9);

        Intent intent1 = getActivity().getIntent();
        Name = intent1.getStringExtra(NAME);
        textView.setText("Welcome " + Name);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrincipalTeacherView.class);
                startActivity(intent);
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrincipalStudentView.class);
                startActivity(intent);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrincipalAttendanceView.class);
                intent.putExtra(PrincipalAttendanceView.NAME, Name);
                startActivity(intent);
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BatchResult.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
