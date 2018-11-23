
package com.at.test.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


public class SuperListView extends ListView implements OnScrollListener {
	public SuperListView(Context context) {
		super(context);
	}

	public SuperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//	public SuperListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		initView(context);
//	}

//	public SuperListView(Context context, AttributeSet attrs, int defStyleAttr) {
//		this(context, attrs, 0, 0);
//	}
//
//	public SuperListView(Context context, AttributeSet attrs) {
//		this(context, attrs, 0);
//	}
//
//	public SuperListView(Context context) {
//		this(context, null);
//	}

	private View mHeader;
	private View mFooter;
	private OnRefreshListener mRefreshListener;
	private OnLoadListener mLoadListener;

	public void setRefreshListener(OnRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public void setLoadListener(OnLoadListener loadListener) {
		this.mLoadListener = loadListener;
	}

	private void initView(Context context) {
//		mHeader = LayoutInflater.from(context).inflate(R.layout.refresh_header, null);
//		mFooter = LayoutInflater.from(context).inflate(R.layout.load_footer, null);
//		addHeaderView(mHeader);
//		addFooterView(mFooter);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.v("Listview", "dispatchTouchEvent");
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.v("Listview", "onInterceptTouchEvent");
		return super.onInterceptTouchEvent(ev);
	}

	private static final int PULL_TO_REFRESH = 1;
	private static final int RELEASE_TO_REFRESH = 2;
	private static final int REFESHING = 3;
	private static final int DONE = 4;

	private int mCurrentState = DONE;
	private int mStartY = 0;
	private int mHeaderRF;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int mLastY = 0;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (mCurrentState == RELEASE_TO_REFRESH) {
				mHeader.setPadding(0, 0, 0, 0);
				mRefreshListener.onRefresh();
				mCurrentState = REFESHING;
			} else if (mCurrentState == REFESHING) {

			} else {
				mCurrentState = DONE;
				changeHeaderState();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			mLastY = (int) ev.getY();
			int h = (mLastY - mStartY);
			if (getFirstVisiblePosition() == 0 && h > 0) {
				int rh = (h - mHeaderRF);
				if (rh > 0) {
					mCurrentState = RELEASE_TO_REFRESH;
					mHeader.setPadding(0, rh, 0, 0);
				} else {
					mCurrentState = PULL_TO_REFRESH;
				}
			} else {
				mCurrentState = DONE;
			}
			changeHeaderState();
			break;
		case MotionEvent.ACTION_CANCEL:
			mCurrentState = DONE;
			changeHeaderState();
			break;
		}
		Log.v("Listview", "onTouchEvent");
		return super.onTouchEvent(ev);
	}

	private void changeHeaderState() {
		switch (mCurrentState) {
		case RELEASE_TO_REFRESH:
			mHeader.setVisibility(View.VISIBLE);
			break;
		case REFESHING:
			mHeader.setVisibility(View.VISIBLE);
			break;
		case PULL_TO_REFRESH:
			mHeader.setVisibility(View.VISIBLE);
		case DONE:
			mHeader.setVisibility(View.GONE);
			break;
		}
	}

	public void OnRefreshComplete() {
		mCurrentState = DONE;
		changeHeaderState();
	}

	public void onLoadCompelte() {
		mIsLoading = false;
	}

	public interface OnRefreshListener {
		void onRefresh();
	}

	public interface OnLoadListener {
		void onLoad();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			if ((getLastVisiblePosition() == getPositionForView(view)) && !mIsLoading) {
				mLoadListener.onLoad();
				mIsLoading = true;
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			break;
		}

	}

	private boolean mIsLoading = false;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

}
