package com.example.beactive;

import android.graphics.drawable.Drawable;

/* +
    Model of the installed apps List, to perform the Lock/Unlock actions.
 */
public class Model {

    private String name;
    Drawable icon;
    int locked;
    String package_name;

    public Model(String name, Drawable icon, int locked, String package_name) {
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}
