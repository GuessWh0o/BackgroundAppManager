package com.github.paulononaka.installapkinbackground.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.github.paulononaka.installapkinbackground.R;

import java.io.File;

/**
 * Created by Maks on 8/17/2017.
 */

public class ActivityTestAmazon extends AppCompatActivity {

    Button buttonUpload;
    Button buttonDownload;

    private final String bucketName = "activaire-androidapps";

    File fileToUpload = new File("/storage/sdcard0/Pictures/Screenshots/photos.png");
    File fileToDownload = new File("/storage/sdcard0/Pictures/My");
    AmazonS3 s3;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_amazon);

        buttonUpload = (Button) findViewById(R.id.btnUploadToS3);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileToUpload(v);
            }
        });

        buttonDownload = (Button) findViewById(R.id.btnDownloadFromS3);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileToDownload(v);
            }
        });

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();
    }

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:4d05a25d-14b6-44a0-96fd-d3477f357011", // Identity Pool ID
        Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     * @param view
     */
    public void setFileToUpload(View view){

        TransferObserver transferObserver = transferUtility.upload(
                "test.numetric",     /* The bucket to upload to */
                "photos.png",       /* The key for the uploaded object */
        fileToUpload       /* The file where the data to upload exists */
        );
        transferObserverListener(transferObserver);
    }
    public void setFileToDownload(View view){

        TransferObserver transferObserver = transferUtility.download(
                bucketName,     /* The bucket to download from */
                "My",    /* The key for the object to download */
        fileToDownload        /* The file to download the object to */
        );

        transferObserverListener(transferObserver);

    }

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error",ex.getMessage());
            }
        });
    }
}
