package com.example.utils;

public class FastClickYUtil {
    // 两次点击间隔不能少于200ms
    private static final int FAST_CLICK_DELAY_TIME = 400;
    private static final int FAST_CLICK_DELAY_TIME_H5 = 700;

    private static long lastClickTime;
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static boolean isFastClickH5() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME_H5) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
