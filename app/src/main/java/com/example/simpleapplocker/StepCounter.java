package com.example.simpleapplocker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounter extends AppCompatActivity implements SensorEventListener {


    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.StepCounter";

    private final String TOTAL_KEY = "1234";
    private final String LAST_KEY = "4321";


    SensorManager sensorManager;
    int myCount = 0;

    TextView tv_steps;

    boolean running = false;
    private int totalCounter;
    private int lastCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

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

        totalCounter = mPreferences.getInt(TOTAL_KEY, 0);
        lastCounter = mPreferences.getInt(LAST_KEY, 0);

        int increase = totalCounter - lastCounter;
        myCount += increase;

    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        lastCounter = totalCounter;


        // saving the data
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(TOTAL_KEY, totalCounter);
        preferencesEditor.putInt(LAST_KEY, lastCounter);
        preferencesEditor.apply();


        //comment this if you dont want to stop listening for step counts
        // sensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        myCount++;
//      tv_steps.setText(String.valueOf(myCount));

        tv_steps.setText(String.valueOf(event.values[0]));
//      Toast.makeText(this,String.valueOf(myCount),Toast.LENGTH_SHORT).show();
        totalCounter = (int) event.values[0];

        Intent intent = new Intent(getBaseContext(), LockScreen.class);
        intent.putExtra("counts", event.values[0]);
        startActivity(intent);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(StepCounter.this,MainActivity.class));
        finish();

    }
}
