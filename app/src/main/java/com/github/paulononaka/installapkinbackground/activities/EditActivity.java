package com.github.paulononaka.installapkinbackground.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.paulononaka.installapkinbackground.utils.InstallTask;
import com.github.paulononaka.installapkinbackground.R;
import com.github.paulononaka.installapkinbackground.utils.AppDownloader;

/**
 * Created by Maks on 8/10/2017.
 */

public class EditActivity extends DetailActivity implements InstallTask.InstallListener {

    private static final String DEFAULT_URL = "https://github.com/app-manager/AppManager-for-Android/blob/master/tests/apk/dummy.apk?raw=true";
    private static final int DIALOG_REQUEST_CODE_SHOW_PASSWORD = 1;
    private static final int DIALOG_REQUEST_CODE_DELETE = 2;
    private static final int DIALOG_REQUEST_CODE_FINISH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        setContentView(R.layout.activity_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mFileEntry = null;

        Button deleteButton = (Button) findViewById(R.id.delete);
        Button showPasswordButton = (Button) findViewById(R.id.show_password);
        if (hasFileEntryInIntent()) {
            mFileEntry = getFileEntryFromIntent();
            if (mFileEntry != null) {
                setTitle(R.string.activity_title_edit_app);
                restoreValues(mFileEntry);
            }
            showPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//TODO:                    confirmShowPassword();
                }
            });
        } else {
//TODO:              ((TextView) findViewById(R.id.name)).setText(extractNameFromUrl(DEFAULT_URL));
            ((TextView) findViewById(R.id.url)).setText(DEFAULT_URL);
            showPasswordButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                install();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:                 save();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:                confirmDelete();
            }
        });
    }

    @Override
    public void onComplete(AppDownloader.DownloadResponse response) {

    }
}
