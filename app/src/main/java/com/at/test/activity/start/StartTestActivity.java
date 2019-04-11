package com.at.test.activity.start;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by dqq on 2019/4/10.
 */
public class StartTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartFactory.buildOf(this).orderList("1020200202020");
    }
}
