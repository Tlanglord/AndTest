package com.at.test.activity.rcy.proxy;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dqq on 2019/11/7.
 */
public class ProxyHttp {

    private static final String TAG = "ProxyHttp";


    public class ProxyHttpThread extends Thread {

        @Override
        public void run() {
            super.run();
            start();
        }

        public void start() {
            ServerSocket serverSocket = null;
            byte[] readBuffer = new byte[1024];
            try {
                serverSocket = new ServerSocket(8080);
                while (loop) {
                    Socket proxyClient = serverSocket.accept();
                    BufferedInputStream bufferedInputStream = (BufferedInputStream) proxyClient.getInputStream();
                    String str = "";
                    while (bufferedInputStream.read(readBuffer) != -1) {
                        str += new String(readBuffer);
                        Log.v(TAG, str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void startProxy() {
        ProxyHttpThread proxyHttpThread = new ProxyHttpThread();
        proxyHttpThread.start();
    }

    private boolean loop;

    public void stopLoop() {
        loop = false;
    }


}
