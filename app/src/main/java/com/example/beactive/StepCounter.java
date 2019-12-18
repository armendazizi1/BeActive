package com.example.beactive;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private ProgressBar progressBarra;


    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.StepCounter";


    private final String COUNT_KEY = "4661";

    Context mContext;

    SensorManager sensorManager;
    int myCount;

    TextView tv_steps;

    boolean running = false;
    private int totalCounter;
    TextView tv_info2;

    StepCounts_Database pass_db = new StepCounts_Database(this);

    String pass = "";

    Stats_Database stats_db = new Stats_Database(this);

    private int stats_count = 0;

    private int total_count_so_dar = 0;
    private int today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_step_counter);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tv_info2 = findViewById(R.id.tv_info2);


        Cursor res2 = pass_db.getAllData();


        while (res2.moveToNext()) {

            pass = res2.getString(0);
        }


        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats_count = res.getInt(0);
            total_count_so_dar += stats_count;
            today = res.getInt(0);
        }


        total_count_so_dar -= today;
        // we added +10 to the current_day to distinguish it from the other days.


        progressBarra = findViewById(R.id.progressBarra);
        progressBarra.setMax(Integer.parseInt(pass));
        progressBarra.setSecondaryProgress(Integer.parseInt(pass));

        tv_info2.setText("Your Goal: " + pass);


    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);

        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }


        myCount = mPreferences.getInt(COUNT_KEY, 0) + (totalCounter - myCount);

    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;


        // saving the data
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY, myCount);
        preferencesEditor.apply();


        //comment this if you dont want to stop listening for step counts
        // sensorManager.unregisterListener(this);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        totalCounter = (int) event.values[0] - total_count_so_dar;
        tv_steps.setText(String.valueOf(totalCounter));

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int current_minutes = cal.get(Calendar.MINUTE);
        int current_day = cal.get(Calendar.DAY_OF_WEEK);

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        if (current_day != today) {
            stats_db.updateData(current_day, totalCounter);
            total_count_so_dar += totalCounter;


            // again we add +10 to distinguish from the other days
            stats_db.updateData(11, current_day );
            today = current_day;
        }

        progressBarra.setProgress(totalCounter);
        progressBarra.setSecondaryProgress(Integer.parseInt(pass));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StepCounter.this, MainActivity.class));
        finish();

    }
}
