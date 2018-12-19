package com.at.test.media.mreorder.net;

import android.app.Activity;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用CAMERA 类型录制
 * camera2
 * ImageReader.OnImageAvailableListener
 * socket send
 * socket server recv
 * surface
 */
public class MediaRecorderSocketActivity extends Activity {

    private static final String TAG = "MediaRecorderSocketActi";

    final static int start_recorder = 1;
    final static int init_server_success = 2;

    private SurfaceView surfaceView;
    private MediaRecorder mediaRecorder;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleMessageImpl(msg);
        }
    };
    private ReadThread readThread;
    private FileObserver fileObserver;
    private LocalServerSocket localServerSocket;

    private void handleMessageImpl(Message msg) {
        switch (msg.what) {
            case start_recorder:
                FileDescriptor fd = (FileDescriptor) msg.obj;
                startMediaRecorder(fd);
                break;
            case init_server_success:
                initClient();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecorder);
        surfaceView = findViewById(R.id.test_mrecorder_sv);
        initServer();
//        startMediaRecorder(null);
    }

    private void initServer() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    localServerSocket = new LocalServerSocket(Constants.SOCKET);
                    handler.obtainMessage(init_server_success).sendToTarget();
                    Log.v(TAG, "sent server init success");
                    while (loopServer) {
                        Log.v(TAG, "server loop");
                        LocalSocket localSocket = localServerSocket.accept();
                        InputStream is = localSocket.getInputStream();
                        readThread = new ReadThread(is, "server input read");
                        readThread.start();
                    }
                    if (localServerSocket != null) {
                        localServerSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    boolean loopClient = true;
    boolean loopServer = true;

    private void initClient() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    LocalSocket localSocket = new LocalSocket();
                    LocalSocketAddress localSocketAddress = new LocalSocketAddress(Constants.SOCKET);
                    localSocket.connect(localSocketAddress);
                    localSocket.setReceiveBufferSize(500000);
                    localSocket.setSendBufferSize(500000);
                    Log.v(TAG, "client connect server");
                    if (localSocket.isConnected()) {
                        Log.v(TAG, "connect server success");
                        handler.obtainMessage(start_recorder, localSocket.getFileDescriptor()).sendToTarget();
//                        byte bytes[] = new byte[1024];
//                        InputStream is = localSocket.getInputStream();
//                        OutputStream os = localSocket.getOutputStream();

                    }
                    if (localSocket != null) {
                        Log.v(TAG, "local socket client close");
                        localSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void startMediaRecorder(final FileDescriptor fd) {
        Log.v(TAG, "startMediaRecorder");

        if (fd == null) {
            Log.v(TAG, "fd == null");
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(90000);
        mediaRecorder.setVideoFrameRate(4);
        mediaRecorder.setVideoEncodingBitRate(6000000);

        try {
            String path = PathUtil.getMediaRecorderSavePath();
            mediaRecorder.setOutputFile(path);
            observer(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "surface view created : " + surfaceView.getHolder().isCreating());
        Log.v(TAG, "surface view created : " + surfaceView.getHolder().getSurface() != null ? "true" : "false");
        Surface surface = surfaceView.getHolder().getSurface();
        if (surfaceView.getHolder().getSurface() != null) {
            startRecorder(surface);
        }
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "surfaceCreated");
                Surface surface = holder.getSurface();
                startRecorder(surface);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v(TAG, "surfaceDestroyed");
            }
        });
    }

    private void startRecorder(Surface surface) {
        try {
            mediaRecorder.setPreviewDisplay(surface);
            mediaRecorder.prepare();
            mediaRecorder.start();// Recording is now started
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            loopClient = false;
            loopServer = false;

            if (readThread != null) {
                readThread.setLoop(false);
            }

            if (fileObserver != null) {
                fileObserver.stopWatching();
                fileObserver = null;
            }

            if (localServerSocket != null) {
                Log.v(TAG, "localServerSocket close");
                localServerSocket.close();
                localServerSocket = null;
            }

            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void observer(String path) {
        fileObserver = new FileObserver(path) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                Log.v(TAG, "path : " + path + " , " + event);
            }
        };
        fileObserver.startWatching();
    }
}
