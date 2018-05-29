package com.skj.wheel.definedview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.skj.wheel.R;

/**
 * Created by 孙科技 on 2018/4/10.
 */
public class MyTSView extends TextSwitcher {

    private String[] mAdvertisements;//滚动文字
    private final int HOME_AD_RESULT = 1;
    private int time = 1000;//滚动间隔
    private int mSwitcherCount = 0;

    public MyTSView(Context context) {
        this(context, null);
    }

    public MyTSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 自定义属性
     *
     * @param context
     */
    private void initView(final Context context) {
        this.setFactory(new ViewFactory() {
            // 这里用来创建内部的视图，这里创建TextView，用来显示文字
            public View makeView() {
                TextView tv = new TextView(context);
                // 设置文字的显示单位以及文字的大小
                // tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources()
                //        .getDimension(R.dimen.font_size));
                tv.setTextColor(getResources().getColor(R.color.black));
                return tv;
            }
        });
        this.setInAnimation(context,
                R.anim.enter_bottom);
        this.setOutAnimation(context, R.anim.leave_top);
    }

    /**
     * 自定义文字
     *
     * @param mAdvertisements
     */
    public void setmAdvertisements(String[] mAdvertisements) {
        this.mAdvertisements = mAdvertisements;
        mHandler.sendEmptyMessage(HOME_AD_RESULT);
    }

    /**
     * 设置时间间隔
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * UI线程更新
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 广告
                case HOME_AD_RESULT:
                    setText(mAdvertisements[mSwitcherCount % mAdvertisements.length]);
                    mSwitcherCount++;
                    mHandler.sendEmptyMessageDelayed(HOME_AD_RESULT, time);
                    break;
            }

        }
    };

}
