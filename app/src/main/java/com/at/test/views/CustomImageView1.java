package com.at.test.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CustomImageView1 extends AppCompatImageView {

    public final static int TYPE_CIRCLE = 0;
    public final static int TYPE_ROUND = 1;
    public final static int TYPE_OVAL = 2;

    private int mType = 0;
    private int mWidth;
    private int mRadius;
    private int mRoundRadius = 50;

    private Paint mPaint;
    private RectF mRectF;
    private Matrix mMatrix;
    private Shader mBitmapShader;

    public CustomImageView1(Context context) {
        super(context);
    }

    public CustomImageView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
    }


    //    public CustomImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr, 0);
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mMatrix = new Matrix();
//    }
//
//    public CustomImageView1(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CustomImageView1(Context context) {
//        this(context, null);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mType == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (null == getDrawable()) {
            return;
        }
        setBitmapShader();
        mRectF = new RectF(0, 0, getWidth(), getHeight());
        //		mRectF = new RectF((float)getLeft(),(float)getTop(),(float)getRight(),(float)getBottom());

        if (mType == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        } else if (mType == TYPE_ROUND) {
            mPaint.setColor(Color.RED);
            canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
        } else if (mType == TYPE_OVAL) {
            canvas.drawOval(mRectF, mPaint);
        }
    }

    private void setBitmapShader() {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);
        // bitmapΪɫһBitmapShader
        mBitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        float scale = 1.0f;
        int bSize = 0;
        if (mType == TYPE_CIRCLE) {
            // �õ�bitmap�ߵ�Сֵ
            bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bSize;
        } else if (mType == TYPE_ROUND || mType == TYPE_OVAL) {
            // �ͼƬ�Ŀ�߸�view�Ŀ�߲�ƥ�䣬�Ҫ�ŵıźͼƬ�Ŀ�ߣ�һҪview�Ŀ�ߣ�ȡֵ
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        // shader�ı任Ҫ�ڷŴС
        mMatrix.setScale(scale, scale);
        // �ñ任
        mBitmapShader.setLocalMatrix(mMatrix);
        BlurMaskFilter bmf = new BlurMaskFilter(bSize, Blur.NORMAL);
        mPaint.setMaskFilter(bmf);
        mPaint.setShader(mBitmapShader);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

}
