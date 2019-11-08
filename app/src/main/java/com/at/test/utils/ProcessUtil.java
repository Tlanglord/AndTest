package com.at.test.utils;

/**
 * Created by dqq on 2019/4/22.
 */
public class ProcessUtil {

    public static String getProcessIdStr() {
        return String.valueOf(android.os.Process.myPid());
    }

    public static int getProcessId() {
        return android.os.Process.myPid();
    }
}
