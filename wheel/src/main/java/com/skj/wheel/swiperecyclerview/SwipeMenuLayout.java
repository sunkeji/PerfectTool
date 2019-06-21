package com.skj.wheel.swiperecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.skj.wheel.R;
import com.skj.wheel.util.KLogUtil;

/**
 * Created by 孙科技 on 2018/2/11.
 */

public class SwipeMenuLayout extends ViewGroup {

    private static final String TAG = "SwipeMenuLayout";

    private Scroller mScroller;

    private int mScaledTouchSlop;
    /**
     * 向右滑动显示左边菜单
     */
    private int leftMenuId;

    private View leftMenuView;
    /**
     * 向左滑动显示右边菜单
     */
    private int rightMenuId;

    private View rightMenuView;
    /**
     * 中间自定义内容
     */
    private int contentId;

    private View contentView;

    // 静态类写入内存共享。用来判断当前界面是否有menu打开
    private static SwipeMenuLayout swipeMenuLayout;

    private boolean isSwipeing;

    private static State curState;

    private static boolean isTouching = false;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);

        // 1.创建 Scroller 对象
        mScroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.p_SwipeMenuLayout);
        try {
            leftMenuId = typedArray.getResourceId(R.styleable.p_SwipeMenuLayout_leftMenuId, 0);
            rightMenuId = typedArray.getResourceId(R.styleable.p_SwipeMenuLayout_rightMenuId, 0);
            contentId = typedArray.getResourceId(R.styleable.p_SwipeMenuLayout_contentId, 0);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 测量方法可能会被调用多次
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);
        int viewHeight = 0;
        int viewWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            viewHeight = Math.max(viewHeight, childView.getMeasuredHeight());
            KLogUtil.d(TAG, "onMeasure: getMeasureWidth() = " + i + "," + +childView.getMeasuredWidth());
            viewWidth += childView.getMeasuredWidth();
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        KLogUtil.d(TAG, "onLayout: l = " + l + ",t = " + t + ",r = " + r + ",b = " + b);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (leftMenuView == null && childView.getId() == leftMenuId) {
                leftMenuView = childView;
                continue;
            }
            if (rightMenuView == null && childView.getId() == rightMenuId) {
                rightMenuView = childView;
                continue;
            }
            if (contentView == null && childView.getId() == contentId) {
                contentView = childView;
            }
        }

        // 布局 leftMenu
        if (leftMenuView != null) {
            KLogUtil.d(TAG, "onLayout: leftMenuView.getMeasureWidth() = " + leftMenuView.getMeasuredWidth());
            leftMenuView.layout(-leftMenuView.getMeasuredWidth(), t, 0, b);
        }
        // 布局 contentView
        if (contentView != null) {
            KLogUtil.d(TAG, "onLayout: contentView.getMeasureWidth() = " + contentView.getMeasuredWidth());
            contentView.layout(0, t, contentView.getMeasuredWidth(), b);
        }
        // 布局 rightMenu
        if (rightMenuView != null) {
            KLogUtil.d(TAG, "onLayout: rightMenuView.getMeasureWidth() = " + rightMenuView.getMeasuredWidth());
            rightMenuView.layout(contentView.getMeasuredWidth(), t,
                    contentView.getMeasuredWidth() + rightMenuView.getMeasuredWidth(), b);
        }

    }

    // 2. 重写 computeScroll()
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {  // 动画没有结束
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            postInvalidate();
        }
    }

    private PointF lastPoint;

    // 记录第一次触摸点的坐标，方便计算手指抬起时，总的滑动距离
    private PointF firstPoint;

    // 手指按下到抬起，总的滑动距离
    float finalDistance;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouching) {
                    return false;
                }
                isTouching = true;
                isSwipeing = false;
                if (firstPoint == null) {
                    firstPoint = new PointF();
                }
                if (lastPoint == null) {
                    lastPoint = new PointF();
                }
                // 当前触摸的不是已经打开的那个 SwipeMenuLayout,则需要将打开的那个关闭掉。
                if (swipeMenuLayout != null) {
                    if (swipeMenuLayout != this) {
                        // 调用已经打开的那个 SwipeMenuLayout 关闭方法
                        swipeMenuLayout.handleSwipeMenu(State.CLOSE);
//                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }
                firstPoint.set(ev.getX(), ev.getY());
                lastPoint.set(ev.getX(), ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                // 偏移量 = 当前坐标值 - 上次坐标值
                int dx = (int) (ev.getX() - lastPoint.x);
                int dy = (int) (ev.getY() - lastPoint.y);
                // scrollBy 移动，正值内容向左移动，负值内容向右移动
                if (Math.abs(dx) > Math.abs(dy)) {
                    scrollBy(-dx, 0);
                }
                // 边界限定
                if (getScrollX() > 0) {  // 向左滑动,滑出 rightMenuView
                    if (rightMenuView != null) {  // 存在 rightMenuView,滑动距离不能超过 rightMenuView 宽度
                        if (getScrollX() > rightMenuView.getMeasuredWidth()) {
                            scrollTo(rightMenuView.getMeasuredWidth(), 0);
                        }
                    } else {  // 不存在 rightMenuView,禁止向左滑动
                        scrollTo(0, 0);
                    }
                } else if (getScrollX() < 0) {  // 向右滑动，滑出 leftMenuView
                    if (leftMenuView != null) {  // 存在 leftMenuView,滑动距离不能大于 leftMenuView 宽度
                        if (getScrollX() < -leftMenuView.getMeasuredWidth()) {  // getScrollX()是负值，
                            scrollTo(-leftMenuView.getMeasuredWidth(), 0);
                        }
                    } else {
                        scrollTo(0, 0);
                    }
                }
                // 当水平滑动时，请求父控件不要拦截事件
                if (Math.abs(dx) > mScaledTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                lastPoint.set(ev.getX(), ev.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                finalDistance = ev.getX() - firstPoint.x;
                if (Math.abs(finalDistance) > mScaledTouchSlop) {
                    isSwipeing = true;
                }
                State state = isShouldOpenMenu(getScrollX());
                handleSwipeMenu(state);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时拦截点击时间
                if (Math.abs(finalDistance) > mScaledTouchSlop) {
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //滑动后不触发contentView的点击事件
                if (isSwipeing) {
                    isSwipeing = false;
                    finalDistance = 0;
                    return true;
                }
//
//                if (getX() < getScreenWidth() - rightMenuView.getMeasuredWidth()) {
//                    return true;
//                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void handleSwipeMenu(State state) {
        if (state == State.RIGHT_MENU_OPEN) {
            swipeMenuLayout = this;
            mScroller.startScroll(getScrollX(), 0, rightMenuView.getMeasuredWidth() - getScrollX(), 0);
            curState = state;
        } else if (state == State.LEFT_MENU_OPEN) {
            swipeMenuLayout = this;
            // getScrollX() 为负值
            mScroller.startScroll(getScrollX(), 0, -getScrollX() - leftMenuView.getMeasuredWidth(), 0);
            curState = state;
        } else if (state == State.CLOSE) {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
            swipeMenuLayout = null;
            curState = null;
        }
        //通知View重绘-invalidate()->onDraw()->computeScroll()
        invalidate();
    }

    private State isShouldOpenMenu(int scrollX) {
        // (1) scrollX > 0 : 表明现在处于 rightMenuView 打开状态，根据临界值决定是关闭，还是继续打开
        if (scrollX > 0) {
            if (finalDistance < 0) {        // 左滑
                if (rightMenuView != null && scrollX > mScaledTouchSlop) {
                    return State.RIGHT_MENU_OPEN;
                }
            } else if (finalDistance > 0) {  // 右滑
                if (rightMenuView != null && scrollX < rightMenuView.getMeasuredWidth() - mScaledTouchSlop) {
                    return State.CLOSE;
                }
            }
        } else if (scrollX < 0) { // (2)scrollX < 0:表明现在处于 leftMenuView 打开状态，根据临界值是否打开，还是关闭
            if (finalDistance < 0) { //  左滑
                if (leftMenuView != null && Math.abs(scrollX) > mScaledTouchSlop) {
                    return State.CLOSE;
                }
            } else if (finalDistance > 0) {  // 右滑
                if (leftMenuView != null && Math.abs(scrollX) > mScaledTouchSlop) {
                    return State.LEFT_MENU_OPEN;
                }
            }
        }
        return State.CLOSE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this == swipeMenuLayout) {
            swipeMenuLayout.handleSwipeMenu(curState);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this == swipeMenuLayout) {
            swipeMenuLayout.handleSwipeMenu(State.CLOSE);
        }
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    enum State {
        LEFT_MENU_OPEN,
        RIGHT_MENU_OPEN,
        CLOSE
    }

}