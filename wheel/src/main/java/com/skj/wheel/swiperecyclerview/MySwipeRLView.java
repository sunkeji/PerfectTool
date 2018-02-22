package com.skj.wheel.swiperecyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.skj.wheel.R;


/**
 * Created by 孙科技 on 2017/11/1.
 */

public class MySwipeRLView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    private Handler mHandler = new Handler();//UI线程
    private int colorTheme = R.color.main_color;

    private OnSwipeListener onSwipeListener;//定义下拉刷新对象

    public interface OnSwipeListener {

        void onRefresh();
    }

    public MySwipeRLView(Context context) {
        this(context, null);
    }

    public MySwipeRLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 自定义swipeRefreshView(自动刷新)
     */
    private void init() {
        this.setColorSchemeResources(colorTheme);
        this.setOnRefreshListener(this);
        this.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * 自定义刷新主题颜色
     *
     * @return
     */
    public int getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
        this.setColorSchemeResources(colorTheme);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
                onSwipeListener.onRefresh();
            }
        }, 500);
    }

    /**
     * 下拉刷新监听事件
     *
     * @param onSwipeListener
     */
    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

}
