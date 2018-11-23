package com.at.test.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;


public class FadeListView extends ListView implements OnScrollListener {

    private OnAlphaChangeListener mAlphaChange;
    private FrameLayout mHeaderContainer;
    private ImageView mHeaderDefaultView;

    public OnAlphaChangeListener getAlphaChange() {
        return mAlphaChange;
    }

    public void setOnAlphaChangeListener(OnAlphaChangeListener mAlphaChange) {
        this.mAlphaChange = mAlphaChange;
        this.setOnScrollListener(this);
    }

    @SuppressLint("NewApi")
    public FadeListView(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public FadeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public FadeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public FadeListView(Context context) {
        super(context);
        init(context);
        this.setOnScrollListener(this);
        // TODO Auto-generated constructor stub
    }

    private void init(Context context) {
//        this.mHeaderContainer = new FrameLayout(context);
//        mHeaderDefaultView = new ImageView(context);
//        mHeaderContainer.setBackgroundColor(Color.parseColor("#4499ff"));
//        mHeaderDefaultView.setImageResource(R.drawable.img_top_hotel_details);
//        mHeaderDefaultView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        mHeaderContainer.addView(mHeaderDefaultView);
//        addHeaderView(mHeaderContainer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //VelocityTrackerCompat velocityTrackerCompat = new VelocityTrackerCompat();

        float lastY = 0;
        float curY = 0;
        int alpha = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // curY = ev.getY();
                // alpha = getScrollY();
                // alpha = (int)(curY - lastY);
                // Log.v("alpha", "" + alpha);
                // Log.v("alpha", "alpha / 100" + alpha / 10);

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnAlphaChangeListener {
        void onAlphaChange(float alpha, int def);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int firstVisiblePos = view.getFirstVisiblePosition();
        int alpha = getCustomerScrollY(firstVisiblePos);
        Log.v("alpha", "" + alpha);
        float t = 100 + alpha;
        Log.v("alpha", "getBottom --- " + getBottom());
        Log.v("alpha", "mHeaderContainer ---getBottom --- " + mHeaderContainer.getBottom());

        if (t < 100 && t > 0) {
            Log.v("alpha", "111111111--" + t / 100);
            mAlphaChange.onAlphaChange(t, 0);
        }
    }

    public int getCustomerScrollY(int firstVisiblePos) {

        View c = null;
        if (firstVisiblePos == 0) {
            c = this.getChildAt(1);
        } else {
            c = this.getChildAt(0);
        }

        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = this.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

}
