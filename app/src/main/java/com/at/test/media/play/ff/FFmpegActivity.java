package com.at.test.media.play.ff;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Surface;

public class FFmpegActivity extends Activity {

    static {
//        System.loadLibrary("avcodec");
//        System.loadLibrary("avfilter");
//        System.loadLibrary("avformat");
//        System.loadLibrary("avutil");
//        System.loadLibrary("swresample");
//        System.loadLibrary("swscale");
        System.loadLibrary("ff-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSurface(null);
    }

    public native void setSurface(Surface surface);
}
