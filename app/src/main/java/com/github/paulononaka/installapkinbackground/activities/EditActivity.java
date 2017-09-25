package com.github.paulononaka.installapkinbackground.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.paulononaka.installapkinbackground.R;

/**
 * Created by Maks on 8/10/2017.
 */

public class EditActivity extends DetailActivity {

    private static final String DEFAULT_URL = "https://github.com/app-manager/AppManager-for-Android/blob/master/tests/apk/dummy.apk?raw=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        setContentView(R.layout.activity_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mFileEntry = null;

        if (hasFileEntryInIntent()) {
            mFileEntry = getFileEntryFromIntent();
            if (mFileEntry != null) {
                setTitle(R.string.activity_title_edit_app);
                restoreValues(mFileEntry);
            }
        } else {
//TODO:              ((TextView) findViewById(R.id.name)).setText(extractNameFromUrl(DEFAULT_URL));
            ((TextView) findViewById(R.id.url)).setText(DEFAULT_URL);
        }
        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                install();
            }
        });
    }
}
