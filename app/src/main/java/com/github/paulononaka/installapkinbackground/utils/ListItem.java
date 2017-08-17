package com.github.paulononaka.installapkinbackground.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by Maks on 8/10/2017.
 */

public class ListItem {
    private Drawable icon;
    private String appName;
    private String packName;

    public ListItem(Drawable icon, String appName, String packName) {
        this.icon = icon;
        this.appName = appName;
        this.packName = packName;

    }

    public Drawable getIcon() {
        return icon;
    }

    public String getAppName() {
        return appName;
    }


    public String getPackName() {
        return packName;
    }

    public boolean isIconNull() {
        return icon == null;
    }
}
