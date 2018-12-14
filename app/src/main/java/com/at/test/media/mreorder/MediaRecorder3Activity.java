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
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;

import com.at.test.R;
import com.at.test.media.mreorder.utils.GenTextureSurface;
import com.at.test.media.utils.PathUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

/**
 * 基于camera2、MediaRecorder的视频录制
 */
public class MediaRecorder3Activity extends Activity {

    private static final String TAG = "MediaRecorder3Activity";

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private GLSurfaceView glSurfaceView;
    private ImageReader imageReader;
    private MediaRecorder mediaRecorder;
    private SurfaceTexture surfaceTexture;
    private int textureId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecorder3);
        startGLSurfaceView();
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

    private void startGLSurfaceView() {
        glSurfaceView = findViewById(R.id.test_mrecorder3_gl_sv);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new MrRender());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//        glSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });
    }


    private Handler handler = new Handler();

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
                            Surface preview = glSurfaceView.getHolder().getSurface();

                            textureId = GenTextureSurface.createOESTextureObject();
                            surfaceTexture = new SurfaceTexture(textureId);
                            surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                                @Override
                                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                                    Log.v(TAG, "onFrameAvailable");
                                    glSurfaceView.requestRender();
                                }
                            });
                            Surface handleSurface = new Surface(surfaceTexture);
                            surfaceList.add(handleSurface);
                            builder.addTarget(handleSurface);

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
                                    MediaRecorder3Activity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mediaRecorder.start();
                                        }
                                    });
                                }

                                @Override
                                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                                }
                            }, handler);

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
                }, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void captureImager() {
        if (Build.VERSION.SDK_INT > 18) {
            ImageReader imageReader = ImageReader.newInstance(1080, 10920, ImageFormat.JPEG, 1024);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    Image.Plane planes[] = image.getPlanes();
                    ByteBuffer byteBuffer = planes[0].getBuffer();
                    byte bytes[] = new byte[byteBuffer.limit()];

                }
            }, null);
            Surface image = imageReader.getSurface();
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

    private static final String VERTEX_SHADER = "" +
            //顶点坐标
            "attribute vec4 aPosition;\n" +
            //纹理矩阵
            "uniform mat4 uTextureMatrix;\n" +
            //自己定义的纹理坐标
            "attribute vec4 aTextureCoordinate;\n" +
            //传给片段着色器的纹理坐标
            "varying vec2 vTextureCoord;\n" +
            "void main()\n" +
            "{\n" +
            //根据自己定义的纹理坐标和纹理矩阵求取传给片段着色器的纹理坐标
            "  vTextureCoord = (uTextureMatrix * aTextureCoordinate).xy;\n" +
            "  gl_Position = aPosition;\n" +
            "}\n";

    private static final String FRAGMENT_SHADER = "" +
            //使用外部纹理必须支持此扩展
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            //外部纹理采样器
            "uniform samplerExternalOES uTextureSampler;\n" +
            "varying vec2 vTextureCoord;\n" +
            "void main() \n" +
            "{\n" +
            //获取此纹理（预览图像）对应坐标的颜色值
            "  vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);\n" +
            //求此颜色的灰度值
            "  float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);\n" +
            //将此灰度值作为输出颜色的RGB值，这样就会变成黑白滤镜
            "  gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);\n" +
            "}\n";

    //每行前两个值为顶点坐标，后两个为纹理坐标
    private static final float[] vertexData = {
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
    };

    public FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }


    //加载着色器，GL_VERTEX_SHADER代表生成顶点着色器，GL_FRAGMENT_SHADER代表生成片段着色器
    public int loadShader(int type, String shaderSource) {
        //创建Shader
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Create Shader Failed!" + GLES20.glGetError());
        }
        //加载Shader代码
        GLES20.glShaderSource(shader, shaderSource);
        //编译Shader
        GLES20.glCompileShader(shader);
        return shader;
    }

    //将两个Shader链接至program中
    public int linkProgram(int verShader, int fragShader) {
        //创建program
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Create Program Failed!" + GLES20.glGetError());
        }
        //附着顶点和片段着色器
        GLES20.glAttachShader(program, verShader);
        GLES20.glAttachShader(program, fragShader);
        //链接program
        GLES20.glLinkProgram(program);
        //告诉OpenGL ES使用此program
        GLES20.glUseProgram(program);
        return program;
    }

    private float transformMatrix[] = new float[16];

    public class MrRender implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            Log.v(TAG, "onSurfaceCreated");
            start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            Log.v(TAG, "onDrawFrame");
            if (surfaceTexture != null) {
                //更新纹理图像
                surfaceTexture.updateTexImage();
                //获取外部纹理的矩阵，用来确定纹理的采样位置，没有此矩阵可能导致图像翻转等问题
                surfaceTexture.getTransformMatrix(transformMatrix);

                GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

                int vertexShader = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER);
                int fragmentShader = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
                int mShaderProgram = linkProgram(vertexShader, fragmentShader);

                //获取Shader中定义的变量在program中的位置
                int aPositionLocation = GLES20.glGetAttribLocation(mShaderProgram, "aPosition");
                int aTextureCoordLocation = GLES20.glGetAttribLocation(mShaderProgram, "aTextureCoordinate");
                int uTextureMatrixLocation = GLES20.glGetUniformLocation(mShaderProgram, "uTextureMatrix");
                int uTextureSamplerLocation = GLES20.glGetUniformLocation(mShaderProgram, "uTextureSampler");

                //激活纹理单元0
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                //绑定外部纹理到纹理单元0
                GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
                //将此纹理单元床位片段着色器的uTextureSampler外部纹理采样器
                GLES20.glUniform1i(uTextureSamplerLocation, 0);

                //将纹理矩阵传给片段着色器
                GLES20.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0);

                FloatBuffer floatBuffer = createBuffer(vertexData);

                //将顶点和纹理坐标传给顶点着色器
                if (floatBuffer != null) {
                    //顶点坐标从位置0开始读取
                    floatBuffer.position(0);
                    //使能顶点属性
                    GLES20.glEnableVertexAttribArray(aPositionLocation);
                    //顶点坐标每次读取两个顶点值，之后间隔16（每行4个值 * 4个字节）的字节继续读取两个顶点值
                    GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 16, floatBuffer);

                    //纹理坐标从位置2开始读取
                    floatBuffer.position(2);
                    GLES20.glEnableVertexAttribArray(aTextureCoordLocation);
                    //纹理坐标每次读取两个顶点值，之后间隔16（每行4个值 * 4个字节）的字节继续读取两个顶点值
                    GLES20.glVertexAttribPointer(aTextureCoordLocation, 2, GLES20.GL_FLOAT, false, 16, floatBuffer);
                }

                //绘制两个三角形（6个顶点）
                GLES20.glDrawArrays(GL_TRIANGLES, 0, 6);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }
}
