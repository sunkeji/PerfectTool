package com.skj.wheel.definedview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.skj.wheel.R;
import com.skj.wheel.util.KToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 孙科技 on 2017/5/15.
 */

@SuppressLint("AppCompatCustomView")
public class MyETView extends EditText implements View.OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable = null;
    private Context mContext;

    public MyETView(Context context) {
        this(context, null);
    }

    public MyETView(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public MyETView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initInputKey();
        // 设置键盘按钮事件
//        setOnEditorActionListener(new OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                // 搜索键
//                if (actionId == EditorInfo.IME_ACTION_NEXT) {
//                    // 隐藏软件盘
//                    hideKeyBody();
//                    if (mOnSearcherClickListener != null) {
//                        mOnSearcherClickListener.onSeacherClick(getText().toString().trim());
//                    }
//                }
//                return false;
//            }
//        });
    }

    private void init() {
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setFilters(new InputFilter[]{inputFilter});
        this.setMaxLines(1);
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
        mClearDrawable = getResources().getDrawable(R.mipmap.icon_del);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        this.setClearIconVisible(false);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setClearIconVisible(s.length() > 0);
    }

    InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_.-]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                KToastUtil.TextToast(mContext, "不允许输入特殊符号！");
                return "";
            }
        }

    };

    private int maxContent = 1000;

    public void setMaxLength(final int maxContent) {
        this.maxContent = maxContent;
        InputFilter inputFilter1 = new InputFilter.LengthFilter(1000) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dest.length() > maxContent) {
                    KToastUtil.TextToast(mContext, "字数超出限制！");
                    return "";
                }
                return super.filter(source, start, end, dest, dstart, dend);
            }
        };
        setFilters(new InputFilter[]{inputFilter1});
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        if (mClearDrawable == null)
            return;
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public Drawable getClearDrawable() {
        return mClearDrawable;
    }

    public void setClearDrawable(Drawable mClearDrawable) {
        this.mClearDrawable = mClearDrawable;
    }


    private OnSeacherClickListener mOnSearcherClickListener;

    protected void hideKeyBody() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    private void initInputKey() {
        setSingleLine(true);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    }

    public void setOnSeacherClickListener(OnSeacherClickListener onSearcherClickListener) {
        this.mOnSearcherClickListener = onSearcherClickListener;
    }

    public interface OnSeacherClickListener {

        /**
         * 搜索点击监听
         */
        void onSeacherClick(String content);
    }
}
