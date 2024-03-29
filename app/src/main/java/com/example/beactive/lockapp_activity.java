package com.example.beactive;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class lockapp_activity extends AppCompatActivity {


    private List<Model> appList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListofAppAdapter mAdapter;

    // database of locked apps
    Apply_count_on_AppDatabase db = new Apply_count_on_AppDatabase(this);

    // databaae of saved step count goal.
    StepCounts_Database step_count_db = new StepCounts_Database(this);

    List<String> lock = new ArrayList<>();

    String step_count_goal="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockapp_activity);


        Cursor res = db.getAllData();
        Cursor res2 = step_count_db.getAllData();


        while (res.moveToNext()) {
            lock.add(res.getString(0));

        }

        while (res2.moveToNext()) {

            step_count_goal = res2.getString(0);
        }




        recyclerView = (RecyclerView) findViewById(R.id.installed_app_list);

        mAdapter = new ListofAppAdapter(appList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getInstalledApps();
        mAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            // Locks/Unlocks apps with Onclick event.
            @Override
            public void onClick(View view, int position) {

                final Model model = appList.get(position);
                if(model.getLocked() == R.drawable.lock){
                    db.deleteData(model.getPackage_name());
                    Toast.makeText(lockapp_activity.this,   "App UnLocked", Toast.LENGTH_LONG).show();
                    model.setLocked(R.drawable.unlock);
                }
                else{
                    db.insertData(model.getPackage_name(),step_count_goal);
                    Toast.makeText(lockapp_activity.this,   "App Lock Successfully ", Toast.LENGTH_LONG).show();
                    model.setLocked(R.drawable.lock);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        })
        );




    }



    // Retrieves the list of apps installed on the device.
    private List<AppList> getInstalledApps() {
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

          System.out.println("size = "+packs.size());
            if(lock.contains(packs.get(i).packageName)){

                if ((isSystemPackage(p) == false)) {
                    String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                    Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                    appList.add(new Model(appName, icon,R.drawable.lock,packs.get(i).packageName) );
                }

            }

            else{
                if ((isSystemPackage(p) == false)) {
                    String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                    Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                    appList.add(new Model(appName, icon,R.drawable.unlock,packs.get(i).packageName) );
                }
            }





        }
        return res;
    }

    // checks if the app is a systemPackage
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    // returns to MainActivity on Back pressed.
    @Override
    public void onBackPressed() {

        startActivity(new Intent(lockapp_activity.this, MainActivity.class));
        finish();

    }
}
