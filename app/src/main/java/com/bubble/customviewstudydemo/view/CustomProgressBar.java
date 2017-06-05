package com.bubble.customviewstudydemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.bubble.customviewstudydemo.R;
import com.bubble.customviewstudydemo.utils.StringUtils;

/**
 * description: 圆环进度条
 * version:
 * blog: http://blog.csdn.net/lmj623565791/article/details/24500107
 *
 * @author bubble
 * @date 2017/5/10
 */
public class CustomProgressBar extends CustomBaseView {
    /**
     * 进度条颜色
     */
    private int progressColor;
    /**
     * 底层颜色
     */
    private int backgroundColor;

    /**
     * 进度条宽度
     */
    private int mProgressWidth;

    /**
     * 百分比文字大小
     */
    private int mPercentTextSize;

    /**
     * 百分比文字颜色
     */
    private int mPercentTextColor;

    /**
     * 画笔
     */
    private Paint mPaint;//进度条
    private RectF oval;
    private Paint mTextPaint;//百分比文字
    private Rect textBound;

    /**
     * 当前进度
     */
    private int mProgress;

    private boolean stopThread = false;

    /**
     * 速度
     */
    private int mSpeed;

    private static final int DEFAULT_SIZE = 150;//view 默认边长（dp）
    private static final int DEFAULT_SPEED = 20;//默认速度


    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet set) {
        this(context, set, 0);
    }

    public CustomProgressBar(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);

        init(set, defStyle, R.styleable.CustomProgressBar);
    }

    @Override
    public void init(AttributeSet set, int defStyleAttr, int[] attrs) {
        super.init(set, defStyleAttr, attrs);

        oval = new RectF();
        mPaint = new Paint();
        mPaint.setStrokeWidth(mProgressWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        textBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);//消除锯齿
        mTextPaint.setTextSize(mPercentTextSize);
        mTextPaint.setColor(mPercentTextColor);

        initSpeed();
    }

    private void initSpeed() {
        // 绘图线程
        new Thread() {
            public void run() {
                while (!stopThread) {
                    if (mProgress == 360) {//进度条跑完一圈后交换颜色
                        mProgress = 0;
                        int tempColor = progressColor;
                        progressColor = backgroundColor;
                        backgroundColor = tempColor;
                    }
                    mProgress++;//加 1 操作放在 mProgress == 360 后面，使百分比可以显示 100% ，再从 0% 开始
                    postInvalidate();//更新

                    //速度
                    try {
                        if (mSpeed > 0) {
                            Thread.sleep(500 / mSpeed);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void getAttrs(AttributeSet set, int defStyleAttr, int[] attrs) {
        super.getAttrs(set, defStyleAttr, attrs);

        progressColor = typedArray.getColor(R.styleable.CustomProgressBar_progressColor, Color.GREEN);

        backgroundColor = typedArray.getColor(R.styleable.CustomProgressBar_backgroundColor, Color.RED);

        mProgressWidth = typedArray.getDimensionPixelSize(R.styleable.CustomProgressBar_circleWidth, StringUtils.getDip(mContext, 20));

        mSpeed = typedArray.getInt(R.styleable.CustomProgressBar_speed, DEFAULT_SPEED);

        mPercentTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomProgressBar_textSize, StringUtils.getSp(mContext, 14));

        mPercentTextColor = typedArray.getColor(R.styleable.CustomProgressBar_textColor, Color.BLACK);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth;

        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int desireWidth = getPaddingLeft() + getPaddingRight() + StringUtils.getDip(mContext, DEFAULT_SIZE);
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
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mProgressWidth / 2 - getPaddingLeft();// 半径：圆心到圆环中心的距离

        //开始画弧
        // 用于定义的圆弧的形状和大小的界限
        oval.set(centre - radius, centre - radius, centre + radius, centre + radius);

        //画笔在圆环的中心线上
        //画进度条
        mPaint.setColor(progressColor); // 设置进度条的颜色
        //canvas.drawCircle(centre, centre, radius, mPaint); // 画圆环
        canvas.drawArc(oval, -90, mProgress, false, mPaint);//画弧

        //画背景条
        mPaint.setColor(backgroundColor); // 设置背景条的颜色
        //canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        canvas.drawArc(oval, mProgress - 90, 360 - mProgress, false, mPaint);//画弧

        //更新百分比数字
        String mPercentText = StringUtils.getPercent(mProgress, 360);
        mTextPaint.getTextBounds(mPercentText, 0, mPercentText.length(), textBound);
        canvas.drawText(mPercentText, getWidth() / 2 - textBound.width() / 2, getHeight() / 2 + textBound.height() / 2, mTextPaint);
    }

    /**
     * 设置速度
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    /**
     * 关闭线程
     *
     * @param stop
     */
    public void stopThread(boolean stop) {
        stopThread = stop;
    }
}
