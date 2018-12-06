package com.at.test.graphics.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getHeight() / 6;
        int color = Color.parseColor("#4499ff");

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        Path path = new Path();
        path.setFillType(Path.FillType.WINDING);

        RectF rectF1 = new RectF();
        rectF1.set(0, 0, h, h);
        path.addArc(rectF1, 0, 90);
        canvas.drawPath(path, paint);

        path.addCircle(h / 2, h + h / 2, h / 2, Path.Direction.CCW);
        canvas.drawPath(path, paint);

        path.addCircle(h / 2, 2 * h + h / 2, h / 2, Path.Direction.CW);
        canvas.drawPath(path, paint);
        path.lineTo(0, 0);
        path.rLineTo(h / 2, 4 * h);
        canvas.drawPath(path, paint);

        path.setFillType(Path.FillType.EVEN_ODD);
        paint.setStyle(Paint.Style.STROKE);
        path.addCircle(h / 2, 4 * h + h / 2, h / 2, Path.Direction.CCW);
        path.addCircle(h, 4 * h + h / 2, h / 2, Path.Direction.CCW);
        canvas.drawPath(path, paint);

        paint.reset();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{1, 3, 3}, 5));
        path.addCircle(h + h, 4 * h + h / 2, h / 2, Path.Direction.CCW);
        paint.setPathEffect(new CornerPathEffect(50));
        path.addCircle(2 * h + h / 2, 4 * h + h / 2, h / 2, Path.Direction.CW);
        canvas.drawPath(path, paint);

        path.quadTo(h, h, 4 * h, 5 * h);
        path.rewind();
        path.reset();

        path.cubicTo(h, h, 2 * h, 3 * h, 4 * h, 5 * h);
        canvas.drawPath(path, paint);

        Paint paintN = new Paint();
        paintN.setStrokeWidth(10);
        paintN.setColor(Color.GREEN);
        paintN.setStyle(Paint.Style.STROKE);
        paintN.setAntiAlias(true);

        Path pathN = new Path();
        pathN.moveTo(100, 100);
        pathN.lineTo(450, 100);
        pathN.lineTo(100, 300);
        paintN.setStrokeJoin(Paint.Join.MITER);
        canvas.drawPath(pathN, paintN);

        pathN.moveTo(100, 400);
        pathN.lineTo(450, 400);
        pathN.lineTo(100, 600);
        paintN.setStrokeJoin(Paint.Join.BEVEL);
        canvas.drawPath(pathN, paintN);

        pathN.moveTo(100, 700);
        pathN.lineTo(450, 700);
        pathN.lineTo(100, 900);
        paintN.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(pathN, paintN);
    }
}
