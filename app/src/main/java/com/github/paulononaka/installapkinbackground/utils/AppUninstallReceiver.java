package com.github.paulononaka.installapkinbackground.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.github.paulononaka.installapkinbackground.MyApplication;

import static com.github.paulononaka.installapkinbackground.activities.MainActivity.TAG;

/**
 * Created by Maks on 8/10/2017.
 */

public class AppUninstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        Log.d(TAG, "onReceive: Uninstalled: " + packageName);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.context, "Application has been uninstalled", Toast.LENGTH_LONG).show();
            }
        });
    }
}
