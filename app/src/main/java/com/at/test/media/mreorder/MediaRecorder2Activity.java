package com.at.test.media.mreorder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于camera2、MeadiaRecorder的视频录制
 */
public class MediaRecorder2Activity extends Activity {

    private CameraDevice cameraDevice;
    private TextureView textureView;
    private CameraCaptureSession cameraCaptureSession;
    private SurfaceView surfaceView;
    private boolean isSurfaceView;
    private ImageReader imageReader;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecorder2);

//        startTextureView();
        startSurfaceView();

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

    }

    private void initImageReader() {
        if (Build.VERSION.SDK_INT > 18) {
            imageReader = ImageReader.newInstance(1, 1, ImageFormat.JPEG, 1);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = imageReader.acquireLatestImage();
                    image.getPlanes();
                }
            }, new Handler());
        }
    }

    private void startTextureView() {
        textureView = findViewById(R.id.test_mrecorder2_tv);

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                start();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    private void startSurfaceView() {
        isSurfaceView = true;
        surfaceView = findViewById(R.id.test_mrecorder2_sv);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void start() {
        if (Build.VERSION.SDK_INT > 22 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.reset();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            mediaRecorder.setOutputFile(PathUtil.getMediaRecorderSavePath());

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] cameraIdList = cameraManager.getCameraIdList();
                cameraManager.openCamera(cameraIdList[0], new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        cameraDevice = camera;
                        try {
                            final CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                            List<Surface> surfaceList = new ArrayList<>();
                            Surface preview;
                            if (isSurfaceView) {
                                preview = surfaceView.getHolder().getSurface();
                            } else {
                                SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                                surfaceTexture.setDefaultBufferSize(textureView.getWidth(), textureView.getHeight());
                                preview = new Surface(surfaceTexture);
                            }

                            surfaceList.add(preview);
                            builder.addTarget(preview);

                            Surface recorder = mediaRecorder.getSurface();
                            surfaceList.add(recorder);
                            builder.addTarget(recorder);


                            cameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(@NonNull CameraCaptureSession session) {
                                    cameraCaptureSession = session;
                                    try {
                                        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                                        cameraCaptureSession.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                            @Override
                                            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                                super.onCaptureStarted(session, request, timestamp, frameNumber);
                                            }

                                            @Override
                                            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                                                super.onCaptureProgressed(session, request, partialResult);
                                            }
                                        }, new Handler());
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                    MediaRecorder2Activity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mediaRecorder.start();
                                        }
                                    });
                                }

                                @Override
                                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                                }
                            }, new Handler());

                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {

                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {

                    }
                }, new Handler());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
