package com.example.utils;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
/**
 * 尺寸工具类，包括屏幕大小，单位转换
 * Created by hook on 2016/3/21 0021.
 */
public class SizeUtil {
    private static Resources res = MyApplication.getInstance().getResources();

    public static int dip2Px(int dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
    }

    public static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, res.getDisplayMetrics());
    }

    public static float px2dip(float px) {
        final float scale = res.getDisplayMetrics().density;
        return (px / scale);
    }

    public static float px2sp(float px) {
        return (px / res.getDisplayMetrics().scaledDensity);
    }

    public static float getScale() {
        return res.getDisplayMetrics().density;
    }

    public static int getScreenWidth() {
        return res.getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return res.getDisplayMetrics().heightPixels;
    }

    public static int getSize(int sizeId) {
        return res.getDimensionPixelSize(sizeId);
    }

    public static void setViewSize(View v, int width, int height) {
        ViewGroup.LayoutParams mParams = v.getLayoutParams();
        if (width != 0) {
            mParams.width = width;
        }
        if (height != 0) {
            mParams.height = height;
        }
        v.setLayoutParams(mParams);
    }

    public static int getStatusBarSize() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = res.getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
