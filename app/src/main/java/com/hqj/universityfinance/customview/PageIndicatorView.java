package com.hqj.universityfinance.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-9-13.
 */

public class PageIndicatorView extends View {

    private static final String TAG = "PageIndicatorView";
    private Context mContext;

    private int mMaxNum;
    private int mCurrentFocus;
    private int mPreFocus;
    private int mPointGap;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int mLineTop;
    private int mLinePadding;
    private ValueAnimator mSwitchAnimator;
    private float mAnimationProgress = 1;

    private Bitmap mDefault = null;
    private Bitmap mFocus = null;

    public PageIndicatorView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initResIds(attrs);
        init();
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initResIds(attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0x707070);
        mLineTop = mDefault.getHeight() / 2;
        mPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        mLinePadding = mDefault.getWidth();
        mPointGap = getResources().getDimensionPixelSize(R.dimen.page_indicator_padding_left);
    }

    private void initResIds(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.indicator);
        int defaultResId = a.getResourceId(R.styleable.indicator_default_src, 0);
        int focusResId = a.getResourceId(R.styleable.indicator_focus_src, 0);
        if (defaultResId != 0 && focusResId != 0) {
            mDefault = BitmapFactory.decodeResource(getContext().getResources(),
                    defaultResId);
            mFocus = BitmapFactory.decodeResource(getContext().getResources(),
                    focusResId);
        }

        a.recycle();
    }

    public void setMaxNum(int num) {
        mMaxNum = num;
    }

    public int getMaxNum() {
        return mMaxNum;
    }

    public void setCurrentFocus(int currentFocus) {
        if (currentFocus != mCurrentFocus) {
            mPreFocus = mCurrentFocus;
            mCurrentFocus = currentFocus;
            startSwitchAnimator();
        }
    }

    private void startSwitchAnimator() {
        cancelSwitchAnimator();
        mSwitchAnimator = ValueAnimator.ofFloat(0, 1f);
        mSwitchAnimator.setDuration(200);
        mSwitchAnimator.addUpdateListener(mAnimatorUpdateListener);
        mSwitchAnimator.addListener(mAnimatorListener);
        mSwitchAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimationProgress = (Float) animation.getAnimatedValue();
            invalidate();
        }
    };

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }
    };

    public void cancelSwitchAnimator() {
        if (mSwitchAnimator != null && mSwitchAnimator.isRunning()) {
            mSwitchAnimator.cancel();
        }
        mSwitchAnimator = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDefault != null) {
            int h = Math.max(mDefault.getHeight(), mFocus.getHeight());
            setMinimumHeight(h);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDefault == null || mFocus == null || mMaxNum <= 0) {
            return;
        }
        int padding = mLinePadding;

        int left = (getWidth()
                - mFocus.getWidth()
                - (mMaxNum - 1) * mDefault.getWidth()
                - mPointGap * (mMaxNum - 1))
                / 2;
        final int hDefault = (getHeight() - mDefault.getHeight()) / 2;
        final int hFocus = (getHeight() - mFocus.getHeight()) / 2;
        int lineTop = mLineTop + hDefault;
        mPaint.setAlpha(0x10);

        float p = mAnimationProgress;

        for (int i = 0; i < mMaxNum; i++) {
            Bitmap defaultBmp = mDefault;
            Bitmap focusBmp = mFocus;
            Log.d(TAG, "onDraw: wangjuncheng left = "+left);
            if (i != mCurrentFocus) {
                if (i != mPreFocus) {
                    Log.d(TAG, "onDraw: wangjuncheng 1");
                    mPaint.setAlpha(0xff);
                    canvas.drawBitmap(defaultBmp, left, hDefault, null);
                } else {
                    Log.d(TAG, "onDraw: wangjuncheng 2");
                    mPaint.setAlpha((int) (0xff * (1 - p)));
                    canvas.drawBitmap(focusBmp, left, hFocus, mPaint);
                    mPaint.setAlpha((int) (0xff * p));
                    canvas.drawBitmap(defaultBmp, left, hDefault, mPaint);
                }
                left += defaultBmp.getWidth();
            } else {
                Log.d(TAG, "onDraw: wangjuncheng 3");
                mPaint.setAlpha((int) (0xff * (1 - p)));
                canvas.drawBitmap(defaultBmp, left, hDefault, mPaint);
                mPaint.setAlpha((int) (0xff * p));
                canvas.drawBitmap(focusBmp, left, hFocus, mPaint);
                left += focusBmp.getWidth();
            }

            if (i != mMaxNum - 1) {
                left += mPointGap;
            }
        }
    }
}
