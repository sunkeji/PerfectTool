package com.skj.wheel.util;

/**
 * Created by 孙科技 on 2017/10/31.
 */

public class KClickUtil {

    /**
     * 防止过快点击造成多次事件
     */
    private static long lastClickTime;
    private static long setClickTime = (long) (1000 * 1 * 0.5);

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < setClickTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isFastClick(int setTime) {
        setClickTime = 1000 * setTime;
        return isFastClick();
    }

    private static long httpLastClickTime;
    private static long httpSetClickTime = 1000 * 5;

    public synchronized static boolean isHttpFastClick() {
        long time = System.currentTimeMillis();
        if (time - httpLastClickTime < httpSetClickTime) {
            return true;
        }
        httpLastClickTime = time;
        return false;
    }
}
