package com.at.test.opengl;

import android.app.Activity;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.at.test.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class GLJavaActivity extends Activity {

    private SurfaceView surfaceView;
    private int global_gl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_java);
        surfaceView = findViewById(R.id.test_gl_java_sv);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // 1. init
                EGL10 egl10 = (EGL10) EGLContext.getEGL();
                EGLDisplay display = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                egl10.eglInitialize(display, null);

                int[] attribList = {
                        EGL10.EGL_RED_SIZE, 8,
                        EGL10.EGL_GREEN_SIZE, 8,
                        EGL10.EGL_BLUE_SIZE, 8,
                        EGL10.EGL_ALPHA_SIZE, 8,
                        EGL10.EGL_RENDERABLE_TYPE,
                        EGL14.EGL_OPENGL_ES2_BIT,
                        EGL10.EGL_NONE, 0,      // placeholder for recordable [@-3]
                        EGL10.EGL_NONE
                };

                //2 chooseConfig
                EGLConfig[] configs = new EGLConfig[1];
                int[] numConfigs = new int[1];
                egl10.eglChooseConfig(display, attribList, configs, configs.length, numConfigs);
                EGLConfig config = configs[0];

                //4. 创建Surface
                EGLSurface eglSurface = egl10.eglCreateWindowSurface(display, config, holder.getSurface(), new int[]{EGL14.EGL_NONE});
                //5. 创建Context
                EGLContext context = egl10.eglCreateContext(
                        display,
                        config,
                        EGL10.EGL_NO_CONTEXT,
                        new int[]{EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE}
                );
                egl10.eglMakeCurrent(display, eglSurface, eglSurface, context);

                String shader_vertex = "uniform mediump mat4 MODELVIEWPROJECTIONMATRIX;\n" +
                        "attribute vec4 POSITION;\n" +
                        "void main(){\n" +
                        "  gl_Position = POSITION;\n" +
                        "}";
                String shader_fragment = "precision mediump float;\n" +
                        "void main(){\n" +
                        "   gl_FragColor = vec4(0,0,1,1);\n" +
                        "}";

                global_gl = GLES20.glCreateProgram();
                GLES20.glClearColor(1f, 1f, 1f, 1f);
                int v = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
                GLES20.glShaderSource(v, shader_vertex);

                int f = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
                GLES20.glShaderSource(f, shader_fragment);

                GLES20.glCompileShader(v);
                GLES20.glCompileShader(f);

                GLES20.glAttachShader(global_gl, v);
                GLES20.glAttachShader(global_gl, f);

                GLES20.glLinkProgram(global_gl);

                drawTriangles();

                //6. 指定当前的环境为绘制环境
                egl10.eglSwapBuffers(display, eglSurface);
                egl10.eglDestroySurface(display, eglSurface);
                egl10.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                        EGL10.EGL_NO_CONTEXT);
                egl10.eglDestroyContext(display, context);
                egl10.eglTerminate(display);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void drawTriangles() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        float vertexs[] = {
                0.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
        };

        GLES20.glUseProgram(global_gl);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuffer.asFloatBuffer();
        buffer.put(vertexs);
        buffer.flip();
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, buffer);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
