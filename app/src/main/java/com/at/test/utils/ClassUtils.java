package com.at.test.utils;


import com.at.test.activity.CoordinatorLayoutActivity;
import com.at.test.activity.ObjectAnimationActivity;
import com.at.test.activity.Process2Activity;
import com.at.test.activity.ScrollActivity;
import com.at.test.activity.rcy.DouYinListActivity;
import com.at.test.aidl.book.BookActivity;
import com.at.test.app.keyguard.KeyguardActivity;
import com.at.test.app.usage.UsagesActivity;
import com.at.test.app.wallpaper.WallpaperActivity;
import com.at.test.graphics.sample.SampleViewActivity;
import com.at.test.hw.finger.FingerprintActivity;
import com.at.test.media.audio.AudioActivity;
import com.at.test.media.codec.MediaCodecActivity;
import com.at.test.media.extractor.MediaExtractorActivity;
import com.at.test.media.mreorder.MediaRecorder2Activity;
import com.at.test.media.mreorder.MediaRecorder3Activity;
import com.at.test.media.mreorder.MediaRecorderActivity;
import com.at.test.media.mreorder.net.MediaRecorderSocketActivity;
import com.at.test.media.play.VideoViewActivity;
import com.at.test.media.play.ff.FFmpegActivity;
import com.at.test.media.sync.MediaSyncActivity;
import com.at.test.ndk.NdkActivity;
import com.at.test.net.socket.local.LocalSocketActivity;
import com.at.test.net.socket.web.WebSocketActivity;
import com.at.test.opengl.GLActivity;
import com.at.test.opengl.GLJavaActivity;
import com.at.test.opensl.OpenSLRecorderActivity;
import com.at.test.transition.TransitionActivity;

public enum ClassUtils {

    ObjectActivity(ObjectAnimationActivity.class),
    GLActivity(GLActivity.class),
    NdkActivity(NdkActivity.class),
    AudioActivity(AudioActivity.class),
    MediaRecorderActivity(MediaRecorderActivity.class),
    MediaRecorder2Activity(MediaRecorder2Activity.class),
    MediaRecorder3Activity(MediaRecorder3Activity.class),
    FingerprintActivity(FingerprintActivity.class),
    KeyguardActivity(KeyguardActivity.class),
    UsagesActivity(UsagesActivity.class),
    WallpaperActivity(WallpaperActivity.class),
    SampleViewActivity(SampleViewActivity.class),
    TransitionActivity(TransitionActivity.class),
    BookActivity(BookActivity.class),
    GLJavaActivity(GLJavaActivity.class),
    OpenSLRecorderActivity(OpenSLRecorderActivity.class),
    MediaExtractorActivity(MediaExtractorActivity.class),
    MediaCodecActivity(MediaCodecActivity.class),
    MediaSyncActivity(MediaSyncActivity.class),
    LocalSocketActivity(LocalSocketActivity.class),
    WebSocketActivity(WebSocketActivity.class),
    MediaRecorderSocketActivity(MediaRecorderSocketActivity.class),
    FFmpegActivity(FFmpegActivity.class),
    ScrollActivity(ScrollActivity.class),
    VideoViewActivity(VideoViewActivity.class),
    CoordinatorLayoutActivity(CoordinatorLayoutActivity.class),
    Process2Activity(Process2Activity.class),
    DouYinListActivity(DouYinListActivity.class);


//	//	ScreenSlideActivity(ScreenSlideActivity.class),
//	FlowActivity(FlowActivity.class),
//	Flow1Activity(Flow1Activity.class),
//	FadeActivity(FadeActivity.class),
//	TranslationYActivity(TranslationYActivity.class),
//	TestViewActivity(TestViewActivity.class),
//	PageFlingActivity(PageFlingActivity.class),
//	FrescoActivity(FrescoActivity.class),
//	SuperHorizontalActivity(SuperHorizontalActivity.class),
//	RxActivity(RxActivity.class),
//	MultipleItemsListActivity(MultipleItemsListActivity.class),
//	CustomActivity(CustomActivity.class);

    private Class<?> mc;

    ClassUtils(Class<?> c) {
        mc = c;
    }

    public Class<?> getMC() {
        return mc;
    }
}
