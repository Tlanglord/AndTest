package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TextSampleView extends View {

    public TextSampleView(Context context) {
        super(context);
    }

    public TextSampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = "KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看KKK考虑看看";
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(40);

        if (Build.VERSION.SDK_INT > 22) {
            canvas.drawTextRun(str, 0, str.length(), 0, str.length(), 0, 40, false, paint);
        }
    }
}
