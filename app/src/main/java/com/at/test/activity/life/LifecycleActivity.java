package com.at.test.activity.life;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dqq on 2019/4/9.
 */
public class LifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new Life() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onResume() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    public interface Life extends LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void onCreate();

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onResume();

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void onPause();

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy();
    }
}
