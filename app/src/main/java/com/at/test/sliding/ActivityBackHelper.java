package com.at.test.sliding;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.at.test.base.BaseActivity;

/**
 * Created by dongqiangqiang on 2017/6/7.
 */

public class ActivityBackHelper implements GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private BaseActivity activity;
    private SlidingFinishLayout layout;

    public void init(final BaseActivity activity) {
        this.activity = activity;
        if (activity.onFlingBack()) {
            detector = new GestureDetector(activity, this);
        }

        if (activity.onSlidingBack()) {
            layout = new SlidingFinishLayout(activity);
            layout.bind();
            layout.setEnableSliding(true);
            layout.setOnSlidingFinishListener(new SlidingFinishLayout.OnSlidingFinishListener() {
                @Override
                public void onSlidingFinish() {
                    if (activity != null && !activity.isFinishing()) {
                        activity.backOfFinish();
                    }
                }
            });
        }
    }

    public void setEnableSliding(boolean open) {
        if (layout != null) {
            layout.setEnableSliding(open);
        }
    }

    public boolean processDispatchEvent(MotionEvent event) {
        if (detector != null) {
            return detector.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getRawX() < 100 && velocityX >= 1000) {
            if (activity != null && !activity.isFinishing()) {
                activity.backOfFinish();
            }
            return true;
        }

        return false;
    }
}
