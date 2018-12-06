package com.at.test.media.play;

import android.app.Activity;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @Author : qiang
 * @Date : 2018/11/28
 **/
public class AsyncPlayerActivity extends Activity {

    private AsyncPlayer asyncPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncPlayer = new AsyncPlayer(null);
//        asyncPlayer.play(, , , );
    }
}
