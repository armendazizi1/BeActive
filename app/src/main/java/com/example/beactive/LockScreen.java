package com.example.beactive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class LockScreen extends AppCompatActivity implements SensorEventListener {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.StepCounter";
    SensorManager sensorManager;
    boolean running = false;

    Button btn;
    TextView status;
    public int v_int;

    public int step_counter = 0;
    private int stats_count = 0;

    Stats_Database stats_db = new Stats_Database(this);

    private int total_count_so_dar = 0;

    private int minutes;

    String v1, v2;

    public LockScreen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        btn = (Button) findViewById(R.id.unlock_btn);
        status = (TextView) findViewById(R.id.App_name);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // v1 and v2 get data from background services
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            v1 = extras.getString("steps");
            v2 = extras.getString("pack");


            //The key argument here must match that used in the other activity
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                v_int = (int) Integer.parseInt(v1);

//                if (v1.equals(t1.getText().toString())) {
                if (v_int <= step_counter) {

                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(v2);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(launchIntent);
                    finish();
                    Toast.makeText(LockScreen.this, "You reached your Goal!", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(LockScreen.this, (v_int - step_counter) + " steps to go", Toast.LENGTH_LONG).show();
//                    t1.setText("");
                }

            }

        });


        // get the database stats

        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats_count = res.getInt(0);
            total_count_so_dar += stats_count;
        }


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        minutes = cal.get(Calendar.MINUTE);


    }

    // For the step counter
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

    // For the step counter
    @Override
    protected void onPause() {
        super.onPause();
        running = false;


        //comment this if you dont want to stop listening for step counts
        // sensorManager.unregisterListener(this);

    }

    // for the step counter
    @Override
    public void onSensorChanged(SensorEvent event) {

//        Toast.makeText(this,String.valueOf(event.values[0]),Toast.LENGTH_SHORT).show();
        step_counter = (int) Double.parseDouble(String.valueOf(event.values[0])) - total_count_so_dar;


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int current_minutes = cal.get(Calendar.MINUTE);

        // update the counter after 2 minutes
        if (current_minutes != minutes) {
//            stats_db.insertData(step_counter);
            total_count_so_dar += step_counter;
//            Toast.makeText(this, String.valueOf(current_minutes), Toast.LENGTH_SHORT).show();
            minutes = current_minutes;
        }


    }

    // for the step counter
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
