#include <jni.h>
#include <string>
#include "bar/bar.h"
#include <android/log.h>
#include <GLES2/gl2.h>
#include <thread>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>


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

std::thread *t_thread;

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
    t_thread = NULL;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_ndk_NdkActivity_startDownCountTimer(JNIEnv *env, jobject ji,
                                                     jobject onTimerListener) {

    timerListener = env->NewGlobalRef(onTimerListener);
    t_thread = new std::thread(run);
//    std::thread t_thread(run)
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
//    const char *str = env->GetStringUTFChars(str_, 0);
    std::string _strexp = "1234";
//    try {
//        _strexp.at(5);
//    } catch (std::exception e) {
//        jclass jclz = env->FindClass("java/lang/NullPointerException");
//        env->ThrowNew(jclz, "_strexp is null");
//        LOGE("_strexp is null");
//        return;
//    }
//    LOGE("_strexp is null after");


    jclass jlz = env->FindClass("com/at/test/ndk/NdkActivity");
    jmethodID jlzmid = env->GetStaticMethodID(jlz, "talkException", "()V");
    env->CallStaticVoidMethod(jlz, jlzmid);
//    if (env->ExceptionCheck()) {
//        env->Throw(env->ExceptionOccurred());
    if (env->ExceptionOccurred()) {
//        env->ExceptionDescribe();
//        env->ExceptionClear();

        jclass jclz = env->FindClass("java/lang/NullPointerException");
        env->ThrowNew(jclz, "_strexp is null");
    }
//    }




//    env->Throw();

//    env->ReleaseStringUTFChars(str_, str);
}

//用SLObjectItf声明引擎接口对象
SLObjectItf engineObject = nullptr;
//声明具体的引擎对象实例
SLEngineItf engineEngine = nullptr;

SLOutputMixItf outputMixItf = NULL;
SLObjectItf outputMixObject = NULL;
SLEnvironmentalReverbItf outputMixEnvironmentalReverbItf = NULL;
SLEnvironmentalReverbSettings environmentalReverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

SLPlayItf playPlayer = NULL;
SLPlaybackRateItf playbackRateItf = NULL;
SLObjectItf playObject = NULL;

SLVolumeItf volumeItf = NULL;


/**
 * 创建引擎
 */
void createEngine() {

    slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);//第一步创建引擎
    (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);//实现（Realize）engineObject接口对象
    (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE,
                                  &engineEngine);//通过engineObject的GetInterface方法初始化engineEngine
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_play(JNIEnv *env, jobject instance, jstring url_) {
    const char *url = env->GetStringUTFChars(url_, 0);

    createEngine();

    SLInterfaceID mids[1] = {SL_IID_ENVIRONMENTALREVERB};
    SLboolean mreq[1] = {SL_BOOLEAN_FALSE};

    //第二步，创建混音器
    (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 1, mids, mreq);
    (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,
                                     &outputMixEnvironmentalReverbItf);

    (*outputMixEnvironmentalReverbItf)->SetEnvironmentalReverbProperties(
            outputMixEnvironmentalReverbItf, &environmentalReverbSettings);

    //第三步，设置播放器参数和创建播放器
    SLDataLocator_URI locator_uri = {SL_DATALOCATOR_URI, (SLchar *) url};
    SLDataFormat_MIME format_mime = {SL_DATAFORMAT_MIME, NULL, SL_CONTAINERTYPE_UNSPECIFIED};
    SLDataSource slDataSource = {&locator_uri, &format_mime};

    SLDataLocator_OutputMix locator_outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
    SLDataSink slDataSink = {&locator_outputMix, NULL};

    SLInterfaceID ids[3] = {SL_IID_SEEK, SL_IID_MUTESOLO, SL_IID_VOLUME};
    SLboolean req[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};

    (*engineEngine)->CreateAudioPlayer(engineEngine, &playObject, &slDataSource, &slDataSink, 3,
                                       ids, req);
    (*playObject)->Realize(playObject, SL_BOOLEAN_FALSE);

    (*playObject)->GetInterface(playObject, SL_IID_PLAY, &playPlayer);

    (*playObject)->GetInterface(playObject, SL_IID_VOLUME, &volumeItf);

    (*playPlayer)->SetPlayState(playPlayer, SL_PLAYSTATE_PLAYING);

    env->ReleaseStringUTFChars(url_, url);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_pause(JNIEnv *env, jobject instance) {
    (*playPlayer)->SetPlayState(playPlayer, SL_PLAYSTATE_PAUSED);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_stop(JNIEnv *env, jobject instance) {
    (*playPlayer)->SetPlayState(playPlayer, SL_PLAYSTATE_STOPPED);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_resume(JNIEnv *env, jobject instance) {
    (*playPlayer)->SetPlayState(playPlayer, SL_PLAYSTATE_PLAYING);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_reset(JNIEnv *env, jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_OpenSLUtil_release(JNIEnv *env, jobject instance) {

    // TODO

}

#define NUM_BUFFERS_IN_QUEUE 1
//录音文件
FILE *gFile = NULL;
SLRecordItf recordRecorder;
SLObjectItf recordObject;
SLAndroidSimpleBufferQueueItf recordBufferQueue;

#define BUFFER_SIZE_IN_SAMPLES 8192
#define BUFFER_SIZE_IN_BYTES   (2 * BUFFER_SIZE_IN_SAMPLES)

/* Local storage for Audio data */
int8_t pcmData[NUM_BUFFERS_IN_QUEUE * BUFFER_SIZE_IN_BYTES];

struct RecordDataPCMContext {
    SLPlayItf playItf;
    SLuint32 size;
    SLint8 *pDataBase;    // Base address of local audio data storage
    SLint8 *pData;
};

RecordDataPCMContext recordDataPCMContext;

void recorderQueueCallback(SLAndroidSimpleBufferQueueItf androidSimpleBufferQueueItf,
                           void *pContext) {
    RecordDataPCMContext *pcmContext = (RecordDataPCMContext *) pContext;
    LOGI("recorderQueueCallback");
    fwrite(pcmContext->pData, BUFFER_SIZE_IN_BYTES, 1, gFile);
    pcmContext->pData += BUFFER_SIZE_IN_BYTES;

    if (pcmContext->pData >=
        pcmContext->pDataBase + (NUM_BUFFERS_IN_QUEUE * BUFFER_SIZE_IN_BYTES)) {
        pcmContext->pData = pcmContext->pDataBase;
    }

    (*androidSimpleBufferQueueItf)->Enqueue(androidSimpleBufferQueueItf, pcmContext->pDataBase,
                                            BUFFER_SIZE_IN_BYTES);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_SLRecorder_createEngine(JNIEnv *env, jclass type) {
    createEngine();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_SLRecorder_recording(JNIEnv *env, jclass type, jstring file_) {
    const char *file = env->GetStringUTFChars(file_, 0);
    gFile = fopen(file, "w");

    SLDataLocator_IODevice dataLocator_ioDevice = {SL_DATALOCATOR_IODEVICE, SL_IODEVICE_AUDIOINPUT,
                                                   SL_DEFAULTDEVICEID_AUDIOINPUT, NULL};

    SLDataSource slDataSource = {&dataLocator_ioDevice, NULL};
    SLDataLocator_AndroidSimpleBufferQueue androidSimpleBufferQueue = {
            SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, NUM_BUFFERS_IN_QUEUE
    };

    SLDataFormat_PCM slDataFormat_pcm = {
            SL_DATAFORMAT_PCM,
            2,
            SL_SAMPLINGRATE_44_1,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            16,
            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
            SL_BYTEORDER_LITTLEENDIAN
    };

    SLDataSink slDataSink = {&androidSimpleBufferQueue, &slDataFormat_pcm};
    SLInterfaceID slInterfaceIDs[2] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
                                       SL_IID_ANDROIDCONFIGURATION};
    SLboolean req[2] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};

    /* Create the audio recorder */
    (*engineEngine)->CreateAudioRecorder(engineEngine, &recordObject, &slDataSource, &slDataSink, 2,
                                         slInterfaceIDs, req);

    /* get the android configuration interface*/
    SLAndroidConfigurationItf inputConfig;
    SLresult lresult = (*recordObject)->GetInterface(recordObject, SL_IID_ANDROIDCONFIGURATION,
                                                     &inputConfig);
    if (lresult == SL_RESULT_SUCCESS) {
        SLuint32 presetValue = SL_ANDROID_RECORDING_PRESET_VOICE_RECOGNITION;
        (*inputConfig)->SetConfiguration(inputConfig, SL_ANDROID_KEY_RECORDING_PRESET, &presetValue,
                                         sizeof(SLuint32));
    }

    /* Realize the recorder in synchronous mode. */
    (*recordObject)->Realize(recordObject, SL_BOOLEAN_FALSE);

    /* Get the buffer queue interface which was explicitly requested */
    (*recordObject)->GetInterface(recordObject, SL_IID_RECORD, &recordRecorder);

    /* get the record interface */
    (*recordObject)->GetInterface(recordObject, SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
                                  &recordBufferQueue);

    //Initialize the callback and its context for the recording buffer queue
    recordDataPCMContext.pDataBase = (SLint8 *) &pcmData;
    recordDataPCMContext.pData = recordDataPCMContext.pDataBase;
    recordDataPCMContext.size = sizeof(pcmData);

    (*recordBufferQueue)->RegisterCallback(recordBufferQueue, recorderQueueCallback,
                                           &recordDataPCMContext);

    //Enqueue buffers to map the region of memory allocated to store the recorded data
    for (int i = 0; i < NUM_BUFFERS_IN_QUEUE; i++) {
        LOGD("%d ", i);
        (*recordBufferQueue)->Enqueue(recordBufferQueue, recordDataPCMContext.pData,
                                      BUFFER_SIZE_IN_BYTES);
//        assert(SL_RESULT_SUCCESS == result);
        recordDataPCMContext.pData += BUFFER_SIZE_IN_BYTES;
    }
    recordDataPCMContext.pData = recordDataPCMContext.pDataBase;
    (*recordRecorder)->SetRecordState(recordRecorder, SL_RECORDSTATE_RECORDING);

    env->ReleaseStringUTFChars(file_, file);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_SLRecorder_stop(JNIEnv *env, jclass type) {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_opensl_utils_SLRecorder_shutdown(JNIEnv *env, jclass type) {

    //destroy recorder object , and invlidate all associated interfaces
    if (recordObject) {
        (*recordObject)->Destroy(recordObject);
        recordObject = NULL;
        recordRecorder = NULL;
        recordBufferQueue = NULL;
    }

    // destroy engine object, and invalidate all associated interfaces
    if (engineObject) {
        (*engineObject)->Destroy(engineObject);
        engineObject = NULL;
        engineEngine = NULL;
    }

    fclose(gFile);
}

