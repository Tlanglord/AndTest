package com.at.test.media.play;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.VideoView;

import com.at.test.R;

public class VideoViewActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        String videoPath = "22.mp4";
        final VideoView videoView = findViewById(R.id.test_videoview_vv);
        videoView.setVideoPath(videoPath);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
//        videoView.setMediaController(new MediaController());
    }
}
