package com.at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.at.test.R;
import com.at.test.utils.ProcessUtil;

public class Process2Activity extends Activity {

    public static final String TAG = "Process2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, ProcessUtil.getProcessIdStr());
        setContentView(R.layout.activity_process2);
        findViewById(R.id.test_open_process2child).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Process2Activity.this, Process2ChildActivity.class);
                startActivity(intent);
            }
        });
    }
}
