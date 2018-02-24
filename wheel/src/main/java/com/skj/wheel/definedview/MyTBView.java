package com.skj.wheel.definedview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;


import com.skj.wheel.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 孙科技 on 2017/5/4.
 */

@SuppressLint("AppCompatCustomView")
public class MyTBView extends Button {
    private long length = 60 * 1000;// 倒计时长度，默认一分钟
    private String textAfter = "s";
    private String textBefore = "重新发送";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private Timer timer;
    private TimerTask timerTask;
    private long time;
    private Context context;

    public MyTBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 设置计时时候显示的文本
     *
     * @param text
     * @return
     */
    public MyTBView setTextAfter(String text) {
        this.textAfter = text;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public MyTBView setTextBefore(String text) {
        this.textBefore = text;
        this.setText(textBefore);
        return this;
    }

    /**
     * 设置到计时长度,时间 默认毫秒
     *
     * @return
     */
    public MyTBView setLenght(long length) {
        this.length = length;
        return this;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    MyTBView.this.setText("重新发送" + "(" + time / 1000 + ")");
                    time -= 1000;
                    if (time < 0) {
                        MyTBView.this.setEnabled(true);
                        MyTBView.this.setText(textBefore);
                        clearTimer();
                    }
                    break;
                case 0x02:
                    MyTBView.this.setEnabled(true);
                    MyTBView.this.setText(textBefore);
                    clearTimer();
                    break;
            }

        }
    };

    /**
     * 启动TimerTask,Timer
     */
    private void initTimer(final int what) {
        time = length;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(what);
            }
        };
    }

    /**
     * 注销TimerTask,Timer
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始
     */
    public void start() {
        ToastUtil.TextToast("正在发送，请稍候...");
        initTimer(0x01);
        this.setText("重新发送" + "(" + time / 1000 + ")");
        this.setEnabled(false);
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 停止
     */
    public void stop() {
        handler.sendEmptyMessage(0x02);
        MyTBView.this.setText(textBefore);
    }
}

