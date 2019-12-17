package com.example.beactive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BadgeActivity extends AppCompatActivity implements SensorEventListener {

    private String sharedPrefFile = "com.example.StepCounter";
    SensorManager sensorManager;
    boolean running = false;
    private float total_steps = 100;
    TextView tot_steps;
    TextView tot_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tot_steps = findViewById(R.id.total_steps);
        tot_distance = findViewById(R.id.Total_distance);

        tot_steps.setText("Total number of steps: " + (int) total_steps);
        tot_distance.setText("Distance travelled so far: " +(int)(total_steps*1.6) +" m");

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


    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;


        //comment this if you dont want to stop listening for step counts
        // sensorManager.unregisterListener(this);

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(BadgeActivity.this,MainActivity.class));
        finish();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        this.total_steps = event.values[0];
        int b = (int) event.values[0];
        if (b > 80) {
            ImageView goldenBridge = findViewById(R.id.GBridge);
            goldenBridge.setImageResource(R.drawable.bridge_unlock);
        }
        if (b > 90) {
            ImageView goldenBridge = findViewById(R.id.Bahamas);
            goldenBridge.setImageResource(R.drawable.beach_unlock);
        }
        if (b > 100) {
            ImageView goldenBridge = findViewById(R.id.Route66);
            goldenBridge.setImageResource(R.drawable.desert_unlock);
        }
        if (b > 110) {
            ImageView goldenBridge = findViewById(R.id.K2);
            goldenBridge.setImageResource(R.drawable.mountain_unlock);
        }
        if (b > 120) {
            ImageView goldenBridge = findViewById(R.id.Waterfall);
            goldenBridge.setImageResource(R.drawable.waterfall_unlock);
        }
        if (b > 130) {
            ImageView goldenBridge = findViewById(R.id.Sahara);
            goldenBridge.setImageResource(R.drawable.desert_unlock_2);
        }



        if (b > 2737 * 1.6) {
            ImageView goldenBridge = findViewById(R.id.GBridge);
            goldenBridge.setImageResource(R.drawable.bridge_unlock);
        }
        if (b > 2737 * 1.6) {
            ImageView goldenBridge = findViewById(R.id.Bahamas);
            goldenBridge.setImageResource(R.drawable.beach_unlock);
        }
        if (b > 2737 * 1.6) {
            ImageView goldenBridge = findViewById(R.id.Route66);
            goldenBridge.setImageResource(R.drawable.desert_unlock);
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
