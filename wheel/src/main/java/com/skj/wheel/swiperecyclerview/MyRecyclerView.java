package com.skj.wheel.swiperecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.skj.wheel.R;
import com.skj.wheel.swiperecyclerview.util.GridItemDecoration;
import com.skj.wheel.swiperecyclerview.util.LinearItemDecoration;
import com.skj.wheel.util.DisplayUtil;

import java.util.List;

/**
 * Created by 孙科技 on 2017/11/2.
 */

public class MyRecyclerView extends RecyclerView {

    private Context mContext;
    private ItemDecoration itemDecoration;//分割线
    private int orientation = 1;//recyclerview 列表展现形式 垂直1或水平0
    private int type = 0;//recyclerview 列表展现形式 列表1或网格0

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyRecyclerView);
        orientation = array.getInteger(R.styleable.MyRecyclerView_orientation, 1);
        type = array.getInteger(R.styleable.MyRecyclerView_type, 1);
        if (type == 1) {
            init(context, orientation);
        } else if (type == 0) {
            init(context);
        }

    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context;

    }

    /**
     * 初始化recyclerview 线性布局
     *
     * @param context
     */
    public void init(Context context, int mOrientation) {
        this.mContext = context;
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, mOrientation, false);
        this.setLayoutManager(linearLayoutManager);
        itemDecoration = new DividerItemDecoration(context, mOrientation);
        this.addItemDecoration(itemDecoration);
    }

    /**
     * 设置网格GridLayoutManager
     */
    private GridLayoutManager gridLayoutManager;

    public void setGridLayoutManager(Context context, int spaceCount) {
        gridLayoutManager = new GridLayoutManager(context, spaceCount);
        this.setLayoutManager(gridLayoutManager);
        itemDecoration = new GridItemDecoration(DisplayUtil.dp2px(mContext, 10));
        this.addItemDecoration(itemDecoration);
    }

    /**
     * 设置不规则网格
     *
     * @param mList
     */
    public void setGridlayoutSpaceCount(final List<?> mList) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position, mList);
            }
        });
    }

    private int setSpanSize(int position, List<?> mList) {
        int count = 1;
        int length = mList.get(position).toString().length();
        if (length >= 13) {
            count = 3;
        } else if (length > 5 && length < 13) {
            count = 2;
        }
        return count;
    }

    /**
     * 移除分割线
     */
    public void removeDivider() {
        this.removeItemDecoration(itemDecoration);
    }

    /**
     * 自定义分割线
     */
    public void setItemDecoration() {
        if (type == 1) {
            setItemDecoration(DisplayUtil.dp2px(mContext, 1));
        } else if (type == 0) {
            setItemDecoration(DisplayUtil.dp2px(mContext, 10));
        }
    }

    /**
     * 自定义宽度的默认分割线
     *
     * @param space
     */
    public void setItemDecoration(int space) {
        if (type == 1) {
            if (orientation == 1)
                setLinearItemDecoration(space, 3);
            else if (orientation == 0)
                setLinearItemDecoration(space, 2);
        } else if (type == 0) {
            setGridItemDecoration(space);
        }

    }

    /**
     * 线性自定义分割线
     *
     * @param space        分割线的宽度
     * @param mOrientation 垂直列表分割线0:top、3:bottom默认3;水平列表分割线1：left、2:right默认2
     */
    public void setLinearItemDecoration(int space, int mOrientation) {
        this.removeItemDecoration(itemDecoration);
        itemDecoration = new LinearItemDecoration(space, mOrientation);
        this.addItemDecoration(itemDecoration);
    }

    /**
     * 网格自定义分割线
     *
     * @param space 分割线的宽度
     */
    public void setGridItemDecoration(int space) {
        this.removeItemDecoration(itemDecoration);
        itemDecoration = new GridItemDecoration(space);
        this.addItemDecoration(itemDecoration);
    }

    /**
     * 监听列表是否滑动到底部
     *
     * @return
     */
    public boolean isSlideBottom() {
        return this != null && this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset() >=
                this.computeVerticalScrollRange();
    }

    /**
     * 设置滑动到底部监听事件
     */
    private OnBottomListener onBottomListener = null;

    public void setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
    }


    public interface OnBottomListener {
        void onLoadMore();
    }

    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    /**
     * layoutManager的类型（枚举）
     */
    protected LAYOUT_MANAGER_TYPE layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        LayoutManager layoutManager = this.getLayoutManager();
        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }

    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        currentScrollState = state;
        LayoutManager layoutManager = this.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                (lastVisibleItemPosition) >= totalItemCount - 1)) {
            if (isSlideBottom() && onBottomListener != null)
                onBottomListener.onLoadMore();
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
