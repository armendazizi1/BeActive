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

        stats_db.deleteAll();
        stats_db.insertData(1);
        stats_db.insertData(33);
        stats_db.insertData(44);
        stats_db.insertData(11);
        stats_db.insertData(500);

        List<Integer> stats = new ArrayList<>();

        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats.add(res.getInt(0));

        }


        for (int i: stats){
            tv.setText(tv.getText() + String.valueOf(i)  + "\n");
        }

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StatsActivity.this,MainActivity.class));
        finish();

    }


}
