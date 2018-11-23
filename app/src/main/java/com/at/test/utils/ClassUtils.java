package com.at.test.utils;


import com.at.test.activity.ObjectAnimationActivity;
import com.at.test.ndk.NdkActivity;
import com.at.test.opengl.GLActivity;

public enum ClassUtils {

    ObjectActivity(ObjectAnimationActivity.class),
    GLActivity(GLActivity.class),
    NdkActivity(NdkActivity.class);

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
