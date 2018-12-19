//
// Created by qiang on 2018/12/19.
//

#ifndef ANDROIDTEST_PLAYER_H
#define ANDROIDTEST_PLAYER_H

#include <jni.h>


extern "C" {
#include <libavfilter/avfilter.h>
}

class player {

};


extern "C"
JNIEXPORT void JNICALL
Java_com_at_test_media_play_ff_FFmpegActivity_setSurface(JNIEnv *env, jobject instance,
                                                         jobject surface) {
    avfilter_register_all();
}

#endif //ANDROIDTEST_PLAYER_H
