package com.at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.at.test.utils.ProcessUtil;

public class Process2ChildActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Process2Activity.TAG, ProcessUtil.getProcessIdStr());
    }
}
