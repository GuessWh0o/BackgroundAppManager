package com.github.paulononaka.installapkinbackground.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by Maks on 8/17/2017.
 */

public class MyInternetInfo {
    private Context context;
    private static final String TAG = "MyInternetInfo";
    private static String className;


    public MyInternetInfo(Context context) {
        this.context = context.getApplicationContext();
        className = this.getClass().getSimpleName();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean avail = networkInfo != null && networkInfo.isConnected();
        Log.d(TAG, " isNetworkAvailable() returned: " + avail);
        return avail;
    }
}
