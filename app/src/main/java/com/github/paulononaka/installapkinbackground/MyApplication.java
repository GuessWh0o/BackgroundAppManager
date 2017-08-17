package com.github.paulononaka.installapkinbackground;

import android.app.Application;
import android.content.Context;

import com.github.paulononaka.installapkinbackground.utils.ApkDownloader;

/**
 * Created by Maks on 8/10/2017.
 */

public class MyApplication extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        ApkDownloader.createApkDirectory();
    }

    public static Context getContext() {
        return context;
    }
}
