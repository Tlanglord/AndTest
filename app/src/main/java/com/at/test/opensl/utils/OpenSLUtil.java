package com.at.test.opensl.utils;

public class OpenSLUtil {

    static {
        System.loadLibrary("native-lib");
    }

    public native void play(String url);

    public native void pause();

    public native void stop();

    public native void resume();

    public native void reset();

    public native void release();

}
