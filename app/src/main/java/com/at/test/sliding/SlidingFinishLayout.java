package com.at.test.sliding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by dongqiangqiang on 2017/6/6.
 */

public class SlidingFinishLayout extends RelativeLayout implements View.OnTouchListener {

    private static final String TAG = "SlidingFinishLayout";

    public static final int MOVE_EDGE_PXIEL = 500;
    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private float mRawDownX;
    /**
     * 按下点的Y坐标
     */
    private float mDownY;
    /**
     * 临时存储X坐标
     */
    private float mTempX;
    /**
     * 临时存储y坐标
     */
    private float mDownX;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SildingFinishLayout的宽度
     */
    private float mDecorWidth;
    private float mDecorHeight;
    /**
     * 记录是否正在滑动
     */
    private boolean isSliding;

    private boolean isOpenSliding;

    private OnSlidingFinishListener mOnSildingListener;
    private VelocityTracker mTracker;
    private boolean isFinish;
    boolean mEgde = false;
    private Paint mPaint;
    private Activity mActivity;
    private ViewGroup mDecorView;
    private View mRootView;

    public SlidingFinishLayout(Context context) {
        this(context, null);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mActivity = (Activity) context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        setClickable(true);
        setOnTouchListener(this);
        mTracker = VelocityTracker.obtain();
        mPaint = new Paint();
        mPaint.setStrokeWidth(0);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
    }

    public void bind() {
        Window window = mActivity.getWindow();
        mDecorView = (ViewGroup) window.getDecorView();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDecorView.setBackgroundColor(Color.TRANSPARENT);
        mRootView = mDecorView.getChildAt(0);
        mDecorView.removeView(mRootView);
        addView(mRootView);
        mDecorView.addView(this);
        BackUtil.convertActivityToTranslucent(mActivity);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mDecorWidth = getWidth();
            mDecorHeight = getHeight();
        }
    }

    public boolean isOpenSliding() {
        return isOpenSliding;
    }

    public void setEnableSliding(boolean openSliding) {
        isOpenSliding = openSliding;
    }

    public void setOnSlidingFinishListener(
            OnSlidingFinishListener onSildingFinishListener) {
        this.mOnSildingListener = onSildingFinishListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogM.v(TAG, "case dispatchTouchEvent");
        if (isOpenSliding) {

            mTracker.addMovement(ev);
            final int action = ev.getAction() & MotionEvent.ACTION_MASK;

            if (action == MotionEvent.ACTION_DOWN) {
                mRawDownX = mTempX = ev.getRawX();
                mDownY = ev.getY();
                mDownX = ev.getX();
            }

            if (action == MotionEvent.ACTION_MOVE) {
                mTracker.computeCurrentVelocity(100);
                float xV = mTracker.getXVelocity();
                float minY = Math.abs(ev.getY() - mDownY);
                float minX = Math.abs(ev.getX() - mDownX);
//                float yV = mTracker.getXVelocity();
//                float minY = Math.abs(ev.getY() - mDownY);
                LogM.v(TAG, "case dispatchTouchEvent xv:" + xV);
                LogM.v(TAG, "case dispatchTouchEvent rawX:" + ev.getRawX());
                LogM.v(TAG, "case dispatchTouchEvent minXY:" + (minX - minY));
                if (ev.getRawX() <= MOVE_EDGE_PXIEL && minX > 0 && xV > 10 && minX > minY) {
                    mEgde = true;
                    LogM.v(TAG, "case dispatchTouchEvent egde:" + mEgde);
                }
            }


            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
                LogM.v(TAG, "case dispatchTouchEvent ACTION_UP");
                resetRootScroll();
            }

            if (mEgde) {
                onInterceptTouchEvent(ev);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogM.v(TAG, "case onInterceptTouchEvent");
        if (!isOpenSliding) {
            return super.onInterceptTouchEvent(ev);
        }

        float minY = Math.abs(ev.getY() - mDownY);
        float minX = Math.abs(ev.getX() - mDownX);

        LogM.v(TAG, "case onInterceptTouchEvent minX > minY :" + (minX > minY));

        if (mEgde && minX > minY) {
            LogM.v(TAG, "case onInterceptTouchEvent egde:" + mEgde);
            BackUtil.convertActivityToTranslucent(mActivity);
            return mEgde;
        }

        LogM.v(TAG, "case onInterceptTouchEvent not egde:" + mEgde);
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogM.v(TAG, "onTouch");
        if (!isOpenSliding || !mEgde) {
            return false;
        }
        LogM.v(TAG, "onTouch mEgde");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogM.v(TAG, "case onTouch ACTION_DOWN");
                mRawDownX = mTempX = event.getRawX();
                mDownY = event.getY();
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                LogM.v(TAG, "case onTouch ACTION_MOVE");
                float moveX = event.getRawX();
                int deltaX = (int) (mTempX - moveX);
                float minY = Math.abs(event.getY() - mDownY);
                float minX = Math.abs(event.getX() - mDownX);
                mTempX = moveX;
                isSliding = false;

                if (Math.abs(moveX - mRawDownX) > mTouchSlop) {
                    isSliding = true;
                }

                int scrollX = mRootView.getScrollX();
                if (isSliding) {
                    if (isRightEdge(deltaX, scrollX)) {
                        scrollOrigin();
                    } else {
                        mRootView.scrollBy(deltaX, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
//                LogM.v(TAG, "case onTouchEvent ACTION_UP");
                resetRootScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
//                LogM.v(TAG, "case onTouchEvent ACTION_UP ACTION_CANCEL");
                resetRootScroll();
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                LogM.v(TAG, "case onTouchEvent ACTION_UP ACTION_POINTER_UP");
                resetRootScroll();
                break;
        }
        return true;
    }

    private boolean isRightEdge(int deltaX, int scrollX) {
        return scrollX + deltaX >= 0;
    }

    private void resetRootScroll() {
        isSliding = false;
        mEgde = false;
        if (mRootView.getScrollX() <= -mDecorWidth / 4) {
            isFinish = true;
            scrollRight();
        } else {
            scrollOrigin();
            isFinish = false;
        }
    }

    private void scrollRight() {
//        LogM.v(TAG, "scrollRight");
        float scrollX = mRootView.getScrollX();
        float deltaX = mDecorWidth + scrollX;
        mScroller.startScroll((int) scrollX, 0, (int) -deltaX, 0,
                (int) Math.abs(deltaX));
        mRootView.postInvalidate();
        mRootView.requestLayout();
    }

    private void scrollOrigin() {
//        LogM.v(TAG, "scrollOrigin");
        int deltaX = mRootView.getScrollX();
        if (deltaX > 0) {
            deltaX = 0;
        } else if (deltaX == 0) {
            return;
        }
        mScroller.startScroll(deltaX, 0, -deltaX, 0,
                Math.abs(deltaX) * 2);
        mRootView.postInvalidate();
        mRootView.requestLayout();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mRootView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mRootView.postInvalidate();
            mRootView.requestLayout();

            if (mScroller.isFinished()) {
                if (mOnSildingListener != null && isFinish) {
                    mOnSildingListener.onSlidingFinish();
                }
            }
        }
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        drawBackground(canvas);
//        drawShadow(canvas);
//        super.dispatchDraw(canvas);
//    }

    private void drawBackground(Canvas canvas) {
        LogM.v(TAG, "case drawBackground");
        float shadowWidth = -mRootView.getScrollX();
        float ratio = shadowWidth / mDecorWidth;
        LogM.v(TAG, "case drawBackground ratio:" + ratio);
        mPaint.setAlpha((int) ((1 - ratio) * 180));
        canvas.drawRect(shadowWidth, 0, 0, mDecorHeight, mPaint);
    }

    private void drawShadow(Canvas canvas) {
        canvas.save();
        int width = -mRootView.getScrollX();
        Shader shader = new LinearGradient(width - 40, 0, width, 0, new int[]{Color.parseColor("#1edddddd"), Color.parseColor("#6e666666"), Color.parseColor("#9e666666")}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(shader);
        RectF rectF = new RectF(width - 40, 0, width, mDecorHeight);
        canvas.drawRect(rectF, mPaint);
        canvas.restore();
    }


    public interface OnSlidingFinishListener {
        void onSlidingFinish();
    }
}
