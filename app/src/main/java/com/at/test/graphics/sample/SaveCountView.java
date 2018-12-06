package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SaveCountView extends View {

    public SaveCountView(Context context) {
        super(context);
    }

    public SaveCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SaveCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(40);

        String str = "哦哦哦哦哦哦哦哦哦";
        canvas.drawText(str, 0, 40, paint);
        canvas.save();
        canvas.rotate(30);
        str = "也一样一样一样";
        canvas.drawText(str, 0, 80, paint);
        canvas.restore();

        int x = getMeasuredWidth();
        int y = getMeasuredWidth();

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(x / 2, y / 2, x / 2, paint);

        paint.setColor(Color.BLUE);
        canvas.drawLine(x / 2, y / 8, x / 2, 0, paint);

        canvas.save();//注意，这里调用了保存canvas的方法

        for (int i = 0; i < (360 / 30); i++) {
            canvas.rotate(30, x / 2, y / 2);
            canvas.drawLine(x / 2, y / 8, x / 2, 0, paint);
        }
        canvas.restoreToCount(canvas.getSaveCount());
    }
}
