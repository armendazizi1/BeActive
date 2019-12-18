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


    SensorManager sensorManager;
    boolean running = false;

    // the unlock button when you open a locked app
    Button btn;
    TextView status;
    public int v_int;

    private int step_counter = 0;
    private int stats_count = 0;

    Stats_Database stats_db = new Stats_Database(this);


    private int total_count_so_dar = 0;
    private int today;

    String v1, v2;

    public LockScreen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        btn = (Button) findViewById(R.id.unlock_btn);
        status = (TextView) findViewById(R.id.App_name);

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

                // this is the step count goal
                v_int = (int) Integer.parseInt(v1);

                // check if the user has reached their goal
                if (v_int <= step_counter) {

                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(v2);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(launchIntent);
                    finish();
                    Toast.makeText(LockScreen.this, "You reached your Goal!", Toast.LENGTH_LONG).show();

                    // Otherwise show a Toast with the number of steps required to reach the goal
                } else {
                    Toast.makeText(LockScreen.this, (v_int - step_counter) + " steps to go", Toast.LENGTH_LONG).show();
                }

            }

        });


        // get the database stats
        Cursor res = stats_db.getAllData();
        while (res.moveToNext()) {
            stats_count = res.getInt(0);
            total_count_so_dar += stats_count;
            today = res.getInt(0);
        }


        total_count_so_dar -= today;


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

        step_counter = (int) Double.parseDouble(String.valueOf(event.values[0])) - total_count_so_dar;

        // get the current day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int current_day = cal.get(Calendar.DAY_OF_WEEK);

        // if the day has changed, save the number of steps of that day in the database
        // and update the today to equal the current day.
        if (current_day != today) {
            stats_db.updateData(current_day, step_counter);
            total_count_so_dar += step_counter;
            stats_db.updateData(11, current_day);
            today = current_day;
        }


    }

    // for the step counter
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Launch Home Screen after pressing the back button
    @Override
    public void onBackPressed() {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();

    }

}
