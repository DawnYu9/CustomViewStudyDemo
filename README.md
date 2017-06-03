> 学习鸿洋大神的博客专辑[【Android 自定义控件之起步】](http://blog.csdn.net/lmj623565791/article/category/2680591)后所做的demo。

> 欢迎讨论、建议和指正错误 (^_^)

## 优化部分

### [Android 自定义View (三) 圆环交替 等待效果](http://blog.csdn.net/lmj623565791/article/details/24500107)
> code: [CustomProgressBar.java](/app/src/main/java/com/bubble/customviewstudydemo/view/CustomProgressBar.java)

1. 优化颜色交换逻辑，使用中间变量`tempColor`
2. 线程增加`stopThread`停止标识判断，离开页面时关闭线程

    `View`中
    ``` java
    private boolean stopThread = false;
    
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
    
    /**
     * 关闭线程
     */
    public void stopThread(boolean stop) {
        stopThread = stop;
    }
    ```       

    `Activity`中
    ```
    @Override
    protected void onDestroy() {
        super.onDestroy();
        customProgressBar.stopThread(true);
    }
    ```

3. 重写`onMeasure()`方法，支持`wrap_content`，并设定控件为正方形

    ``` java
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
    ```

4. 修改`onDraw()`绘制方式，将背景条的画圆改为动态画弧，优化画弧有锯齿的问题
5. 控件中心增加百分比文字动态更新进度

    ``` java
    @Override
    protected void onDraw(Canvas canvas) {
        ...

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
    ```

### [Android 自定义View (四) 视频音量调控](http://blog.csdn.net/lmj623565791/article/details/24529807)
> code: [CustomVolumControlBar.java](/app/src/main/java/com/bubble/customviewstudydemo/view/CustomVolumControlBar.java)

1. 重写`onMeasure()`方法，支持`wrap_content`，并设定控件为正方形(见上)
2. 优化音量加减操作，支持上下滑动手势连续调节音量大小

    ``` java
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
    ```

3. 增加对音量加减的端点判断

    ``` java
    public void up() {
        if (mCurrentCount < mCount) {
            mCurrentCount++;
            postInvalidate();
        }
    }

    public void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
            postInvalidate();
        }
    }
    ```

4. 增加圆心角属性，可以随意调节为圆环或圆弧

    ``` java
    /**
     * 圆心角度数
     */
    private int mCentralAangle;
    
    /**
     * 根据参数画出每个进度块
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

        mPaint.setColor(progressColor); // 画进度条
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, startAngle + i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(backgroundColor); // 画进度条背景色
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, startAngle + i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }
    ```

## 预览
![main.png](/preview/pic/main.png)
![CustomImg.png](/preview/pic/CustomImg.png)
![CustomImgContainer.png](/preview/pic/CustomImgContainer.png)
![CustomProgressBar.gif](/preview/gif/CustomProgressBar.gif)
![CustomRandomTextView.gif](/preview/gif/CustomRandomTextView.gif)
![CustomVolumControlBar.gif](/preview/gif/CustomVolumControlBar.gif)

## 参考&amp;致谢
[【Android 自定义控件之起步】](http://blog.csdn.net/lmj623565791/article/category/2680591)

[给AppCompatActivity的标题栏上加上返回按钮](http://www.jianshu.com/p/3600b2178afa)

[Android实现正方形View](http://blog.csdn.net/qjay_dev/article/details/46852859)

[[Android] (在ScrollView里嵌套view)重叠view里面的onTouchEvent的调用方法](http://www.cnblogs.com/rossoneri/p/3994662.html)

[Android自定义View：MeasureSpec的真正意义与View大小控制](https://segmentfault.com/a/1190000007948959)

[Android自定义view详解](https://shaohui.me/2016/07/08/Android%E8%87%AA%E5%AE%9A%E4%B9%89view%E8%AF%A6%E8%A7%A3/)

> 感谢以上作者以及部分评论的作者给予的思路和帮助 (^_^)

