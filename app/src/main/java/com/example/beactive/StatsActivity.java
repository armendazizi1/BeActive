package com.example.beactive;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    Stats_Database stats_db = new Stats_Database(this);

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        tv = findViewById(R.id.stats_tv);


        List<Integer> stats = new ArrayList<>();
        List<String> stats_days = new ArrayList<>();

        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats.add(res.getInt(0));
            stats_days.add(res.getString(1));

        }


        for (int i = 0; i<stats.size(); i++){
            tv.setText(tv.getText() + "\n" + stats_days.get(i) + " - "+ String.valueOf(stats.get(i))  + "\n");
        }

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StatsActivity.this,MainActivity.class));
        finish();

    }


}
