package com.at.test.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuperHorizontalScrollView extends HorizontalScrollView {

	private int mCurrentPos = 0;
	private int mLastPos = 0;
	private int mFirstVisiblePosition;
	private int mLastVisiblePosition;
	private boolean isInited = false;


	public SuperHorizontalScrollView(Context context) {
		super(context);
	}

	public SuperHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public int getFirstVisiblePosition() {
		return mFirstVisiblePosition;
	}

	public void setFirstVisiblePosition(int firstVisiblePosition) {
		this.mFirstVisiblePosition = firstVisiblePosition;
	}

	public int getLastVisiblePosition() {
		return mLastVisiblePosition;
	}

	public void setLastVisiblePosition(int lastVisiblePosition) {
		this.mLastVisiblePosition = lastVisiblePosition;
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////		initVisiblePostion();
//	}

	private void initVisiblePostion() {
		if (!isInited) {
			int width = 0;
			int pos = 0;
			LinearLayout v = getLinearLayout();
			for (int i = 0; i < v.getChildCount(); i++) {
				View view = v.getChildAt(i);
				width += view.getMeasuredWidth();
				if (width >= getMeasuredWidth()) {
					pos = i;
					break;
				}
			}
			setFirstVisiblePosition(0);
			setLastVisiblePosition(pos);
			isInited = true;
		}
	}

	public void setCurrentPos(int currentPos) {
		mCurrentPos = currentPos;
		if (mCurrentPos > mLastVisiblePosition) {
			scrollRight(mCurrentPos);
		} else if (mCurrentPos < mFirstVisiblePosition) {
			scrollLeft(mCurrentPos);
		} else {
			setCurrentView();
		}
	}

	private void scrollLeft(int currentPos) {
		if (currentPos >= 0) {
			int width = getChildWidth(currentPos);
			smoothScrollBy(-width, 0);
			setCurrentView();
			setFirstVisiblePosition(--mFirstVisiblePosition);
			setLastVisiblePosition(--mLastVisiblePosition);
		}
	}

	private void scrollRight(int currentPos) {
		if (currentPos < getLinearChildCount()) {
			int width = getChildWidth(currentPos);
			smoothScrollBy(width, 0);
			setCurrentView();
			setLastVisiblePosition(++mLastVisiblePosition);
			setFirstVisiblePosition(++mFirstVisiblePosition);
		}
	}

	private void setCurrentView() {
		if (getLinearChildView(mLastPos) != null && getLinearChildView(mCurrentPos) != null) {
			getLinearChildView(mLastPos).setBackgroundResource(0);
			getLinearChildView(mCurrentPos).setBackgroundColor(Color.parseColor("#4499ff"));
			mLastPos = mCurrentPos;
		}
	}

	public int getChildWidth(int index) {
		return getLinearChildWidth(index);
	}

	private int getLinearChildWidth(int index) {
		if (getLinearChildView(index) != null) {
			return getLinearChildView(index).getMeasuredWidth();
		}
		return 0;
	}

	public void addView(View child) {
		if (!isLinearEmpty()) {
			getLinearLayout().addView(child);
		}
	}

	public void addView(View child, int index) {
		if (!isLinearEmpty()) {
			getLinearLayout().addView(child, index);
			child.setTag(index);
			child.setOnClickListener(onClickListener);
		}
	}
	
	ViewPager viewPager;
	

	public void setUpWithViewPager(){
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	};

	public void addView(View child, LinearLayout.LayoutParams params) {
		if (!isLinearEmpty()) {
			getLinearLayout().addView(child, params);
		}
	}

	public void addView(View child, int width, int height) {
		if (!isLinearEmpty()) {
			getLinearLayout().addView(child, width, height);
		}
	}

	public void addView(View child, int index, LinearLayout.LayoutParams params) {
		if (!isLinearEmpty()) {
			getLinearLayout().addView(child, index, params);
		}
	}

	private boolean isLinearEmpty() {
		return getLinearLayout() == null;
	}

	private LinearLayout getLinearLayout() {
		if (getChildCount() > 0) {
			return (LinearLayout) getChildAt(0);
		}
		return null;
	}

	public int getLinearChildCount() {
		if (!isLinearEmpty()) {
			return getLinearLayout().getChildCount();
		}
		return 0;
	}

	public TextView getLinearChildView(int index) {
		if (!isLinearEmpty() && index < getLinearChildCount()) {
			return (TextView) getLinearLayout().getChildAt(index);
		}
		return null;
	}

	//	@Override
	//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	//		super.onSizeChanged(w, h, oldw, oldh);
	//	}
	//
	//	@Override
	//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	//		super.onScrollChanged(l, t, oldl, oldt);
	//		int direct = l - oldl;
	//		if (direct > 0) {
	//			//scrollRight();
	//		} else {
	//			//scrollLeft(currentPos)
	//		}
	//	}
	//
	//	@Override
	//	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
	//		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	//	}
	//
	//	@Override
	//	protected int computeHorizontalScrollOffset() {
	//		Log.v("Scroll", "direct4:" + super.computeHorizontalScrollOffset());
	//		return super.computeHorizontalScrollOffset();
	//	}

	private int mLastX;
	private int mFirstX;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mFirstX = getScrollX();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			mLastX = getScrollX();

			int count = 0;
			int offset = mLastX - mFirstX;
			// right
			if (offset > 0) {
				count = getScrolledViewCount(true, offset);
				setFirstVisiblePosition(mFirstVisiblePosition + count);
				setLastVisiblePosition(mLastVisiblePosition + count);
			} else {
				count = getScrolledViewCount(false, -offset);
				setFirstVisiblePosition(mFirstVisiblePosition - count);
				setLastVisiblePosition(mLastVisiblePosition - count);
			}
			break;
		}
		return true;
	}

	private int getScrolledViewCount(boolean right, int offset) {
		int pos = 0;
		int w = 0;
		int count = 0;
		if (right) {
			for (int i = getFirstVisiblePosition(); i < getLinearChildCount(); i++) {
				w += getLinearChildWidth(i);
				count++;
				if (w >= offset) {
					pos = count;
					break;
				}
			}
		} else {
			for (int i = getLastVisiblePosition(); i > getFirstVisiblePosition(); i--) {
				w += getLinearChildWidth(i);
				count++;
				if (w >= offset) {
					pos = count;
					break;
				}
			}
		}
		return pos;
	}

}
