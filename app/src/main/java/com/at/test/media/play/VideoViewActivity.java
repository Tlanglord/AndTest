package com.at.test.media.play;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.VideoView;

import com.at.test.R;

public class VideoViewActivity extends Activity {


    private static final String TAG = "VideoViewActivity";
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        String videoPath = "https://replayqn.wangxiao.eaydu.com/ll/2480/89d9ca4f11a1060608d92b79189c054f.flv.mp4";
        videoView = findViewById(R.id.test_videoview_vv);
        videoView.setVideoPath(videoPath);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.v(TAG, "onPrepared");
                videoView.start();
            }
        });
//        videoView.setMediaController(new MediaController());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.resume();
        }
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        videoView.pause();
    }
}
