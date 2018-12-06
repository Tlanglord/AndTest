package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TextOnPathView extends View {

    public TextOnPathView(Context context) {
        super(context);
    }

    public TextOnPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextOnPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        Picture picture = new Picture();


        int h = getHeight() / 2;
        String str = "看了看是否开发卡里的看法看了看是否开发卡";
        Path path = new Path();
        RectF f = new RectF();
        f.set(0, 0, h, h);
        path.addArc(f, -180, 180);
        canvas.drawArc(f, -180, 180, false, paint);
        canvas.drawTextOnPath(str, path, 40, 40, paint);

        picture.endRecording();

    }
}
