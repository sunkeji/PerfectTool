package com.wheel.perfect.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.skj.wheel.definedview.selector.WheelView;
import com.skj.wheel.definedview.selector.adapters.ArrayWheelAdapter;
import com.skj.wheel.util.ToastUtil;
import com.wheel.perfect.R;

import java.util.ArrayList;

/**
 * Created by 孙科技 on 2018/2/5.
 * 滑轮联动单列选择器
 */

public class SelectorIntervalView {

    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick {
        void onClick(View view, int postion1, int postion2);
    }

    /**
     * 弹出底部滚轮选择
     *
     * @param context
     * @param list    ArrayList<?>
     * @param click   OnWheelViewClick
     */
    public static void alertBottomWheelOption(Context context, ArrayList<?> list, ArrayList<?> list1,
                                              final OnWheelViewClick click) {

        final PopupWindow popupWindow = new PopupWindow();

        View view = LayoutInflater.from(context).inflate(R.layout.selector_interval, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.btnSubmit);
        final WheelView wv_option = (WheelView) view.findViewById(R.id.wv_option);
        final WheelView wv_option1 = (WheelView) view.findViewById(R.id.wv_option1);
        final WheelView wv_option2 = (WheelView) view.findViewById(R.id.wv_option2);
        wv_option.setAdapter(new ArrayWheelAdapter(list1));
        wv_option.setCyclic(false);
        wv_option.setTextSize(16);
        wv_option1.setAdapter(new ArrayWheelAdapter(list));
        wv_option1.setCyclic(true);
        wv_option1.setTextSize(16);
        wv_option2.setAdapter(new ArrayWheelAdapter(list1));
        wv_option2.setCyclic(false);
        wv_option2.setTextSize(16);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (wv_option2.getCurrentItem() > wv_option.getCurrentItem())
                    click.onClick(view, wv_option.getCurrentItem(), wv_option2.getCurrentItem());
                else
                    ToastUtil.TextToast("后边的值要大于前者");
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int top = view.findViewById(R.id.ll_container).getTop();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int y = (int) motionEvent.getY();
                    if (y < top) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0),
                Gravity.CENTER, 0, 0);
    }


}
