package com.wheel.perfect.activity;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skj.wheel.definedview.MyTSView;
import com.skj.wheel.util.LogUtil;
import com.wheel.perfect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙科技 on 2018/5/24.
 */
public class EditTestActivity extends AppCompatActivity {
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.edit1)
    EditText edit1;
    @BindView(R.id.ts_view)
    MyTSView tsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);
        ButterKnife.bind(this);
//        AndroidAdjustResizeBugFix.assistActivity(this);
//        mSearchLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        mSearchLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mSearchLayout.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = mSearchLayout.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight <= 100) {
                    //软键盘隐藏啦
                    edit.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.i(false + "-----------");
                        }
                    }, 100);
                } else {
                    ////软键盘弹出啦
//                    edit.setVisibility(View.GONE);
                    LogUtil.i(true + "-----------");
                }
            }
        });
        String[] s = new String[]{"ceshi", "cddjdjjd"};
        tsView.setmAdvertisements(s);

    }

    private boolean mBackEnable = false;
    private boolean mIsBtnBack = false;
    private int rootBottom = Integer.MIN_VALUE;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            mSearchLayout.getGlobalVisibleRect(r);
            // 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
            if (rootBottom == Integer.MIN_VALUE) {
                rootBottom = r.bottom;
                return;
            }
            // adjustResize，软键盘弹出后高度会变小
            if (r.bottom < rootBottom) {
                mBackEnable = false;
            } else {
                mBackEnable = true;
                if (mIsBtnBack) {
                    finish();
                }
            }


        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchLayout.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        } else {
            mSearchLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
        super.onDestroy();
    }
}
