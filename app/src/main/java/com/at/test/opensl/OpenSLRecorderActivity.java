package com.at.test.opensl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.at.test.media.utils.PathUtil;
import com.at.test.opensl.utils.SLRecorder;

public class OpenSLRecorderActivity extends Activity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = PathUtil.getAudioPcmRecorderSavePath();
        SLRecorder.createEngine();
        SLRecorder.recording(path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SLRecorder.stop();
        SLRecorder.shutdown();
    }
}
