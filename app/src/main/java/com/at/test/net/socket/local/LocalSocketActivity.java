package com.at.test.net.socket.local;

import android.app.Activity;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.at.test.R;
import com.at.test.net.socket.util.LocalSocketUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class LocalSocketActivity extends Activity {

    private static final String TAG = "LocalSocketActivity";
    private LServer lServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_socket);
        lServer = new LServer();
        lServer.start();
        findViewById(R.id.test_local_socket_gen_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new LClient().start();
            }
        });
    }

    class LServer extends Thread {
        LocalServerSocket localServerSocket = null;
        volatile boolean loop;

        public LServer() {
            loop = true;
        }

        public void close() {
            loop = false;
            if (localServerSocket != null) {
                try {
                    localServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                localServerSocket = new LocalServerSocket(LocalSocketUtil.SOCKET_DOMAIN);
                Log.v(TAG, "new LocalServerSocket");


                while (loop) {
//                    Log.v(TAG, "LocalServerSocket loop");
                    LocalSocket localSocket = localServerSocket.accept();
                    localSocket.close();
                }
                Log.v(TAG, "LocalServerSocket end loop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void startServerReadThread(final LocalSocket localSocket) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        if (localSocket != null) {
                            Log.v(TAG, "LocalServerSocket client accept success");
                            LocalSocketAddress localSocketAddress = localSocket.getLocalSocketAddress();
                            InputStream is = localSocket.getInputStream();
                            OutputStream os = localSocket.getOutputStream();
                            Log.v(TAG, "ready recv data");
                            byte bytes[] = new byte[1024];
                            int size = 0;
                            while ((size = is.read(bytes)) > 0) {
                                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                                byteBuffer.put(bytes);
                                byteBuffer.flip();
//                                        byteBuffer.limit(size);
                                byte bs[] = new byte[size];
                                byteBuffer.get(bs);
                                String s = new String(bs);
                                Log.v(TAG, "from client :  data : " + s);
                            }
//                                    Log.v(TAG, "no data");
//                                    String str = new String("echo : " + System.currentTimeMillis());
//                                    os.write(str.getBytes());
//                                    os.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    class LClient extends Thread {

        boolean loop = true;

        @Override
        public void run() {
            super.run();
            try {
                LocalSocket localSocket = new LocalSocket();
                localSocket.getFileDescriptor();
                LocalSocketAddress localSocketAddress = new LocalSocketAddress(LocalSocketUtil.SOCKET_DOMAIN);
                localSocket.connect(localSocketAddress);
                localSocket.setReceiveBufferSize(500000);
                localSocket.setSendBufferSize(500000);
                Log.v(TAG, "connect status : " + localSocket.isConnected());
                Log.v(TAG, "client connect");
//                while (loop) {
                Log.v(TAG, "client connect success");
                InputStream is = localSocket.getInputStream();
                OutputStream os = localSocket.getOutputStream();
                String str = new String("hi ----------");
                os.write(str.getBytes());
                os.flush();
                localSocket.close();
//                    byte bytes[] = new byte[1024];
//                    is.read(bytes);
//                    String stros = new String(bytes);
//                    Log.v(TAG, "from server, data : " + stros);
//                    os.close();
//                    is.close();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lServer != null) {
            Log.v(TAG, "lServer close");
            lServer.close();
        }
    }
}
