package com.at.test.media.mreorder.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class ReadThread extends Thread {

    private static final String TAG = Constants.TAG;

    InputStream is;

    boolean loop = true;

    public ReadThread(InputStream is, String name) {
        super(name);
        this.is = is;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void run() {
        super.run();
        if (is == null) {
            Log.v(TAG, getName() + " , is == null");
            return;
        }
        byte bytes[] = new byte[1024];
        int size = 0;
        while (loop) {
            try {
                size = is.read(bytes);
                Log.v(TAG, getName() + ", size : " + size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
