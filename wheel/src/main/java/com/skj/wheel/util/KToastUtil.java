package com.skj.wheel.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by 孙科技 on 2017/10/31.
 */

public class KToastUtil {
    private static Toast toast = null;
    public static int duration = 1000 * 10;

    /**
     * 普通文本消息提示
     *
     * @param text
     */
    public static void TextToast(final Context context, final CharSequence text) {
        if (KTextUtil.isEmpty(text))
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
            toast = Toast.makeText(context, text, duration);
            // 设置Toast提示消息在屏幕上的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            // 显示消息
            toast.show();
        }

    }


    /**
     * 带图片消息提示
     *
     * @param ImageResourceId
     * @param text
     */
    public static void ImageToast(Context context, int ImageResourceId,
                                  CharSequence text) {
        ImageToast(context, ImageResourceId, text, 1);
    }

    public static void ImageToast(Context context, int ImageResourceId,
                                  CharSequence text, int orientation) {
        // 创建一个Toast提示消息
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);

        // 设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        // 获取Toast提示消息里原有的View
        LinearLayout toastView = (LinearLayout) toast.getView();
        // 创建一个ImageView
        ImageView img = new ImageView(context);
        img.setImageResource(ImageResourceId);
        img.setPadding(0, 20, 0, 20);
        // 创建一个LineLayout容器
        // 向LinearLayout中添加ImageView和Toast原有的View
        toastView.setOrientation(orientation);
        toastView.addView(img, 0);
        // 将LineLayout容器设置为toast的View
        // 显示消息
        toast.show();
    }
}
