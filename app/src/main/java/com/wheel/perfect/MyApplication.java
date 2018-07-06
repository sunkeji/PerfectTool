package com.wheel.perfect;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * ClassName:	MyApplication
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/27 10:02
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class MyApplication extends Application {
    private static MyApplication app;

    public static MyApplication getInstance() {
        if (app == null)
            app = new MyApplication();
        return app;
    }

    public static Context mContext;
    public static ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void copy(String copy) {
        //第一个参数，是描述复制的内容，也可以和内容一样。
        ClipData clipData = ClipData.newPlainText("copy from demo", copy);
        mClipboardManager.setPrimaryClip(clipData);
    }

    public String duplicate() {
        // 粘贴板有数据，并且是文本
        if (mClipboardManager.hasPrimaryClip()
                && mClipboardManager.getPrimaryClipDescription().hasMimeType
                (ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = mClipboardManager.getPrimaryClip().getItemAt(0);
            CharSequence text = item.getText();
            if (text == null) {
                return "";
            }
            return text.toString();
        }
        return "";
    }
}
