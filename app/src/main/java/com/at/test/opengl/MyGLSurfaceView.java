package com.at.test.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.at.test.opengl.utils.GLJniUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by dqq on 2018/11/20.
 * ANativeWindow和eglCreateWindowSurface
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private static final int GL_ES_VERSION = 2;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRender();
    }


    private void initRender() {
        setEGLContextClientVersion(GL_ES_VERSION);
        setRenderer(new MyRender());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public class MyRender implements Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLJniUtil.initGL();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLJniUtil.render();
        }
    }

}
