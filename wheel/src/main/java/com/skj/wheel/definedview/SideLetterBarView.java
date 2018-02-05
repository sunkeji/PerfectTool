package com.skj.wheel.definedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.skj.wheel.R;

import java.util.Set;
import java.util.TreeMap;

public class SideLetterBarView extends View {
    private String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;
    private Paint paint = new Paint();
    private boolean showBg = false;//字母栏的背景色
    private OnLetterChangedListener onLetterChangedListener;//字母选择事件
    private TextView overlay;//选择的字母悬浮显示

    private int letter_TextColor;
    private int letter_TextColor_select;
    private float letter_TextSize;

    public SideLetterBarView(Context context) {
        this(context, null);
    }

    public SideLetterBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.style.SideLetterBar);
    }

    public SideLetterBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SideLetterBarView, defStyleAttr, R.style.SideLetterBar);

        letter_TextColor = a.getColor(R.styleable.SideLetterBarView_letter_text_color, 0);
        letter_TextColor_select = a.getColor(R.styleable.SideLetterBarView_letter_text_selectcolor, 0);
        letter_TextSize = a.getDimension(R.styleable.SideLetterBarView_letter_text_size, 0);
    }


    /**
     * 设置悬浮的textview
     *
     * @param overlay
     */
    public void setOverlay(TextView overlay) {
        this.overlay = overlay;
    }

    /**
     * 设置列表中有的字母数量，默认26字母全部
     *
     * @param map
     */
    public void setLetterList(TreeMap<String, Object> map) {
        // 将Map 的键转化为Set
        Set<String> mapKeySet = map.keySet();
        b = new String[mapKeySet.size()];
        mapKeySet.toArray(b);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBg) {
            canvas.drawColor(Color.TRANSPARENT);
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setTextSize(letter_TextSize);
            paint.setColor(letter_TextColor);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(letter_TextColor_select);
                paint.setFakeBoldText(true);  //加粗
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnLetterChangedListener listener = onLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBg = false;
                choose = -1;
                invalidate();
                if (overlay != null) {
                    overlay.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter);
    }

}
