package com.github.paulononaka.installapkinbackground.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paulononaka.installapkinbackground.R;
import com.github.paulononaka.installapkinbackground.utils.FileEntry;
import com.github.paulononaka.installapkinbackground.utils.MyInternetInfo;

/**
 * Created by Maks on 8/10/2017.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_ENTRY = "fileEntry";
    protected FileEntry mFileEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        setContentView(R.layout.activity_detail);
        mFileEntry = null;

        if (hasFileEntryInIntent()) {
            mFileEntry = getFileEntryFromIntent();
            if (mFileEntry != null && !TextUtils.isEmpty(mFileEntry.name)) {
                setTitle(mFileEntry.name);
                restoreValues(mFileEntry);
            }
        }
        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                install();
            }
        });
    }

    protected void restoreValues(FileEntry entry) {
        ((TextView) findViewById(R.id.name)).setText(entry.name);
        ((TextView) findViewById(R.id.url)).setText(entry.url);
    }

    protected void install() {
        FileEntry entry = getFileEntryFromScreen();
        if (hasFileEntryInIntent()) {
            FileEntry storedEntry = getFileEntryFromIntent();
            entry.basicAuthUser = storedEntry.basicAuthUser;
            entry.basicAuthPassword = storedEntry.basicAuthPassword;
        }

        MyInternetInfo networkInfo = new MyInternetInfo(this);
        if (!networkInfo.isNetworkAvailable()) {
            Toast.makeText(this, R.string.error_no_connected_network, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(entry.name)) {
            entry.name = entry.url;
        }
    }

    protected FileEntry getFileEntryFromScreen() {
        FileEntry entry = new FileEntry();
        if (null != mFileEntry) {
            mFileEntry.copyMetaDataTo(entry);
        }
        entry.url = ((TextView) findViewById(R.id.url)).getText().toString();
        entry.name = ((TextView) findViewById(R.id.name)).getText().toString();
        return entry;
    }

    protected boolean hasFileEntryInIntent() {
        Intent intent = getIntent();
        return intent != null && intent.hasExtra(EXTRA_FILE_ENTRY);
    }

    protected FileEntry getFileEntryFromIntent() {
        return getIntent().getParcelableExtra(EXTRA_FILE_ENTRY);
    }

    private void edit() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.EXTRA_FILE_ENTRY, mFileEntry);
        startActivity(intent);
        finish();
    }

}
