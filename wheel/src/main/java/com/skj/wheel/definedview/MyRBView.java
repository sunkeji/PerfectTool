package com.skj.wheel.definedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skj.wheel.R;


/**
 * Created by 孙科技 on 2017/5/23.
 */

public class MyRBView extends RelativeLayout implements Checkable {

    private ImageView imgLogo;
    private TextView textTitle;
    private TextView textSign;
    private RadioButton rbIcon;

    private boolean mChecked;    ////状态是否选中
    private boolean mBroadcasting;
    private int id;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public MyRBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_default_rb, this);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        textTitle = (TextView) findViewById(R.id.text_title);
        textSign = (TextView) findViewById(R.id.text_sign);
        rbIcon = (RadioButton) findViewById(R.id.rb_icon);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyRBView);

        Drawable d = array.getDrawable(R.styleable.MyRBView_rb_radio);
        if (d != null) {
            rbIcon.setButtonDrawable(d);
        }

        String title = array.getString(R.styleable.MyRBView_rb_title);
        if (title != null) {
            setTextTitle(title);
        }

        int str = array.getInt(R.styleable.MyRBView_rb_sign, 0);
        if (str != 1) {
            setTextDesc(0);
        }
        Drawable logo = array.getDrawable(R.styleable.MyRBView_rb_logo);
        if (logo != null) {
            setDrawableLogo(logo);
        }

        boolean checked = array.getBoolean(R.styleable.MyRBView_rb_checked, false);
        rbIcon.setChecked(checked);

        array.recycle();
        setClickable(true);

        id = getId();
    }


    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        if (mChecked != checked) {
            mChecked = checked;
            rbIcon.refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
            mBroadcasting = false;
        }
        rbIcon.setChecked(checked);
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }

    @Override
    public boolean performClick() {
        // TODO Auto-generated method stub
        /*
         * XXX: These are tiny, need some surrounding 'expanded touch area',
         * which will need to be implemented in Button if we only override
         * performClick()
         */

        /* When clicked, toggle the state */
        toggle();
        return super.performClick();
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(MyRBView buttonView, boolean isChecked);
    }

    public void setTextDesc(int s) {
        if (s != 1) {
            textSign.setVisibility(GONE);
        }
    }

    public void setTextTitle(String s) {
        if (s != null) {
            textTitle.setText(s);
        }
    }

    public String getTextTitle() {
        String s = textTitle.getText().toString();
        return s == null ? "" : s;
    }

    public void setDrawableLogo(Drawable d) {
        if (d != null) {
            imgLogo.setImageDrawable(d);
        }
    }

    public void setChangeImg(int checkedId) {
        if (checkedId == id) {
            rbIcon.setChecked(true);
        } else {
            rbIcon.setChecked(false);
        }
    }
}
