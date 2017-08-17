package com.github.paulononaka.installapkinbackground.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Maks on 8/10/2017.
 */

public class InstallTask extends AsyncTask<String, Void, AppDownloader.DownloadResponse> {

    public interface InstallListener {
        void onComplete(final AppDownloader.DownloadResponse response);
    }

    private static final String TAG = InstallTask.class.getSimpleName();
    private InstallListener mListener;
    private Activity mActivity;
    private FileEntry mFileEntry;

    public void setListener(final InstallListener listener) {
        mListener = listener;
    }

    public InstallTask(final Activity activity, FileEntry entry) {
        mActivity = activity;
        mFileEntry = entry;
    }

    @Override
    protected AppDownloader.DownloadResponse doInBackground(final String... strings) {
        try {
            AppDownloader downloader = new AppDownloader(mActivity, mFileEntry);
            return downloader.download(mActivity);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(AppDownloader.DownloadResponse r) {
        super.onPostExecute(r);
        if (mListener != null) {
            mListener.onComplete(r);
        }
        mActivity.finish();
    }
}
