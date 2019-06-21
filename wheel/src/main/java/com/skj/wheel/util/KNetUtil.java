package com.skj.wheel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ClassName:	KNetUtil
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/27 10:56
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class KNetUtil {
    public static KNetUtil KNetUtil;
    public ConnectivityManager connectivity;
    public NetworkInfo info;

    public static KNetUtil getInstance(Context context) {
        synchronized (KNetUtil.class) {
            if (KNetUtil == null) {
                KNetUtil = new KNetUtil(context);
            }
        }
        return KNetUtil;
    }

    public KNetUtil(Context context) {
        connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
            info = connectivity.getActiveNetworkInfo();

    }

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public boolean isNetworkAvailable() {
        if (info != null && info.isConnected()) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是WiFi
     *
     * @return
     */
    public boolean isWIFI() {
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否移动网络
     *
     * @return
     */
    public boolean isMobile() {
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

}
