package com.at.test.opengl.utils;

/**
 * Created by dqq on 2018/11/20.
 */
public class GLJniUtil {

    static {
        System.loadLibrary("native-lib");
    }

    public static native void render();

    public static native void initGL();

    public static native String getAuthor();
}
