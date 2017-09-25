package com.github.paulononaka.installapkinbackground.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.paulononaka.installapkinbackground.ApplicationManager;
import com.github.paulononaka.installapkinbackground.interfaces.OnInstalledPackaged;
import com.github.paulononaka.installapkinbackground.R;
import com.github.paulononaka.installapkinbackground.utils.AppInstalledReceiver;
import com.github.paulononaka.installapkinbackground.utils.AppUninstallReceiver;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    Button btnInstallActivity;
    Button btnUninstallActivity;
    Button btnDownloadActivity;
    AppInstalledReceiver appInstalledReceiver;
    AppUninstallReceiver appUninstallReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  btnInstallActivity = (Button) findViewById(R.id.btnInstallActivity);
        btnUninstallActivity = (Button) findViewById(R.id.btnUninstallActivity);
        btnDownloadActivity = (Button) findViewById(R.id.btnDownloadActivity);


        registerAppInstallReceiver();
        registerAppUninstallReceiver();

        try {
            final ApplicationManager am = new ApplicationManager(MainActivity.this);
            am.setOnInstalledPackaged(new OnInstalledPackaged() {

                public void packageInstalled(String packageName, int returnCode) {
                    if (returnCode == ApplicationManager.INSTALL_SUCCEEDED) {
                        Log.d(TAG, "Install succeeded");
                    } else {
                        Log.d(TAG, "Install failed: " + returnCode);
                    }
                }
            });

//
//            btnInstallActivity.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//
//                    try {
//                        runInstallActivity();
//                       // am.installPackage("/sdcard/test.apk");
//                    } catch (Exception e) {
//                        logError(e);
//                    }
//                }
//            });

            btnUninstallActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    try {
                        runUninstallActivity();
                        //am.uninstallPackage("manager.test.com.testapp");
                    } catch (Exception e) {
                        logError(e);
                    }
                }
            });

            btnDownloadActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    try {
                        runDownloadActivity();
                    } catch (Exception e) {
                        logError(e);
                    }
                }
            });



        } catch (Exception e) {
            logError(e);
        }
    }

    private void runInstallActivity() {
        startActivity(new Intent(MainActivity.this, InstallActivity.class));
    }

    private void runUninstallActivity() {
        startActivity(new Intent(MainActivity.this, UninstallActivity.class));
    }

    private void runDownloadActivity() {
        startActivity(new Intent(MainActivity.this, DownloadActivity.class));
    }

    private void registerAppInstallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addDataScheme("package");
        registerReceiver(appInstalledReceiver, intentFilter);
    }

    private void registerAppUninstallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(appUninstallReceiver, intentFilter);
    }

    private void logError(Exception e) {
        e.printStackTrace();
        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
