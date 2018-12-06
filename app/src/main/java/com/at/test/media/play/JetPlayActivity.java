package com.at.test.media.play;

import android.app.Activity;
import android.media.JetPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class JetPlayActivity extends Activity {

    private JetPlayer jetPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = "";
        jetPlayer = JetPlayer.getJetPlayer();
        jetPlayer.loadJetFile(path);
        jetPlayer.play();
        jetPlayer.setEventListener(new JetPlayer.OnJetEventListener() {
            @Override
            public void onJetEvent(JetPlayer player, short segment, byte track, byte channel, byte controller, byte value) {

            }

            @Override
            public void onJetUserIdUpdate(JetPlayer player, int userId, int repeatCount) {

            }

            @Override
            public void onJetNumQueuedSegmentUpdate(JetPlayer player, int nbSegments) {

            }

            @Override
            public void onJetPauseUpdate(JetPlayer player, int paused) {

            }
        });
    }
}
