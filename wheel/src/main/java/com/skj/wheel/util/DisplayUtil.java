package com.skj.wheel.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.skj.app.MyApplication;


/**
 * Created by 孙科技 on 2017/4/26.
 */

public class DisplayUtil {
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static float dip2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) (((double) displayMetrics.density + 0.5D) * (double) dp);
    }

    public static float px2dip(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (float) ((double) px / ((double) displayMetrics.density + 0.5D));
    }

    /**
     * 获取屏幕宽高
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = MyApplication.mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm = MyApplication.mContext.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = MyApplication.mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = MyApplication.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = MyApplication.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置字体
     */
    public static void setDefault() {
        Resources res = MyApplication.mContext.getResources();
        Configuration config1 = new Configuration();
        config1.setToDefaults();
        res.updateConfiguration(config1, res.getDisplayMetrics());
    }


}
