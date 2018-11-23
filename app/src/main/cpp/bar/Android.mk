LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := bar-jni
LOCAL_SRC_FILES := bar.cpp
include $(BUILD_SHARED_LIBRARY)
