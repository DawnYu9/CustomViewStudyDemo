package com.bubble.customviewstudydemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.bubble.customviewstudydemo.R;
import com.bubble.customviewstudydemo.utils.StringUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @description 点击文本产生一个4位随机数字
 * @date 17/5/9
 */

public class CustomRandomTextView extends CustomBaseView {
    /**
     * 文本
     */
    private String mTitleText;

    /**
     * 文本的颜色
     */
    private int mTitleTextColor;

    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mTextBound;
    private Paint mPaint;

    public CustomRandomTextView(Context context) {
        this(context, null);
    }

    public CustomRandomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRandomTextView(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);

        init(set, defStyle, R.styleable.CustomRandomTextView);
    }

    @Override
    public void init(AttributeSet set, int defStyleAttr, int[] attrs) {
        super.init(set, defStyleAttr, attrs);

        // 获得绘制文本的宽和高
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);

        mTextBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                postInvalidate();
            }

        });
    }

    @Override
    public void getAttrs(AttributeSet set, int defStyleAttr, int[] attrs) {
        super.getAttrs(set, defStyleAttr, attrs);

        mTitleText = typedArray.getString(R.styleable.CustomRandomTextView_titleText);

        mTitleTextColor = typedArray.getColor(R.styleable.CustomRandomTextView_titleTextColor, Color.BLACK);

        mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomRandomTextView_titleTextSize, StringUtils.getSp(mContext, 16));

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
            float textWidth = mTextBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
            float textHeight = mTextBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        int randomInt;
        while (set.size() < 4) {
            randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return sb.toString();
    }
}
