package com.at.test.media.utils;

import android.os.Environment;

import java.io.File;

public class PathUtil {

    public static String getAudioPcmRecorderSaveDir() {
        String recorderDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "audio" + File.separator + "record" + File.separator;
        File file = new File(recorderDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

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


    /**
     * mp4 样本
     *
     * @return
     */
    public static String getVideoExtractorMp4() {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator + "record" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "10089.mp4";
    }

    /**
     * 抽取出来的mp4
     *
     * @param name
     * @return
     */
    public static String getVideoExtractorMp4SavePath(String name) {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator + "extractor" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "raw_" + name + "_" + System.currentTimeMillis() + ".mp4";
    }

    /**
     * 抽取出来的aac
     *
     * @param name
     * @return
     */
    public static String getVideoExtractorAACSavePath(String name) {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator + "extractor" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "raw_" + name + "_" + System.currentTimeMillis() + ".aac";
    }

    /**
     * 合成
     *
     * @param name
     * @return
     */
    public static String getVideoMuxerSavePath(String name) {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator + "muxer" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "mux_" + name + "_" + System.currentTimeMillis() + ".mp4";
    }

    /**
     * pcm source
     *
     * @return
     */
    public static String getCodecPcmSourcePath() {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "audio" + File.separator
                + "codec" + File.separator + "pcm" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "10086.pcm";
    }

    /**
     * pcm encode aac save path
     *
     * @param name
     * @return
     */
    public static String getCodecPcm2AacSavePath(String name) {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "audio" + File.separator
                + "codec" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "codec_" + name + "_" + System.currentTimeMillis() + ".aac";
    }

    public static String getSyncVideoMp4Path() {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator
                + "sync" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "sync_10086.mp4";
    }

    public static String getSyncAudioAACPath() {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AndroidTest" + File.separator + "media" + File.separator
                + "sync" + File.separator;
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return saveDir + "sync_10089.aac";
    }
}
