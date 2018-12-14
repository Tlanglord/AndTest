package com.at.test.media.mreorder.net;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

public class WriteThread extends Thread {

    private static final String TAG = Constants.TAG;
    private OutputStream os;
    private byte[] bytes;

    public WriteThread(OutputStream os, String name) {
        super(name);
        this.os = os;
    }

    public void putBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void run() {
        super.run();
        if (os == null) {
            Log.v(TAG, getName() + " , os == null");
            return;
        }
        while (true) {
            try {
                if (bytes == null || bytes.length == 0) {
                    continue;
                }
                os.write(bytes);
                bytes = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
