package com.github.paulononaka.installapkinbackground.utils;

import android.os.Looper;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Maks on 8/17/2017.
 */

public class S3DownloadRunnable implements Runnable, TransferListener, IS3StallListener {

    private static final String TAG = "S3DownloadRunnable";

    private String key;
    private File destination;
    private CountDownLatch doneSignal;
    private S3Storage s3;
    private boolean complete;
    private SafetyCatchRunnable safetyCatchRunnable;


    S3DownloadRunnable(String key, File destination, CountDownLatch doneSignal, S3Storage s3) {
        this.key = key;
        this.destination = destination;
        this.doneSignal = doneSignal;
        this.s3 = s3;
        complete = false;
    }

    @Override
    public void run() {
        Looper.prepare();
        s3.transferUtility.download(s3.bucketName, key, destination).setTransferListener(this);
    }

    @Override
    public void onStateChanged(int id, TransferState state) {

        switch (state) {
            case COMPLETED:
                s3.setSuccess(true);
                complete = true;
                break;
            case CANCELED:
            case FAILED:
                s3.setSuccess(false);
                complete = true;
                break;
            case PAUSED:
                s3.transferUtility.cancel(id);
                complete = false;
                break;
        }
        if (complete) {
            if (safetyCatchRunnable != null) {
                safetyCatchRunnable.setRun(false);
            }
            doneSignal.countDown();
        }
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
        if (bytesCurrent != 0 && bytesTotal == bytesCurrent) {
            if (safetyCatchRunnable == null) {
                safetyCatchRunnable = new SafetyCatchRunnable(this, id);
                Thread thread = new Thread(safetyCatchRunnable);
                thread.start();
            }
        }
    }

    @Override
    public void onError(int id, Exception ex) {
        Log.d(TAG, "onError() called with: id = [" + id + "], ex = [" + ex + "]");
        s3.setSuccess(false);
        doneSignal.countDown();
    }

    @Override
    public void onCallbackStalled(int downloadId) {
        Log.d(TAG, "OnCallbackStalled: DOWNLOAD ID IS: " + downloadId);
        s3.setSuccess(true);
        doneSignal.countDown();
    }


    /* The S3 onStateChanged callbacks can fail to fire, causing the method never returns in the main sync loop.
    * this will post with a delay of 1 minute every time the progress of the download should be 100%
    * If the onStateChanged callback does not call COMPLETE within a minute, this runnable will cause the
    * countDownLatch to fire and the sync should resume */
    private static class SafetyCatchRunnable implements Runnable {

        private WeakReference<IS3StallListener> stallListenerWeakRef;
        private boolean run;
        int downloadId;

        SafetyCatchRunnable(IS3StallListener s3StallListener, int downloadId) {
            this.stallListenerWeakRef = new WeakReference<>(s3StallListener);
            this.downloadId = downloadId;
            this.run = true;
        }

        public synchronized boolean isRun() {
            return run;
        }

        public synchronized void setRun(boolean run) {
            this.run = run;
        }

        @Override
        public void run() {
            int count = 0;
            while (count < 50) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: ", e);
                }

                if (!isRun()) {
                    return;
                }
                count++;
            }

            if (stallListenerWeakRef.get() != null) {
                stallListenerWeakRef.get().onCallbackStalled(downloadId);
            }
        }
    }
}
