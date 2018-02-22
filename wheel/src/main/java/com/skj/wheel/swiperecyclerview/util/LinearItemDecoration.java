package com.skj.wheel.swiperecyclerview.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 孙科技 on 2017/5/4.
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private final int mVerticalSpaceHeight;
    private final int mOrientation;

    public LinearItemDecoration(int mVerticalSpaceHeight, int mOrientation) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.mOrientation = mOrientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()) {
            if (mOrientation == 0)
                outRect.top = mVerticalSpaceHeight;
            else if (mOrientation == 1)
                outRect.left = mVerticalSpaceHeight;
            else if (mOrientation == 2)
                outRect.right = mVerticalSpaceHeight;
            else if (mOrientation == 3)
                outRect.bottom = mVerticalSpaceHeight;
        }
    }
}