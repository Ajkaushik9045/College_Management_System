package com.example.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AttendanceFragment extends Fragment {
    public static final String NAME = "name";
    String name;

    Button take, delete, check, NSO;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static AttendanceFragment newInstance(String name) {
        AttendanceFragment fragment = new AttendanceFragment();
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        take = view.findViewById(R.id.TAKEATTENDANCE);
        delete = view.findViewById(R.id.DELETEATTENDANCE);
        check = view.findViewById(R.id.CHECKATTENDANCE);
        NSO = view.findViewById(R.id.NSOSTUDENTS);

        if (getArguments() != null) {
            name = getArguments().getString(NAME);
        }


        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TakeAttendance.class);
                intent.putExtra(TakeAttendance.NAME, name);
                startActivity(intent);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckAttendance.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeleteAttendance.class);
                startActivity(intent);
            }
        });

        NSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NSOStudents.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
