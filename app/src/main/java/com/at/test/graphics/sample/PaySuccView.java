package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class PaySuccView extends View {

    private DrawCircle circle;

    public PaySuccView(Context context) {
        super(context);
    }

    public PaySuccView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaySuccView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint;

    private void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circle == null) {
            circle = new DrawCircle(canvas, 500, getLeft(), getTop(), getRight(), getBottom());
        }
//        circle.start();

        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#4499ff"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.MITER);
            paint.setStrokeMiter(180);
        }
//        canvas.drawLine(0, 1, getRight(), 1, paint);

        RectF f = new RectF(10, 10, getWidth() - 10, getHeight() - 10);
        canvas.drawArc(f, 0, 360, false, paint);

        canvas.drawLine(getWidth() / 5, getHeight() / 5 * 2, getWidth() / 5 * 2, getHeight() / 3 * 2, paint);
        canvas.drawLine(getWidth() / 5 * 2, getHeight() / 3 * 2, getWidth() / 5 * 4, getHeight() / 5 * 2 - 10, paint);
//        Path path = new Path();
//        canvas.drawPath(path, paint);
    }

    public class DrawCircle {

        private Canvas canvas;
        private int duration;
        private float left, top, right, bottom;
        private Paint paint;

        public DrawCircle(Canvas canvas, int duration, float left, float top, float right, float bottom) {
            this.canvas = canvas;
            this.duration = duration;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public void start() {
            if (paint == null) {
                paint = new Paint();
                paint.setColor(Color.parseColor("#4499ff"));
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(50);
            }

            RectF f = new RectF(left, top, right, bottom);
            canvas.drawArc(f, -90, 180, false, paint);
        }
    }
}
