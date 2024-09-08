package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class TeacherHomeFragment extends Fragment {
    ImageButton student, attendance, result,announcement;
    TextView textView;
    String name;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    public static final String NAME = "NAME";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TeacherHomeFragment() {
        // Required empty public constructor
    }

    public static TeacherHomeFragment newInstance(String name) {
        TeacherHomeFragment fragment = new TeacherHomeFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_home, container, false);
        student = rootView.findViewById(R.id.STUDENTVIEW);
        attendance = rootView.findViewById(R.id.ATTENDANCEVIEW);
        textView = rootView.findViewById(R.id.textView);
        result = rootView.findViewById(R.id.imageButton9);
        announcement=rootView.findViewById(R.id.imageButton11);
        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra(NAME);
        textView.setText("Welcome " + name);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TeacherStudentView.class);
                startActivity(intent);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrincipalAttendanceView.class);
                intent.putExtra(PrincipalAttendanceView.NAME, name);
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
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Announcement.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
