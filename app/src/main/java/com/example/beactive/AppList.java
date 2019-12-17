package com.example.beactive;

import android.graphics.drawable.Drawable;

public class AppList {

    // class for list of app ( it contains name or stats_icon )

    private String name;
    Drawable icon;
    int locked;
    String package_name;

    public AppList(String name, Drawable icon, int locked, String package_name) {
        this.name = name;
        this.icon = icon;
        this.locked = locked;
        this.package_name = package_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}