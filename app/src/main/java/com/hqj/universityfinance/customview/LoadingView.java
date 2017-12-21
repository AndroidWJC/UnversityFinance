package com.hqj.universityfinance.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-11-26.
 */

public class LoadingView extends View {

    private static final int ANIMATOR_TIME = 1000;
    private static final int STROKE_WIDTH = 5;

    private Paint mPaintFill;
    private Paint mPaintStroke;

    private ValueAnimator mCircleArcAnimator;
    private ValueAnimator mCircleRadiusAnimator;
    private ValueAnimator mCircleAngleAnimator;
    private ValueAnimator mTickAnim;
    private ValueAnimator mCrossAnim;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private ObjectAnimator mScaleAnimator;

    private Point[] mTickPoint = new Point[3];
    private Point[] mCrossPoint = new Point[4];

    private RectF mRectF = new RectF();
    private Context mContext;
    private int mDarkColor;

    public LoadingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(STROKE_WIDTH);

        mDarkColor = ContextCompat.getColor(mContext, R.color.royal_blue);
    }

    public void startLoading() {
        clearAllAnimator();
//        mCircleArcAnimator = ValueAnimator.ofFloat(0, 360, 0);
//        mCircleArcAnimator.setDuration(ANIMATOR_TIME);
//        mCircleArcAnimator.setRepeatMode(ValueAnimator.RESTART);
//        mCircleArcAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mCircleArcAnimator.start();

        mCircleAngleAnimator = ValueAnimator.ofFloat(0, 360);
        mCircleAngleAnimator.setDuration(ANIMATOR_TIME);
        mCircleAngleAnimator.setRepeatMode(ValueAnimator.RESTART);
        mCircleAngleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCircleAngleAnimator.start();
    }

    public void loadSucceed(@Nullable Animator.AnimatorListener listener) {
        clearAllAnimator();

        mCircleRadiusAnimator = ValueAnimator.ofFloat(0, getWidth() / 2f);
        mCircleRadiusAnimator.setDuration(ANIMATOR_TIME / 2);

        mTickAnim = ValueAnimator.ofInt(0, 255);
        mTickAnim.setDuration(ANIMATOR_TIME / 2);
        if (listener != null) {
            mTickAnim.addListener(listener);
        }

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(SCALE_X, 1f, 1.2f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 1f, 1.2f, 1f);
        mScaleAnimator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY);
        mScaleAnimator.setDuration(ANIMATOR_TIME / 2);

        mAnimatorSet.play(mTickAnim).after(mCircleRadiusAnimator).with(mScaleAnimator);
        mAnimatorSet.start();
    }

    public void loadFailed(@Nullable Animator.AnimatorListener listener) {
        clearAllAnimator();

        mCircleRadiusAnimator = ValueAnimator.ofFloat(0, getWidth() / 2f);
        mCircleRadiusAnimator.setDuration(ANIMATOR_TIME / 2);

        mCrossAnim = ValueAnimator.ofInt(0, 255);
        mCrossAnim.setDuration(ANIMATOR_TIME / 2);
        if (listener != null) {
            mCrossAnim.addListener(listener);
        }

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(SCALE_X, 1f, 1.2f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 1f, 1.2f, 1f);
        mScaleAnimator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY);
        mScaleAnimator.setDuration(ANIMATOR_TIME / 2);

        mAnimatorSet.play(mCrossAnim).after(mCircleRadiusAnimator).with(mScaleAnimator);
        mAnimatorSet.start();
    }

    public void clearAllAnimator() {
        if (mCircleArcAnimator != null && mCircleArcAnimator.isRunning()) {
            mCircleArcAnimator.cancel();
        }

        if (mCircleAngleAnimator != null && mCircleAngleAnimator.isRunning()) {
            mCircleAngleAnimator.cancel();
        }

        if (mCircleRadiusAnimator != null && mCircleRadiusAnimator.isRunning()) {
            mCircleRadiusAnimator.cancel();
        }

        if (mTickAnim != null && mTickAnim.isRunning()) {
            mTickAnim.cancel();
        }

        if (mCrossAnim != null && mCrossAnim.isRunning()) {
            mCrossAnim.cancel();
        }

        if (mScaleAnimator != null && mScaleAnimator.isRunning()) {
            mScaleAnimator.cancel();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        measureTickPosition(width / 2, height / 2);
        measureCrossPosition(width / 2, height / 2);
    }

    private void measureTickPosition(int centerX, int centerY) {
        Point position = new Point();
        position.x = centerX / 2;
        position.y = centerY;
        mTickPoint[0] = position;

        position = new Point();
        position.x = centerX / 10 * 9;
        position.y = centerY + centerY / 3;
        mTickPoint[1] = position;

        position = new Point();
        position.x = centerX + centerX / 2;
        position.y = centerY / 3 * 2;
        mTickPoint[2] = position;
    }

    private void measureCrossPosition(int centerX, int centerY) {
        Point position = new Point();
        position.x = centerX / 3 * 2;
        position.y = centerY / 3 * 2;
        mCrossPoint[0] = position;

        position = new Point();
        position.x = centerX / 3 * 2;
        position.y = centerY + centerY / 3;
        mCrossPoint[1] = position;

        position = new Point();
        position.x = centerX + centerX / 3;
        position.y = centerY + centerY / 3;
        mCrossPoint[2] = position;

        position = new Point();
        position.x = centerX + centerX / 3;
        position.y = centerY / 3 * 2;
        mCrossPoint[3] = position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCircleArcAnimator != null && mCircleArcAnimator.isRunning()) {
            drawBackground(canvas, Color.WHITE);
            mPaintStroke.setColor(mDarkColor);
            mRectF.set(STROKE_WIDTH * 2, STROKE_WIDTH * 2,
                    getWidth() - STROKE_WIDTH * 2, getHeight() - STROKE_WIDTH * 2);
            canvas.drawArc(mRectF, 90, (float) mCircleArcAnimator.getAnimatedValue(), false, mPaintStroke);
            invalidate();
        }

        if (mCircleAngleAnimator != null && mCircleAngleAnimator.isRunning()) {
            drawBackground(canvas, Color.WHITE);
            mPaintStroke.setColor(mDarkColor);
            mRectF.set(STROKE_WIDTH * 2, STROKE_WIDTH * 2,
                    getWidth() - STROKE_WIDTH * 2, getHeight() - STROKE_WIDTH * 2);
            canvas.drawArc(mRectF, (float) mCircleAngleAnimator.getAnimatedValue(), 270, false, mPaintStroke);
            invalidate();
        }

        if (mCircleRadiusAnimator != null && mCircleRadiusAnimator.isRunning()) {

            mPaintFill.setColor(mDarkColor);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, mPaintFill);

            mPaintFill.setColor(Color.WHITE);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    getWidth() / 2f - (float) mCircleRadiusAnimator.getAnimatedValue(), mPaintFill);
            invalidate();
        }

        if (mTickAnim != null && mTickAnim.isRunning()) {
            drawBackground(canvas, mDarkColor);
            mPaintStroke.setAlpha((int) mTickAnim.getAnimatedValue());
            mPaintStroke.setColor(Color.WHITE);
            mPaintStroke.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(mTickPoint[0].x, mTickPoint[0].y, mTickPoint[1].x, mTickPoint[1].y, mPaintStroke);
            canvas.drawLine(mTickPoint[1].x, mTickPoint[1].y, mTickPoint[2].x, mTickPoint[2].y, mPaintStroke);
            invalidate();
        }

        if (mCrossAnim != null && mCrossAnim.isRunning()) {
            drawBackground(canvas, mDarkColor);
            mPaintStroke.setAlpha((int) mCrossAnim.getAnimatedValue());
            mPaintStroke.setColor(Color.WHITE);
            canvas.drawLine(mCrossPoint[0].x, mCrossPoint[0].y, mCrossPoint[2].x, mCrossPoint[2].y, mPaintStroke);
            canvas.drawLine(mCrossPoint[1].x, mCrossPoint[1].y, mCrossPoint[3].x, mCrossPoint[3].y, mPaintStroke);
            invalidate();
        }
    }

    private void drawBackground(Canvas canvas, int color) {
        mPaintFill.setColor(color);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, mPaintFill);
    }
}
