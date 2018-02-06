package com.skj.wheel.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skj.app.MyApplication;

/**
 * Created by 孙科技 on 2017/10/31.
 */

public class ToastUtil {
    private static Toast toast = null;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static int duration = 1000 * 10;

    /**
     * 普通文本消息提示
     *
     * @param text
     */

    public static void TextToast(final CharSequence text) {
        TextToast(MyApplication.mContext, text);
    }

    public static void TextToast(final Context context, final CharSequence text) {
        if (TextUtil.isEmpty(text))
            return;
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        if (myLooper != mainLooper) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 创建一个Toast提示消息
                    toast = Toast.makeText(context, text, duration);
                    // 设置Toast提示消息在屏幕上的位置
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    // 显示消息
                    toast.show();
                }
            });
        } else {
            // 创建一个Toast提示消息
            toast = Toast.makeText(MyApplication.mContext, text, duration);
            // 设置Toast提示消息在屏幕上的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            // 显示消息
            toast.show();
        }

    }


    /**
     * 普通文本消息是否提示
     *
     * @param text
     * @param isShow
     */
    public static void TextToast(final CharSequence text, final boolean isShow) {
        if (TextUtil.isEmpty(text))
            return;
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        if (myLooper != mainLooper) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 创建一个Toast提示消息
                    toast = Toast.makeText(MyApplication.mContext, text, duration);
                    // 设置Toast提示消息在屏幕上的位置
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    // 显示消息
                    if (isShow)
                        toast.show();
                }
            });
        } else {
            // 创建一个Toast提示消息
            toast = Toast.makeText(MyApplication.mContext, text, duration);
            // 设置Toast提示消息在屏幕上的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            // 显示消息
            if (isShow)
                toast.show();
        }

    }

    /**
     * 带图片消息提示
     *
     * @param ImageResourceId
     * @param text
     */
    public static void ImageToast(int ImageResourceId,
                                  CharSequence text) {
        // 创建一个Toast提示消息
        toast = Toast.makeText(MyApplication.mContext, text, Toast.LENGTH_LONG);
        // 设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        // 获取Toast提示消息里原有的View
        View toastView = toast.getView();
        // 创建一个ImageView
        ImageView img = new ImageView(MyApplication.mContext);
        img.setImageResource(ImageResourceId);
        // 创建一个LineLayout容器
        LinearLayout ll = new LinearLayout(MyApplication.mContext);
        // 向LinearLayout中添加ImageView和Toast原有的View
        ll.addView(img);
        ll.addView(toastView);
        // 将LineLayout容器设置为toast的View
        toast.setView(ll);
        // 显示消息
        toast.show();
    }
}
