package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PictureView extends View {

    public PictureView(Context context) {
        super(context);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Picture picture = new Picture();
        Canvas canvas1 = picture.beginRecording(getWidth(), getHeight());
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.RED);
        canvas1.drawText("的考虑删了但是圣诞快乐是", 0, getHeight() / 2, paint);
        picture.endRecording();

        canvas.drawPicture(picture);
    }
}
