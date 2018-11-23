package com.at.test.sliding;

import android.os.Bundle;

import com.at.test.R;
import com.at.test.base.BaseActivity;


public class TestActivity extends BaseActivity {

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onDestroyImpl() {

    }
}
