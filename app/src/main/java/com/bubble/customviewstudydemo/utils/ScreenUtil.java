package com.bubble.customviewstudydemo.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * description: 屏幕工具类
 * version:
 *
 * @author bubble
 * @date 2017/5/10
 */
public class ScreenUtil {
    public static int screenWidth = 0;
    public static int screenHeight = 0;

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void getScreenSize(Context context) {
        if (screenWidth == 0 || screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            getScreenSize(context);
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            getScreenSize(context);
        }
        return screenHeight;
    }
}
