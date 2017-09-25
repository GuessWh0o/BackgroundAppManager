package com.github.paulononaka.installapkinbackground.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.github.paulononaka.installapkinbackground.R;
import com.github.paulononaka.installapkinbackground.utils.Constants;
import com.github.paulononaka.installapkinbackground.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Maks on 8/18/2017.
 */

public class DownloadSelectionActivity extends ListActivity {
    // The S3 client used for getting the list of objects in the bucket
    private AmazonS3Client s3;
    // An adapter to show the objects
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_selection);
        initData();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the file list.
        new GetFileListTask().execute();
    }

    private void initData() {
        // Gets the default S3 client.
        s3 = Util.getS3Client(DownloadSelectionActivity.this);
        transferRecordMaps = new ArrayList<HashMap<String, Object>>();
    }

    private void initUI() {
        simpleAdapter = new SimpleAdapter(this, transferRecordMaps,
                R.layout.bucket_item, new String[]{
                "key"
        },
                new int[]{
                        R.id.key
                });
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                switch (view.getId()) {
                    case R.id.key:
                        TextView fileName = (TextView) view;
                        fileName.setText((String) data);
                        return true;
                }
                return false;
            }
        });
        setListAdapter(simpleAdapter);

        // When an item is selected, finish the activity and pass back the S3
        // key associated with the object selected
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("key", (String) transferRecordMaps.get(position).get("key"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    /**
     * This async task queries S3 for all files in the given bucket so that they
     * can be displayed on the screen
     */
    class GetFileListTask extends AsyncTask<Void, Void, Void> {
        // The list of objects we find in the S3 bucket
        private List<S3ObjectSummary> s3ObjList;
        // A dialog to let the user know we are retrieving the files
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(DownloadSelectionActivity.this,
                    getString(R.string.refreshing),
                    getString(R.string.please_wait));
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            // Queries files in the bucket from S3.
            try {
                s3ObjList = s3.listObjects(Constants.BUCKET_NAME).getObjectSummaries();
                transferRecordMaps.clear();
//                for (S3ObjectSummary summary : s3ObjList) {
//                    HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("key", summary.getKey());
//                    transferRecordMaps.add(map);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
