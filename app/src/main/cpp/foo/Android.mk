LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := foo-jni
LOCAL_SRC_FILES := foo.cpp
include $(BUILD_SHARED_LIBRARY)