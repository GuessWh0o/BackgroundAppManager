package com.github.paulononaka.installapkinbackground;

import android.app.Application;
import android.content.Context;

/**
 * Created by Maks on 8/10/2017.
 */

public class MyApplication extends Application {
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

}
