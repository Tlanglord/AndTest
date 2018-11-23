package com.at.test.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	/**
	 * @param context
	 */
	public FlowLayout(Context context) {
		super(context);
	}

	@SuppressLint("NewApi")
	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = resolveSize(0, widthMeasureSpec);
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		int childLeft = paddingLeft;
		int childRight = paddingRight;

		int lineHeight = 0;
		int parentHeight = 0;

		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			LayoutParams childLayoutParams = childView.getLayoutParams();
			childView.measure(
					getChildMeasureSpec(widthMeasureSpec, paddingRight
							+ paddingLeft, childLayoutParams.width),
					getChildMeasureSpec(heightMeasureSpec, paddingTop
							+ paddingBottom, childLayoutParams.height));

			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();

			lineHeight = Math.max(lineHeight, childHeight);

			if (childWidth + childLeft + childRight > width) {
				parentHeight += lineHeight;
				childLeft = paddingLeft;
			} else {
				childLeft += childWidth + 20;
			}
		}

		parentHeight += lineHeight +paddingBottom;
		setMeasuredDimension(width,
				resolveSize(parentHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int parentWidth = getMeasuredWidth();

		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();
		

		int childLeft = paddingLeft;
		int childRight = paddingRight;
		
		int lineHeight = 0;
		int parentHeight = 0;

		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();
			
			
			lineHeight = Math.max(childHeight, lineHeight);

			if (childWidth + childLeft + childRight> parentWidth) {
				parentHeight += lineHeight;
				childLeft = paddingLeft;
				lineHeight = childHeight;
			}

			childView.layout(childLeft, parentHeight, childLeft + childWidth,
					parentHeight + childHeight);
			childLeft += childWidth + 20;
		}
	}

	@Override
	public boolean onDragEvent(DragEvent event) {
		return super.onDragEvent(event);
	}

}
