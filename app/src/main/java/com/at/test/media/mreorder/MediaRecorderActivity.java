package com.at.test.media.mreorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;

import java.io.IOException;

/**
 * 使用CAMERA 类型录制
 */
public class MediaRecorderActivity extends Activity {

    private SurfaceView surfaceView;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecorder);
        String PATH_NAME = PathUtil.getMediaRecorderSavePath();
//        Camera camera = Camera.open();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
//        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setVideoFrameRate(4);
        mediaRecorder.setVideoEncodingBitRate(6000000);
        mediaRecorder.setOutputFile(PATH_NAME);
        surfaceView = findViewById(R.id.test_mrecorder_sv);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mediaRecorder.setPreviewDisplay(holder.getSurface());
                    mediaRecorder.prepare();
                    mediaRecorder.start();// Recording is now started
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


//        mediaRecorder.stop();
//        mediaRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
//        mediaRecorder.release(); //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
