package com.bubble.customviewstudydemo.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * description: 字符串工具类
 * version:
 *
 * @author bubble
 * @date 2017/5/24
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String s) {
        if (s == null || "".equals(s.trim()) || s.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成双精度浮点数
     */
    public static boolean canParseDouble(String numberStr) {
        try {
            Double.parseDouble(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int getPx(Context context, String px) {
        if (!isNullOrEmpty(px) && canParseDouble(px)) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(px), context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getDip(Context context, String dip) {
        if (!isNullOrEmpty(dip) && canParseDouble(dip)) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(dip), context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getDip(Context context, int dip) {
        if (dip != 0) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getSp(Context context, String sp) {
        if (!isNullOrEmpty(sp) && canParseDouble(sp)) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sp), context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getSp(Context context, int sp) {
        if (sp != 0) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getInt(String num) {
        if (!isNullOrEmpty(num) && canParseDouble(num)) {
            return Integer.parseInt(num);
        } else {
            return 0;
        }
    }

    /**
     * 获取百分比字符串
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 百分比字符串
     */
    public static String getPercent(int numerator, int denominator) {
        int percent = Math.round(numerator * 100 / denominator);

        return percent + "%";
    }
}
