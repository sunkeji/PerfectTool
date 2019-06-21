package com.wheel.perfect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skj.wheel.definedview.LayoutView;
import com.skj.wheel.swiperecyclerview.MyRecyclerView;
import com.skj.wheel.swiperecyclerview.MySwipeRLView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙科技 on 2018/2/2.
 */

public class LinkageGearActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    MyRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    MySwipeRLView swipeRefresh;
    @BindView(R.id.layout_view)
    LayoutView layoutView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkage_gear);
        ButterKnife.bind(this);
    }

}
