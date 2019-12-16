package com.example.beactive;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class StatsActivity extends AppCompatActivity {

    Stats_Database stats_db = new Stats_Database(this);

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
//        tv = findViewById(R.id.stats_tv);


        List<Integer> stats = new ArrayList<>();
        List<String> stats_days = new ArrayList<>();

        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats.add(res.getInt(0));
            stats_days.add(res.getString(1));

        }


        for (int i = 0; i<stats.size(); i++){
//            tv.setText(tv.getText() + "\n" + stats_days.get(i) + " - "+ String.valueOf(stats.get(i))  + "\n");
        }

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(8, 0));
//        entries.add(new BarEntry(2, 1));
//        entries.add(new BarEntry(5, 2));
//        entries.add(new BarEntry(20, 3));
//        entries.add(new BarEntry(15, 4));
//        entries.add(new BarEntry(19, 5));
//        entries.add(new BarEntry(199, 6));

        int sum = 0;
        for (int i = 0; i<7; i++){
            sum+= stats.get(i);
            entries.add(new BarEntry(stats.get(i), i));
//            tv.setText(tv.getText() + "\n" + stats_days.get(i) + " - "+ String.valueOf(stats.get(i))  + "\n");
        }

        BarDataSet bardataset = new BarDataSet(entries, "Days");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");
        labels.add("Sun");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("AVG Step counts per day: "+ sum/7);  // set the description
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(2000);

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StatsActivity.this,MainActivity.class));
        finish();

    }


}
