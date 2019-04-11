package com.at.test;

import android.app.Application;

/**
 * Created by dqq on 2019/4/11.
 */
public class TestApplication extends Application {

    private static TestApplication gApp;

    @Override
    public void onCreate() {
        super.onCreate();
        gApp = this;
    }

    public static TestApplication getGlobalApplication() {
        return gApp;
    }
}
