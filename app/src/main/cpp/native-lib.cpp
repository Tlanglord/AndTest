#include <jni.h>
#include <string>
#include "bar/bar.h"
#include <android/log.h>
#include <GLES2/gl2.h>
#include <thread>

#define  LOG    "JNILOG" // 这个是自定义的LOG的TAG
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG,__VA_ARGS__) // 定义LOGD类型
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG,__VA_ARGS__) // 定义LOGI类型
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG,__VA_ARGS__) // 定义LOGF类型


JavaVM *g_jvm = NULL;
JNIEnv *g_env = NULL;
jobject timerListener;

GLuint g_glprogram;

extern "C" JNIEXPORT jstring
JNICALL
Java_com_xes_jnitest_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());

    Bar *bar = new Bar();
    std::string barStr = bar->getBarString();
    LOGD("LOG %s", barStr.c_str());
    return env->NewStringUTF(barStr.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_xes_jnitest_MainActivity_getStringFromNative(JNIEnv *env, jobject instance) {

    std::string returnValue = "Hello from C++";
    return env->NewStringUTF(returnValue.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xes_jnitest_MainActivity_getIntFromNative(JNIEnv *env, jobject instance) {

    // TODO

}


extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opengl_utils_GLJniUtil_render(JNIEnv *env, jclass type) {

    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

    GLfloat vertexs[] = {
            0.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f
    };

    glUseProgram(g_glprogram);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, vertexs);
    glEnableVertexAttribArray(0);
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opengl_utils_GLJniUtil_initGL(JNIEnv *env, jclass type) {

    // TODO
    GLuint glProgram;
    GLuint vertexShader;
    GLuint framgentShader;

    const char *shader_vertex = "uniform mediump mat4 MODELVIEWPROJECTIONMATRIX;\n"
                                "attribute vec4 POSITION;\n"
                                "void main(){\n"
                                "  gl_Position = POSITION;\n"
                                "}";
    const char *shader_fragment = "precision mediump float;\n"
                                  "void main(){\n"
                                  "   gl_FragColor = vec4(0,0,1,1);\n"
                                  "}";

    glProgram = glCreateProgram();

    if (glProgram == 0) {
        LOGE("GLES %s", "init opengl es eroor");
    }

    g_glprogram = glProgram;

    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &shader_vertex, NULL);

    framgentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(framgentShader, 1, &shader_fragment, NULL);

    glCompileShader(vertexShader);
    glCompileShader(framgentShader);

    glAttachShader(glProgram, vertexShader);
    glAttachShader(glProgram, framgentShader);

    glLinkProgram(glProgram);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_at_test_opengl_utils_GLJniUtil_getAuthor(JNIEnv *env, jclass type) {

    std::string returnValue = "Hello from C++";
    return env->NewStringUTF(returnValue.c_str());
}


void run() {

    if ((g_jvm->AttachCurrentThread(&g_env, NULL)) != JNI_OK) {
        LOGD("AttachCurrentThread ! JNI_OK");
    } else {
        LOGD("AttachCurrentThread JNI_OK");
    }

    jclass jclz = g_env->GetObjectClass(timerListener);
    jmethodID jstart_mth_id = g_env->GetMethodID(jclz, "start", "()V");
    jmethodID jend_mth_id = g_env->GetMethodID(jclz, "end", "()V");
    jmethodID jcount_mth_id = g_env->GetMethodID(jclz, "count", "(I)V");
    g_env->CallVoidMethod(timerListener, jstart_mth_id);

    for (int i = 0; i < 10; i++) {
        LOGD("thread %d", i);
        g_env->CallVoidMethod(timerListener, jcount_mth_id, i);
        std::this_thread::sleep_for(std::chrono::seconds(1));
    }
    g_env->CallVoidMethod(timerListener, jend_mth_id);

    g_env->DeleteGlobalRef(timerListener);
    g_env->DeleteLocalRef(jclz);

    g_jvm->DetachCurrentThread();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_ndk_NdkActivity_startDownCountTimer(JNIEnv *env, jobject ji,
                                                     jobject onTimerListener) {

    timerListener = env->NewGlobalRef(onTimerListener);
    std::thread *t_thread = new std::thread(run);
//    std::thread t_thread(run);

}

//当动态库被加载时这个函数被系统调用
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    g_jvm = vm;

    //获取JNI版本
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        LOGE("GetEnv failed!");
        return result;
    }
    g_env = env;

    if (g_env) {
        LOGD("env != null");
    } else {
        LOGD("env == null");
    }

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("JNI_OnUnload");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_ndk_NdkActivity_getException(JNIEnv *env, jobject instance, jstring str_) {
    const char *str = env->GetStringUTFChars(str_, 0);

    env->ReleaseStringUTFChars(str_, str);
}