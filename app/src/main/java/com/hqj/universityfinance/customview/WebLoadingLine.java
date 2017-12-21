package com.hqj.universityfinance.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-12-18.
 */

public class WebLoadingLine extends View {

    private int mProgress;

    private Paint mPaint;

    public WebLoadingLine(Context context) {
        super(context);
        init();
    }

    public WebLoadingLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebLoadingLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setBackground(null);

        mPaint.setStrokeWidth(3);
        mPaint.setColor(getResources().getColor(R.color.royal_blue));
    }

    public void setProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            mProgress = progress;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scale = mProgress / 100f;
        canvas.drawLine(0, getHeight()/2, getWidth() * scale, getHeight()/2, mPaint);
        canvas.drawText(mProgress + "%", getWidth() * scale, getHeight()/2, mPaint);
    }
}
