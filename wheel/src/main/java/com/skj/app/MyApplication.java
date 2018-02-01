package com.skj.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by 孙科技 on 2017/10/30.
 */

public class MyApplication extends Application {

    private static MyApplication app;
    public static Context mContext;

    public synchronized static MyApplication getInstance() {
        if (app == null)
            app = new MyApplication();
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
