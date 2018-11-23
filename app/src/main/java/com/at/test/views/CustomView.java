package com.at.test.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class CustomView extends View {

    private Paint mPaint;
    private RectF mBounds;
    private float width;
    private float height;
    private float radius;
    private float smallLength;
    private float largeLength;
    private float mBorderWidth;
    private int mBorderColor;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //	public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		init(context);
//	}
//
//	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//		init(context);
//
//	}
//
//	public CustomView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		//		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
//		//		mBorderColor = a.getColor(R.styleable.CustomView_border_color, 0xff000000);
//		//		mBorderWidth = a.getDimension(R.styleable.CustomView_border_width, 2);
//		//		a.recycle();
//
////		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
////		mBorderColor = a.getColor(R.styleable.CustomView_border_color, 0xff000000);
////		mBorderWidth = a.getDimension(R.styleable.CustomView_border_width, 2);
////		a.recycle();
////		//				TypedArray a = context.getTheme().obtainStyledAttributes(R.styleable.CustomView);
////		//				mBorderColor = a.getColor(R.styleable.CustomView_border_color, 0xff000000);
////		//				mBorderWidth = a.getDimension(R.styleable.CustomView_border_width, 2);
////		//				a.recycle();
////		init(context);
//	}

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);

//		mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());
//
//		width = mBounds.right - mBounds.left;
//		height = mBounds.bottom - mBounds.top;
//
//		if (width < height) {
//			radius = width / 4;
//		} else {
//			radius = height / 4;
//		}
//
//		smallLength = 10;
//		largeLength = 20;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        width = mBounds.right - mBounds.left;
        height = mBounds.bottom - mBounds.top;

        if (width < height) {
            radius = width / 4;
        } else {
            radius = height / 4;
        }

        smallLength = 10;
        largeLength = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        canvas.drawColor(0xff000000);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(new RectF(mBounds.centerX() - (float) 0.9 * width / 2, mBounds.centerY() - (float) 0.9 * height / 2, mBounds.centerX()
                + (float) 0.9 * width / 2, mBounds.centerY() + (float) 0.9 * height / 2), 30, 30, mPaint);
        mPaint.setColor(mBorderColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), radius, mPaint);
        float start_x, start_y;
        float end_x, end_y;
        for (int i = 0; i < 60; ++i) {
            start_x = radius * (float) Math.cos(Math.PI / 180 * i * 6);
            start_y = radius * (float) Math.sin(Math.PI / 180 * i * 6);
            if (i % 5 == 0) {
                end_x = start_x + largeLength * (float) Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y + largeLength * (float) Math.sin(Math.PI / 180 * i * 6);
            } else {
                end_x = start_x + smallLength * (float) Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y + smallLength * (float) Math.sin(Math.PI / 180 * i * 6);
            }
            start_x += mBounds.centerX();
            end_x += mBounds.centerX();
            start_y += mBounds.centerY();
            end_y += mBounds.centerY();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        }
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), 20, mPaint);
        canvas.rotate(60, mBounds.centerX(), mBounds.centerY());
        canvas.drawLine(mBounds.centerX(), mBounds.centerY(), mBounds.centerX(), mBounds.centerY() - radius, mPaint);
    }
}
