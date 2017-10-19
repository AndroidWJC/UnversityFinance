package com.hqj.universityfinance.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;


public class ClearEditText extends EditText {

    private static final String TAG = "ClearEditText";

    //动画时长
    private final int ANIMATOR_TIME = 200;

    //间隔记录
    private int mInterval;
    //清除按钮宽度记录
    private int mBtnWidth;
    //右内边距
    private int mPaddingRight;
    //清除按钮的bitmap
    private Bitmap mBitmap_clear;
    private Bitmap mBitmap_visible;
    private Bitmap mBitmap_invisible;

    //出现和消失动画
    private ValueAnimator mGoneAnimator;
    private ValueAnimator mVisibleAnimator;
    //是否显示的记录
    private boolean isVisible = false;
    private boolean isPassword = false;
    private boolean isPasswordVisible = false;
    private boolean isFocused = false;

    public ClearEditText(final Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mBitmap_clear = createBitmap(ConfigUtils.CLEAR_BUTTON_RES, context);
        mBitmap_visible = createBitmap(ConfigUtils.VISIBLE_BUTTON_RES, context);
        mBitmap_invisible = createBitmap(ConfigUtils.INVISIBLE_BUTTON_RES, context);

        mInterval = getResources().getDimensionPixelSize(R.dimen.btn_in_edittext_interval);
        mBtnWidth = getResources().getDimensionPixelSize(R.dimen.btn_in_edittext_width);
        mPaddingRight = mInterval * 4 + mBtnWidth * 2;

        mGoneAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mVisibleAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATOR_TIME);

        isPassword =
                getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        Log.d(TAG, "init: isPassword = "+isPassword+", type = "+getInputType());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置右内边距, 防止清除按钮和文字重叠
        setPadding(getPaddingLeft(), getPaddingTop(), mPaddingRight, getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        Log.d(TAG, "onDraw: isVisible = "+isVisible);
        if (isVisible) {
            if (mVisibleAnimator.isRunning()) {
                float scale = (float) mVisibleAnimator.getAnimatedValue();
                Log.d(TAG, "onDraw: scale 1 = " + scale);
                drawClearButton(scale, canvas);
                if (isPassword) {
                    drawVisibleButton(scale, canvas, isPasswordVisible);
                }
                invalidate();
            } else {
                drawClearButton(1, canvas);
                if (isPassword) {
                    drawVisibleButton(1, canvas, isPasswordVisible);
                }
            }
        } else {
            if (mGoneAnimator.isRunning()) {
                float scale = (float) mGoneAnimator.getAnimatedValue();
                Log.d(TAG, "onDraw: scale 2 = "+scale);
                drawClearButton(scale, canvas);
                if (isPassword) {
                    drawVisibleButton(scale, canvas, isPasswordVisible);
                }
                invalidate();
            }
        }
    }

    /**
     * 绘制清除按钮出现的图案
     * @param translationX 水平移动距离
     * @param canvas
     */
    private void drawClearButton(float scale, Canvas canvas) {

        int right = (int) (getWidth() + getScrollX() - mInterval - mBtnWidth * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - mInterval - mBtnWidth * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mBtnWidth * scale) / 2);
        int bottom = (int) (top + mBtnWidth * scale);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);
    }

    private void drawVisibleButton(float scale, Canvas canvas, boolean isVisible) {

        int right = (int) (getWidth() + getScrollX() - mInterval * 3 - mBtnWidth - mBtnWidth * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - mInterval * 3 - mBtnWidth - mBtnWidth * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mBtnWidth * scale) / 2);
        int bottom = (int) (top + mBtnWidth * scale);
        Rect rect = new Rect(left, top, right, bottom);
        if (isVisible) {
            canvas.drawBitmap(mBitmap_visible, null, rect, null);
        } else {
            canvas.drawBitmap(mBitmap_invisible, null, rect, null);
        }

    }

    // 清除按钮出现时的动画效果
    private void startVisibleAnimator() {
        endAllAnimator();
        mVisibleAnimator.start();
        invalidate();
    }

    // 清除按钮消失时的动画效果
    private void startGoneAnimator() {
        endAllAnimator();
        mGoneAnimator.start();
        invalidate();
    }

    // 结束所有动画
    private void endAllAnimator(){
        mGoneAnimator.end();
        mVisibleAnimator.end();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocused = focused;
        if (focused && getText().length() > 0) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (text.length() > 0 && isFocused()) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            boolean clearTouched =
                    ( getWidth() - mInterval - mBtnWidth < event.getX() )
                            && (event.getX() < getWidth() - mInterval)
                            && isFocused;
            boolean visibleTouched =
                    (getWidth() - mInterval * 3 - mBtnWidth * 2 < event.getX())
                            && (event.getX() < getWidth() - mInterval * 3 - mBtnWidth)
                            && isPassword && isFocused;

            if (clearTouched) {
                setError(null);
                this.setText("");
            } else if (visibleTouched) {
                if (isPasswordVisible) {
                    isPasswordVisible = false;
                    setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    setSelection(getText().length());
                    invalidate();
                } else {
                    isPasswordVisible = true;
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setSelection(getText().length());
                    invalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // 开始晃动的动画效果
    public void startShakeAnimation(){
        if(getAnimation() == null){
            this.setAnimation(shakeAnimation(4));
        }
        this.startAnimation(getAnimation());
    }

    /**
     * 晃动动画
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    private Bitmap createBitmap(int resId, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

}

