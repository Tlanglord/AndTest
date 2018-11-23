package com.at.test.views;


import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CircleTextView extends AppCompatTextView {

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Circle);
//        fillColor = array.getColor(R.styleable.Circle_fill_color, 0x00000000);
//        array.recycle();
    }


    //    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Circle);
//        fillColor = array.getColor(R.styleable.Circle_fill_color, 0x00000000);
//        array.recycle();
//    }

////    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
////        this(context, attrs, defStyleAttr, 0);
////
////    }
//
//    public CircleTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CircleTextView(Context context) {
//        this(context, null);
//    }

    private int fillColor = 0;


    @Override
    protected void onDraw(Canvas canvas) {

//		Drawable drawable = getBackground();
//		int bgColor = 0;
//		Paint textP = getPaint();
//		int textColor = textP.getColor();
////		int color=getTextColors().getColorForState(getDrawableState(), 0);
//		int color=getCurrentTextColor();
//		
//		textP.setColor(color);
//		setBackgroundColor(Color.parseColor("#00000000"));
//		String text = getText().toString();
//		canvas.drawColor(Color.TRANSPARENT);
//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setColor(fillColor);
//		paint.setStyle(Paint.Style.FILL);
//		paint.setStrokeWidth(3);
//		
//
//		int width = getMeasuredWidth();
//		int height = getMeasuredHeight();
//
//		Paint paint1 = new Paint();
//		paint1.setColor(Color.TRANSPARENT);
//
//		int textW = (int) getPaint().measureText(text);
//		FontMetrics fontMetrics = textP.getFontMetrics();
//		int textH = (int) (fontMetrics.bottom + fontMetrics.top);
//		RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
//		//canvas.drawRect(rectF, paint1);
//		canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width()/2, paint);
//		canvas.drawText(text, rectF.centerX() - textW / 2, rectF.centerY() - textH / 2, textP);
////
//        Paint paint = new Paint();
//        String text = getText().toString();
//        paint.setColor(Color.RED);
//        canvas.save(Canvas.CLIP_SAVE_FLAG);
//        canvas.clipRect(0, 0, 10, getMeasuredHeight());
//
//        Rect f = new Rect();
//        paint.getTextBounds(text, 0, text.length(), f);
//        canvas.drawText(text, 0, getMeasuredHeight() / 2
//                + f.height() / 2, paint);
//        canvas.restore();

    }

}
