package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class graphattendance extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphattendance);

        PieChart pieChart= findViewById(R.id.piechart);
        ArrayList<PieEntry> visitors= new ArrayList<>();
        visitors.add(new PieEntry(500,"January"));
        visitors.add(new PieEntry(600,"February"));
        visitors.add(new PieEntry(700,"March"));
        visitors.add(new PieEntry(300,"April"));
        visitors.add(new PieEntry(200,"May"));

        PieDataSet pieDataSet= new PieDataSet(visitors,"visitors");
        int[] colors = ColorTemplate.COLORFUL_COLORS;
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData= new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Visitors");
        pieChart.animate();


    }
}