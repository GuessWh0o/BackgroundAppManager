package com.github.paulononaka.installapkinbackground.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.github.paulononaka.installapkinbackground.MyApplication;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Maks on 8/17/2017.
 */

public class S3Storage {

    private static final String TAG = "S3Storage";
    private AmazonS3 s3;
    private CognitoCachingCredentialsProvider credentialsProvider;
    TransferUtility transferUtility;
    String bucketName;
    ApkDownloader apkDonwloader;
    private Context context;
    private static final String IDENTITY_POOL_ID = "us-east-1:4d05a25d-14b6-44a0-96fd-d3477f357011";
    private volatile boolean success;

    public S3Storage(String bucketName, ApkDownloader apkDonwloader) {
        context = MyApplication.getContext();
        this.bucketName = bucketName;
        this.apkDonwloader = apkDonwloader;
        setUp();
    }

    private void initProvider() {
        credentialsProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, Regions.US_EAST_1);
    }

    private void initS3() {
        s3 = new AmazonS3Client(credentialsProvider);
    }

    private void initTransferUtility() {
        transferUtility = new TransferUtility(s3, context);
    }

    private void setUp() {
        initProvider();
        initS3();
        initTransferUtility();
    }

    void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean donwloadApk2(String key, File destination) {
        success = false;
        CountDownLatch doneSignal = new CountDownLatch(1);
        Thread thread = new Thread(new S3DownloadRunnable(key, destination, doneSignal, this));
        thread.start();

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "downloadFile2: ", e);
        }

       Log.d(TAG, "downloadFile2() returned: " + success);
        return success;
    }
}
