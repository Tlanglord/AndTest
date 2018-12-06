package com.at.test.media.audio;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.at.test.R;
import com.at.test.graphics.sample.TextView;
import com.at.test.media.utils.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioActivity extends Activity {

    private static final String TAG = "AudioActivity";

    private TextView tvRecorder;
    private TextView tvPlay;

    private AudioRecord mAudioRecord;
    private RecordOutputThread recordOutputThread;
    private AudioTrack audioTrack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        tvRecorder = findViewById(R.id.test_audio_start_recording);
        tvPlay = findViewById(R.id.test_audio_play_recording);
        tvRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    stopRecording();
                } else {
                    startRecording();
                }
                v.setSelected(!v.isSelected());
            }
        });

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioTrack == null) {
                    playRecorder();
                }
            }
        });
    }

    private void playRecorder() {
        audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT),
                AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    private void startRecording() {
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT));

        String path = PathUtil.getAudioPcmRecorderSavePath();
        Log.v(TAG, path);
        recordOutputThread = new RecordOutputThread(path, mAudioRecord);
        recordOutputThread.start();
        mAudioRecord.startRecording();
    }

    private void stopRecording() {
        if (mAudioRecord != null) {
            mAudioRecord.stop();
        }
        if (recordOutputThread != null && recordOutputThread.isAlive()) {
            recordOutputThread.startOutput = false;
            recordOutputThread.interrupt();
        }
    }

    public static class RecordOutputThread extends Thread {

        private String output;
        private boolean startOutput = true;
        private AudioRecord audioRecord;

        public RecordOutputThread(String output, AudioRecord audioRecord) {
            this.output = output;
            this.audioRecord = audioRecord;
        }

        @Override
        public void run() {
            byte[] bytes = new byte[1024];
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(output);
//                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                while (startOutput) {
                    int size = audioRecord.read(bytes, 0, 1024);
                    if (size >= 0) {
                        fileOutputStream.write(bytes);
                    }
                    Log.v(TAG, size + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class PlayRecordThread extends Thread {
        private String inputPath;
        private AudioTrack audioTrack;

        public PlayRecordThread(String inputPath, AudioTrack audioTrack) {
            this.inputPath = inputPath;
            this.audioTrack = audioTrack;
        }

        @Override
        public void run() {
            super.run();
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(inputPath);
                byte[] bytes = new byte[1024];
                while (true) {
                    int size = fileInputStream.read(bytes);
                    if (size > 0) {
                        audioTrack.write(bytes, 0, size);
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
