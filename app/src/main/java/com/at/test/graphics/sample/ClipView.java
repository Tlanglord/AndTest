package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
    public ClipView(Context context) {
        super(context);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getHeight();

        int color = Color.parseColor("#4499ff");
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(40);

        String str = "绝大部分社区，都是披着社交的内容产品";
        float str_w = paint.measureText(str, 0, str.length());
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fh = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(str, 0, fh, paint);


        RectF f = new RectF();
        f.set(str_w / 3, 0, str_w / 2, fh);
        canvas.clipRect(f);
        canvas.drawColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT > 25) {
            canvas.clipOutRect(f);
            canvas.drawColor(Color.RED);
        }

    }
}
