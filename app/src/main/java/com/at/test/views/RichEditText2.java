package com.at.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class RichEditText2 extends AppCompatEditText implements OnLongClickListener, TextWatcher, OnFocusChangeListener, OnEditorActionListener, OnKeyListener {

    private Context mContext;
    private String imgUrl = "";
    private Editable mEditable;

    public void notifyDataSetChanged() {
        //		addDefaultImage(imgUrl);
        addImage(BitmapFactory.decodeFile(imgUrl), imgUrl);
    }

    public void addDefaultImage(String filePath, int start, int end) {
        Log.i("imgpath", filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        // ��ȡ��Ļ�Ŀ��
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        int paddingLeft = getPaddingLeft();
//        int paddingRight = getPaddingRight();
//        int bmWidth = bitmap.getWidth();//ͼƬ�߶�
//        int bmHeight = bitmap.getHeight();//ͼƬ���
//        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
//        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);
//        Bitmap newBitmap = zoomImage(bitmap, zoomWidth, zoomHeight);
//        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
//        spanString.setSpan(imgSpan, 0, pathTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        if (mEditable == null) {
//            mEditable = this.getText(); // ��ȡedittext����
//        }
//        mEditable.delete(start, end);//ɾ��
//        mEditable.insert(start, spanString); // ����spanStringҪ��ӵ�λ��
    }

    public void addImage(Bitmap bitmap, String filePath) {
        Log.i("imgpath", filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        // ��ȡ��Ļ�Ŀ��
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int bmWidth = bitmap.getWidth();//ͼƬ�߶�
        int bmHeight = bitmap.getHeight();//ͼƬ���
        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);
        Bitmap newBitmap = zoomImage(bitmap, zoomWidth, zoomHeight);
        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mEditable == null) {
            mEditable = this.getText(); // ��ȡedittext����
        }
        int start = this.getSelectionStart(); // ��������ӵ�λ��

        if (spanString.length() < start) {
            start = spanString.length();
        }
        mEditable.insert(start, spanString); // ����spanStringҪ��ӵ�λ��
        this.setText(mEditable);
        this.setSelection(start, spanString.length());
    }

    private Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // ��ȡ���ͼƬ�Ŀ�͸�
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        //������Ϊ0 ����ԭͼ
        if (newWidth == 0) {
            newWidth = width;
            newHeight = height;
        }
        // ��������ͼƬ�õ�matrix����
        Matrix matrix = new Matrix();
        // ������������
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // ����ͼƬ����
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

//    public RichEditText2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        mContext = context;
//        initEvent();
//    }

    public RichEditText2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initEvent();
    }

    public RichEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initEvent();
    }

    private void initEvent() {
        setOnLongClickListener(this);
        setOnEditorActionListener(this);
        setOnFocusChangeListener(this);
        setOnKeyListener(this);
        setSelection(2);
        requestFocus();
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public RichEditText2(Context context) {
        super(context);
        mContext = context;
        setOnLongClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        FontMetrics fontMetrics = getPaint().getFontMetrics();

        int textH = (int) (fontMetrics.bottom - fontMetrics.top + fontMetrics.leading);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#4499ff"));
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        int text2H = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        int y = (int) (+getTextSize());
        int line1H = (int) (fontMetrics.bottom - fontMetrics.top + fontMetrics.leading + 2);
        //		inttextHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
        //		 + textH + getLineSpacingExtra()

        int text3H = (int) (textH + getLineSpacingExtra() * 1.1);
        int lineH = (int) (getLineHeight() + getTextSize());
        for (int i = 0; i < height / lineH; i++) {
            //canvas.drawLine(0, y+2, width, y+2, paint);
            //	y+=getLineHeight();
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(mContext, "EEEEEEE", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //		Log.v("rich", getSelectionStart() + "");
        //		Log.v("rich", getSelectionEnd() + "");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.KEYCODE_ENTER:

                break;
        }

        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        requestLayout();
        invalidate();
    }

}
