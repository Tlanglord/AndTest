package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {


    public ArcView(Context context) {
        super(context);
    }


    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getHeight() / 5;
        int color = Color.parseColor("#4499ff");
        Paint paint1 = new Paint();
        paint1.setColor(color);

        RectF f1 = new RectF();

        f1.set(0, 0, h, h);
        canvas.drawArc(f1, 0, 90, true, paint1);

        Paint paint2 = new Paint();
        paint2.setColor(color);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(10);
        RectF f2 = new RectF();
        f2.set(0, h, h, 2 * h);
        canvas.drawArc(f2, 0, 120, true, paint2);

        Paint paint3 = new Paint();
        paint3.setColor(color);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(1);
        RectF f3 = new RectF();
        f3.set(0, 2*h, h, 3 * h);
        canvas.drawArc(f3, 0, 120, false, paint3);
    }
}
