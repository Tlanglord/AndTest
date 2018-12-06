package com.at.test.media.play;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.at.test.R;

import java.io.IOException;

public class MediaPlayerActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        startPlayerVideo();

    }

    private void startPlayerAudio() {
        String audioPath = "222.mp3";
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
//        mediaPlayer.setDisplay(holder);
        //时长
//        mediaPlayer.getDuration();
    }

    private void startPlayerVideo() {
        surfaceView = findViewById(R.id.test_media_player_sv);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                String path = "";
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setDisplay(holder);
                    mediaPlayer.prepareAsync();
                    //时长
                    mediaPlayer.getDuration();
//            mediaPlayer.setDisplay();
//            mediaPlayer.setAudioAttributes();
//            mediaPlayer.setLooping();
//            mediaPlayer.setSurface();
//            mediaPlayer.setDisplay();
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

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
