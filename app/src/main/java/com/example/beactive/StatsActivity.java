package com.example.beactive;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;


// barchart credits: https://medium.com/@karthikganiga007/create-barchart-in-android-studio-14943339a211


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class StatsActivity extends AppCompatActivity {

    Stats_Database stats_db = new Stats_Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        List<Integer> stats = new ArrayList<>();
        List<Integer> stats_days = new ArrayList<>();




// //       update data for the demo
//
//        stats_db.updateData(1,300);
//        stats_db.updateData(2,400);
//        stats_db.updateData(3,355);
//        stats_db.updateData(4,520);
//        stats_db.updateData(5,650);
//        stats_db.updateData(6,340);
//        stats_db.updateData(7,667);
// //       stats_db.updateData(11,5);

        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats.add(res.getInt(0));
            stats_days.add(res.getInt(1));

        }


        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();

        int sum = 0;
        for (int i = 0; (i < (stats.size() - 1)); i++) {
            sum += stats.get(i);
            entries.add(new BarEntry(stats.get(i), i));
        }

        BarDataSet bardataset = new BarDataSet(entries, "Days");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");


        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("AVG Step counts per day: " + sum / 7);  // set the description
        bardataset.setColors(Collections.singletonList(Color.parseColor("#5F8BD3")));
        barChart.animateY(2000);

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StatsActivity.this, MainActivity.class));
        finish();

    }


}
