package com.at.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.ref.WeakReference;

public class CustomImageView extends AppCompatImageView {

    public final static int TYPE_CIRCLE = 0;
    public final static int TYPE_ROUND = 1;
    public final static int TYPE_OVAL = 2;

    private int mType = 2;
    private int mRoundBorderRadius = 50;

    private Paint mPaint;
    private Bitmap mMaskBitmap;
    private WeakReference<Bitmap> mBufferBitmap;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //	public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		mPaint = new Paint();
//	}
//
//	public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//		this(context, attrs, defStyleAttr, 0);
//	}
//
//	public CustomImageView(Context context, AttributeSet attrs) {
//		this(context, attrs, 0);
//	}
//
//	public CustomImageView(Context context) {
//		this(context, null);
//	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mType == TYPE_CIRCLE) {
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //		super.onDraw(canvas);
        Bitmap bmp = (mBufferBitmap == null ? null : mBufferBitmap.get());
        if (bmp == null || bmp.isRecycled()) {
            //��ȡdrawable
            Drawable drawable = getDrawable();
            //��ȡdrawable�Ŀ��
            int dwidth = drawable.getIntrinsicWidth();
            int dheight = drawable.getIntrinsicHeight();
            Log.v("czm", "dwidth=" + dwidth + ",width=" + getWidth());
            if (null != drawable) {
                bmp = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
                float scale = 1.0f;
                //��������
                Canvas drawCanvas = new Canvas(bmp);

                if (mType == TYPE_CIRCLE) {
                    scale = getWidth() * 1.0F / Math.min(dwidth, dheight);
                } else if (mType == TYPE_ROUND || mType == TYPE_OVAL) {
                    scale = Math.max(getWidth() * 1.0f / dwidth, getHeight() * 1.0f / dheight);
                }
                Log.v("czm", "scale=" + scale);
                //�������ű���������bounds�����൱��������ͼƬ
                drawable.setBounds(0, 0, (int) (scale * dwidth), (int) (scale * dheight));
                drawable.draw(drawCanvas);
                if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                    mMaskBitmap = getDrawBitmap();
                }
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);

                canvas.drawBitmap(bmp, 0, 0, null);
                mBufferBitmap = new WeakReference<Bitmap>(bmp);
            }

        } else {
            //������滹���ڵ����
            mPaint.setXfermode(null);
            canvas.drawBitmap(bmp, 0.0f, 0.0f, mPaint);
            return;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        mBufferBitmap = null;
        if (mMaskBitmap != null) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }


    private Bitmap getDrawBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (mType == TYPE_CIRCLE) {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        } else if (mType == TYPE_ROUND) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mRoundBorderRadius, mRoundBorderRadius, paint);
        } else if (mType == TYPE_OVAL) {
            canvas.drawOval(new RectF(0, 0, getWidth(), getHeight()), mPaint);
        }

        return bitmap;
    }

}
