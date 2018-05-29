package com.wheel.perfect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by 孙科技 on 2018/4/9.
 */
public class AutoTextView extends TextSwitcher implements
        ViewSwitcher.ViewFactory {

    private float mHeight = 20;
    private Context mContext;
    //mInUp,mOutUp,上进上出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    //mInDown,mOutDown，下进下出动画
    private Rotate3dAnimation mInDown;
    private Rotate3dAnimation mOutDown;
    private TextView t;


    public AutoTextView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.auto3d);
//        mHeight = a.getDimension(R.styleable.auto3d_textSize, 24);
//        a.recycle();
        mContext = context;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        setFactory(this);
        mInUp = createAnim(-90, 0, true, true);
        mOutUp = createAnim(0, 90, false, true);
        mInDown = createAnim(90, 0, true, false);
        mOutDown = createAnim(0, -90, false, false);
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }

    //创建动画
    private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    //初始化textView属性
    @Override
    public View makeView() {
        // TODO Auto-generated method stub
        t = new TextView(mContext);
        //t.setGravity(Gravity.CENTER);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTextSize(mHeight);
        t.setMaxLines(3);
        //t.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        return t;
    }

    //重写setText方法，动态设置字体颜色
    @Override
    public void setText(CharSequence text) {
        super.setText(text);
    }

    public void setText(CharSequence text, int color) {
        super.setText(text);
        final TextView t = (TextView) getNextView();
        t.setText(text);
        t.setTextColor(color);
        showNext();
    }

    //开始下进下出动画
    public void previous() {
        if (getInAnimation() != mInDown) {
            setInAnimation(mInDown);
        }
        if (getOutAnimation() != mOutDown) {
            setOutAnimation(mOutDown);
        }
    }

    //开始上进上出动画
    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    //初始化动画属性
    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
