package com.at.test.media.utils;

import android.os.Environment;

import java.io.File;

public class PathUtil {

    public static String getMediaRecorderSavePath() {
        String recorderDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator + "record" + File.separator;
        File file = new File(recorderDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String recorderOutputPath = recorderDir + System.currentTimeMillis() + ".mp4";
        return recorderOutputPath;
    }

    public static String getAudioPcmRecorderSavePath() {
        String recorderDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "audio" + File.separator + "record" + File.separator;
        File file = new File(recorderDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String recorderOutputPath = recorderDir + System.currentTimeMillis() + ".pcm";
        return recorderOutputPath;
    }
}
