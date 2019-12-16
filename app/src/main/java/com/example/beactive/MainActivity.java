package com.example.beactive;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button lockapp , update_pass;
    Counts_Database db = new Counts_Database(this);
    List<String> pass = new ArrayList<>();
    String m_Text="";
    Context cn;



    // intent is used to call background services
    Intent mServiceIntent;
    // NotificationService is for background service class
    private BackgroundServices mYourService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation


        lockapp = findViewById(R.id.lock_app_btn);
        update_pass = findViewById(R.id.update_pass_btn);

        cn = this;

        Cursor res = db.getAllData();
        while (res.moveToNext()) {
            pass.add(res.getString(0));
        }

        lockapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pass.isEmpty()){
                    Toast.makeText(MainActivity.this,   "Firs,t set your steps goal ", Toast.LENGTH_LONG).show();

                }
                else {


                    startActivity(new Intent(MainActivity.this,lockapp_activity.class));
                    finish();

                }

            }
        });

        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(cn);
                builder.setTitle("Set steps goal ");

                // Set up the input
                final EditText input = new EditText(cn);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
//                builder.setView(input);

                // Number Picker

                final NumberPicker input2 = new NumberPicker(cn);
                input2.setMaxValue(11);
                input2.setMinValue(0);

               final String[] pickerVals  = new String[] {"1", "5", "10", "20", "40","60","80","100","500","1000","5000","10000"};
               input2.setDisplayedValues(pickerVals);

                builder.setView(input2);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = pickerVals[input2.getValue()];

                        if(m_Text.isEmpty()){

                            Toast.makeText(MainActivity.this,   "Steps goal cannot be empty", Toast.LENGTH_LONG).show();
                        }
                        else{

                            int zz = db.deleteData(m_Text);
                            db.insertData(m_Text);

                            Toast.makeText(MainActivity.this,   "Steps goal set successfully " +m_Text , Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                            finish();

                        }






                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();




            }
        });





    }



    // check your background services
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();

        if(!isAccessGranted()){
            new AlertDialog.Builder(this)
                    .setTitle("USAGE_STATS Permission")
                    .setMessage("Allow USAGE_STATS Permission in Setting")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // action
                            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .show();
        }
        else if(pass.isEmpty()){


            update_pass.setText("Set number of steps");

            AlertDialog.Builder builder = new AlertDialog.Builder(cn);
            builder.setTitle("Set steps goal ");



             // Set up the input
            final EditText input = new EditText(cn);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
//            builder.setView(input);

            final NumberPicker input2 = new NumberPicker(cn);
            input2.setMaxValue(11);
            input2.setMinValue(0);

            final String[] pickerVals  = new String[] {"1", "5", "10", "20", "40","60","80","100","500","1000","5000","10000"};
            input2.setDisplayedValues(pickerVals);

            builder.setView(input2);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = pickerVals[input2.getValue()];

                    if(m_Text.isEmpty()){

                        Toast.makeText(MainActivity.this,   "Steps goal can't be empty", Toast.LENGTH_LONG).show();
                    }
                    else{
                      boolean tt =  db.insertData(m_Text);
                      pass.add(m_Text);
                      Toast.makeText(MainActivity.this,   "Steps goal set successfully "+m_Text, Toast.LENGTH_LONG).show();
                      update_pass.setText("Update steps goal ");

                    }


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();



        }


            // run background services
            mYourService = new BackgroundServices();
            mServiceIntent = new Intent(this, mYourService.getClass());
            // services not running already
            // start services
            if (!isMyServiceRunning(mYourService.getClass())) {
                startService(mServiceIntent);
            }



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // Method for the showCounter button
    public void showCounter(View view) {
        startActivity(new Intent(MainActivity.this,StepCounter.class));
        finish();
    }

    public void showBadges(View view) {
        startActivity(new Intent(MainActivity.this, BadgeActivity.class));
        finish();
    }

    public void showStats(View view) {
        startActivity(new Intent(MainActivity.this, StatsActivity.class));
        finish();
    }
}
