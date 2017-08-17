package com.github.paulononaka.installapkinbackground.utils;

import android.content.Context;
import android.util.Log;

import com.github.paulononaka.installapkinbackground.MyApplication;

import java.io.File;

/**
 * Created by Maks on 8/17/2017.
 */

public class ApkDownloader {
    private static final String TAG = "ApkDownloader";
    protected Context context;
    private static String pathToApk;
    IDownloadEventListener downloadEventListener;
    private String bucketName;  //TODO: BucketName

    private S3Storage s3Storage;

    public ApkDownloader(Context context) {
        this.context = context;
        initAllVariables();
    }

    private void initAllVariables() {
        bucketName = "BUCKET NAME";

        s3Storage = new S3Storage(bucketName, this);
    }

    private boolean verifyApkInstallerOnDevice(String apkName, File localFile) {
        boolean apkExists = false;


        return apkExists;
    }

    public static void createApkDirectory() {
        File dir = new File(MyApplication.getContext().getExternalFilesDir(null), "/APK");
        boolean directoryExists = dir.mkdirs() || dir.isDirectory();
        try {
            if (directoryExists) {
                pathToApk = dir.getAbsolutePath();
                Log.d(TAG, "createApkDirectory: IS CREATED ... " + dir);
                Log.d(TAG, "createApkDirectory: PATH " + pathToApk);
            } else {
                Log.d(TAG, "createApkDirectory: IS NOT CREATED ... " + dir);
                Log.d(TAG, "createApkDirectory: PATH " + pathToApk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPathToApk() {
        return pathToApk;
    }

    private boolean donwloadApk(String apkName) {
        if (!checkNetwork()) {
            return false;
        }

        boolean downloadSuccessful = false;
        File localFile = new File(ApkDownloader.getPathToApk() + "/" + apkName);
        downloadEventListener.onDownloadStatusChanged("Downloading: " + apkName);
        Log.d(TAG, "donwloadApk2: " + apkName);

        try {
            if (s3Storage.donwloadApk2(apkName, localFile)) {
                //TODO: GET MD5 from local and server file
                downloadSuccessful = true;
            } else {
                Log.d(TAG, "donwloadApk: DOWNLOAD FAILED " + apkName);
                localFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return downloadSuccessful;
    }

    private boolean checkNetwork() {
        NetworkInfo netInfo = new NetworkInfo(context);
        return netInfo.isNetworkAvailable();
    }
}
