package com.bubble.customviewstudydemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bubble.customviewstudydemo.R;
import com.bubble.customviewstudydemo.utils.StringUtils;

/**
 * @description
 * @date 17/5/10
 */

public class CustomVolumControlBar extends CustomBaseView {
    /**
     * 进度条填充色
     */
    private int progressColor;

    /**
     * 进度条背景色
     */
    private int backgroundColor;

    /**
     * 圈的宽度
     */
    private int mCircleWidth;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 当前进度，默认起始时是 3
     */
    private int mCurrentCount = 3;

    /**
     * 中间的图片
     */
    private Bitmap mImage;

    /**
     * 每个进度块间的间隙
     */
    private int mSplitSize;

    /**
     * 进度块总数
     */
    private int mCount;

    /**
     * 圆心角度数
     */
    private int mCentralAangle;

    private Rect mRect;

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet set) {
        this(context, set, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);

        init(set, defStyle, R.styleable.CustomVolumControlBar);
    }

    @Override
    public void init(AttributeSet set, int defStyleAttr, int[] attrs) {
        super.init(set, defStyleAttr, attrs);

        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    public void getAttrs(AttributeSet set, @AttrRes int defStyleAttr, @StyleableRes int[] attrs) {
        super.getAttrs(set, defStyleAttr, attrs);

        progressColor = typedArray.getColor(R.styleable.CustomVolumControlBar_progressColor, Color.BLUE);

        backgroundColor = typedArray.getColor(R.styleable.CustomVolumControlBar_backgroundColor, Color.RED);

        mImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.CustomVolumControlBar_centerPic, 0));

        mCircleWidth = typedArray.getDimensionPixelSize(R.styleable.CustomVolumControlBar_circleWidth, StringUtils.getPx(mContext, "5"));

        mCount = typedArray.getInt(R.styleable.CustomVolumControlBar_dotCount, 20);

        mSplitSize = typedArray.getInt(R.styleable.CustomVolumControlBar_splitSize, 15);

        mCentralAangle = typedArray.getInt(R.styleable.CustomVolumControlBar_centralAngle, 360);//默认是圆形，小于360则显示为圆弧

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth;

        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int desireWidth = getPaddingLeft() + getPaddingRight() + mImage.getWidth();// 由图片决定的宽
        switch (specWidthMode) {
            case MeasureSpec.EXACTLY:// match_parent or 指定尺寸
                mWidth = specWidthSize;
                break;
            case MeasureSpec.AT_MOST:// wrap_content
                mWidth = Math.min(desireWidth, specWidthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                //父控件对子控件不加任何束缚，子元素可以得到任意想要的大小，这种MeasureSpec一般是由父控件自身的特性决定的。
                //比如ScrollView，它的子View可以随意设置大小，无论多高，都能滚动显示，这个时候，尺寸就选择自己需要的尺寸size。
            default:
                mWidth = specWidthSize;
                break;
        }

        //正方形，边长以宽为准
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断点形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径

        drawOval(canvas, centre, radius);

        //计算内切正方形的位置
        int relRadius = radius - mCircleWidth / 2;// 获得内圆的半径

        //内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;

        //内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.bottom = (int) (mRect.top + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        //如果图片比较小，那么根据图片的尺寸放置到正中心
        if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
            mRect.right = mRect.left + mImage.getWidth();
            mRect.bottom = mRect.top + mImage.getHeight();
        }

        // 绘图
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     *
     * @param canvas
     * @param centre
     * @param radius
     */
    private void drawOval(Canvas canvas, int centre, int radius) {
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        int mSplitCount = mCount;//分割块的个数，默认为圆形的分割块数，等于mCount

        /**
         * 开始画弧的起始角度位置，0 度为 x 轴正方向，顺时针开始画弧
         * 此处设置为以 y 轴负方向为起点开始顺时针画弧
         */
        int startAngle = mSplitSize / 2 + 90;

        if (mCentralAangle < 360 && mCentralAangle > 0) {//半圆弧
            mSplitCount--;
            startAngle = 270 - mCentralAangle / 2;
        } else {
            mCentralAangle = 360;
        }

        //根据需要画的个数以及间隙计算每个进度块的长度（以圆周长360为基准）
        float itemSize = (mCentralAangle * 1.0f - mSplitCount * mSplitSize) / mCount;

        mPaint.setColor(progressColor); // 设置第一层圆环的颜色
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, startAngle + i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(backgroundColor); // 设置第二层圆环的颜色
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, startAngle + i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    /**
     * 设置圆心角大小
     *
     * @param angle
     */
    public void setCentralAangle(int angle) {
        if (mCentralAangle > 0) {
            mCentralAangle = angle;

            //如果有布局需要发生改变，需要调用requestlayout方法，如果只是刷新动画，则只需要调用invalidate方法。
            invalidate();//调用ondraw()方法，重绘
        }
    }

    /**
     * 上滑，当前数量 +1
     */
    public void up() {
        if (mCurrentCount < mCount) {
            mCurrentCount++;
            postInvalidate();
        }
    }

    /**
     * 下滑，当前数量 -1
     */
    public void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
            postInvalidate();
        }
    }

    private float yDown, yMove, delt;
    private int moveCount;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = event.getY();
                break;
            case MotionEvent.ACTION_MOVE://上下滑动动态更改进度条
                getParent().requestDisallowInterceptTouchEvent(true);//不让父 view 拦截触摸事件

                yMove = event.getY();
                delt = yMove - yDown;
                moveCount = (int) delt / 50;

                if (moveCount > 0) {//向下滑
                    for (int i = 0; i < moveCount; i++) {
                        down();
                    }
                } else if (moveCount < 0) {//向上滑
                    for (int i = 0; i < -moveCount; i++) {
                        up();
                    }
                }

                if (Math.abs(moveCount) > 0) {
                    yDown = yMove;
                }
        }
        return true;
    }
}
