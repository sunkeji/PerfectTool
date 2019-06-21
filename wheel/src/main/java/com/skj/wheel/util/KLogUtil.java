package com.skj.wheel.util;

import android.util.Log;

/**
 * Created by 孙科技 on 2017/10/31.
 */

public class KLogUtil {

    /**
     * 是否开启debug
     */
    private static final String Prefix = "log--";

    public static void e(String msg) {
        e("result:", msg + "", true);
    }

    public static void e(String msg, boolean isDebug) {
        e("result:", msg + "", isDebug);
    }

    public static void e(String tag, String msg) {
        e(tag, msg + "", true);
    }

    public static void e(String tag, String msg, boolean isDebug) {
        if (isDebug) {
            Log.e(Prefix + tag, msg + "");
        }
    }

    public static void i(String msg) {
        i("result:", msg + "", true);
    }

    public static void i(String msg, boolean isDebug) {
        i("result:", msg + "", isDebug);
    }

    public static void i(String tag, String msg) {
        i(tag, msg + "", true);
    }

    public static void i(String tag, String msg, boolean isDebug) {
        if (isDebug && msg != null) {
            while (msg.length() > 0) {
                if (msg.length() > 1024) {
                    Log.i(Prefix + tag, msg.substring(0, 1024) + "");
                    msg = msg.substring(1024);
                } else {
                    Log.i(Prefix + tag, msg + "");
                    msg = "";
                }
            }

        }
    }

    public static void d(String msg) {
        d("result:", msg + "", true);
    }

    public static void d(String msg, boolean isDebug) {
        d("result:", msg + "", isDebug);
    }

    public static void d(String tag, String msg) {
        d(tag, msg + "", true);
    }

    public static void d(String tag, String msg, boolean isDebug) {
        if (isDebug) {
            Log.d(Prefix + tag, msg + "");
        }
    }
}
