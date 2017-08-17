package com.github.paulononaka.installapkinbackground.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.paulononaka.installapkinbackground.R;

/**
 * Created by Maks on 8/10/2017.
 */

public class InstallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);

        findViewById(R.id.add_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addApp();
            }
        });
    }

    private void addApp() {
        startActivity(new Intent(this, EditActivity.class));
    }
}
