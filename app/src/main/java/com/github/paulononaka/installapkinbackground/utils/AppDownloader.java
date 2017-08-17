package com.github.paulononaka.installapkinbackground.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.github.paulononaka.installapkinbackground.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Maks on 8/10/2017.
 */

public class AppDownloader {
    private static final String TAG = AppDownloader.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;
    private static final String BASE_DIR = "apk";
    private URL mUrl;
    private FileEntry mFileEntry;
    private Context mContext;

    public static class DownloadResponse {
        public int responseCode;
        public String errorMessage;
        public String downloadedApkPath;
    }

    /**
     * Create a new downloader with URL.<br />
     * If a malformed URL is passed, this constructor
     * throws {@linkplain java.lang.IllegalArgumentException}.
     */
    public AppDownloader(Context context, FileEntry fileEntry) {
        mFileEntry = fileEntry;
        mContext = context;
        try {
            mUrl = new URL(fileEntry.url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format", e);
        }
    }

    /**
     * Download the specified file and save to app local storage.
     *
     * @param baseContext Base context to access app local storage
     * @return Downloaded APK file path
     * @throws java.io.IOException If downloading failed
     */
    public DownloadResponse download(final Context baseContext) throws IOException {
        DownloadResponse response = new DownloadResponse();
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) mUrl.openConnection();

            // Optionally use basic auth
            if (!TextUtils.isEmpty(mFileEntry.basicAuthUser) && !TextUtils.isEmpty(mFileEntry.basicAuthPassword)) {
                c.setRequestProperty("Authorization",
                        "Basic " + base64Encode(mFileEntry.basicAuthUser + ":" + mFileEntry.basicAuthPassword));
            }

            c.setRequestMethod("GET");
            ContextWrapper cw = new ContextWrapper(baseContext);
            File dir = new File(cw.getExternalFilesDir(null), BASE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            final String apkName = extractFileName(mFileEntry.url);
            File outputFile = new File(dir, apkName);
            c.connect();
            updateHeaderValues(c);

            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fos.close();
            is.close();

            response.downloadedApkPath = outputFile.getAbsolutePath();
        } catch (UnknownHostException e) {
            response.errorMessage = mContext.getString((R.string.error_unknown_host));
        } catch (IOException e) {
            if (c != null) {
                response.responseCode = c.getResponseCode();
                switch (response.responseCode) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        response.errorMessage = mContext.getString(R.string.error_unauthorized);
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        response.errorMessage = mContext.getString(R.string.error_not_found);
                        break;
                    default:
                        response.errorMessage = mContext.getString(R.string.error_download);
                        break;
                }
            }
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }

        return response;
    }

    private String extractFileName(String url) {
        Uri uri = Uri.parse(url);
        List<String> paths = uri.getPathSegments();
        return paths.get(paths.size() - 1);
    }

    private void updateHeaderValues(HttpURLConnection c) {
        mFileEntry.headerLastModified = c.getHeaderField("Last-Modified");
        mFileEntry.headerEtag = c.getHeaderField("Etag");
        mFileEntry.headerContentLength = c.getHeaderField("Content-Length");

        Log.d(TAG, "updateHeaderValues() is accessing by GET method to " + c.getURL().toString());
        Log.d(TAG, "lastModified: " + mFileEntry.headerLastModified);
        Log.d(TAG, "etag: " + mFileEntry.headerEtag);
        Log.d(TAG, "contentLength: " + mFileEntry.headerContentLength);

      //TODO:  new FileEntryDao(mContext).updateHeader(mFileEntry);
    }

    /**
     * check APK was updated
     * <p/>
     * 1. check Last-Modified
     * 2. check Etag if it is not found Last-Modified
     * 3. check Content-Length if is not found Etag
     * <p/>
     * this alg not use If-Modified-Since
     *
     * @return return true if APK was updated, otherwise false
     */
    public boolean needToUpdate(FileEntry fe) throws IOException {
        HttpURLConnection c = (HttpURLConnection) mUrl.openConnection();

        // Optionally use basic auth
        if (!TextUtils.isEmpty(mFileEntry.basicAuthUser) && !TextUtils.isEmpty(mFileEntry.basicAuthPassword)) {
            c.setRequestProperty("Authorization",
                    "Basic " + base64Encode(mFileEntry.basicAuthUser + ":" + mFileEntry.basicAuthPassword));
        }

        c.setRequestMethod("HEAD");
        c.connect();
        try {
            String lastModified = c.getHeaderField("Last-Modified");
            String etag = c.getHeaderField("Etag");
            String contentLength = c.getHeaderField("Content-Length");

            Log.d(TAG, "needToUpdate() is accessing by HEAD method to " + fe.url);
            Log.d(TAG, "lastModified: " + lastModified);
            Log.d(TAG, "etag: " + etag);
            Log.d(TAG, "contentLength: " + contentLength);

            if (!TextUtils.isEmpty(fe.headerLastModified)) {
                if (fe.headerLastModified.equals(lastModified)) {
                    return false;
                } else {
                    return true;
                }
            } else if (!TextUtils.isEmpty(fe.headerEtag)) {
                if (fe.headerEtag.equals(etag)) {
                    return false;
                } else {
                    return true;
                }
            } else if (!TextUtils.isEmpty(fe.headerContentLength)) {
                if (fe.headerContentLength.equals(contentLength)) {

                    return false;
                } else {
                    return true;
                }
            }
        } finally {
            c.disconnect();
        }
        return true;
    }

    private String base64Encode(final String in) {
        return Base64.encodeToString(in.getBytes(), Base64.DEFAULT);
    }
}
