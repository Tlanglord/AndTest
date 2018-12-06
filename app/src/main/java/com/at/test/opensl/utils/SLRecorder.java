package com.at.test.opensl.utils;

public class SLRecorder {

    public static native void createEngine();

    public static native void recording(String file);

    public static native void stop();

    public static native void shutdown();
}
