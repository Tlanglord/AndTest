package com.at.test.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.at.test.R;

/**
 * Created by dqq on 2018/11/20.
 */
public class NdkActivity extends Activity {


    static {
        System.loadLibrary("native-lib");
    }

    private TextView tvDownCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
        tvDownCounter = findViewById(R.id.test_ndk_down_counter);
        startDownCountTimer(new OnTimerListener() {
            @Override
            public void start() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDownCounter.setText("倒计时开始");
                    }
                });
                Log.d("JNILOG", "倒计时开始");
            }

            @Override
            public void end() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDownCounter.setText("倒计时结束");
                    }
                });
                Log.d("JNILOG", "倒计时结束");
            }

            @Override
            public void count(final int down) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDownCounter.setText("当前计数: " + down);
                    }
                });
                Log.d("JNILOG", "当前计数: " + down);
            }
        });

        getException(null);
    }

    public interface OnTimerListener {

        void start();

        void end();

        void count(int down);
    }

    public native void startDownCountTimer(OnTimerListener onTimerListener);

    public native void getException(String str);
}
