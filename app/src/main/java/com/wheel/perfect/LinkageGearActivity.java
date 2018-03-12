package com.wheel.perfect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skj.wheel.definedview.LayoutView;
import com.skj.wheel.swiperecyclerview.MyRecyclerView;
import com.skj.wheel.swiperecyclerview.MySwipeRLView;
import com.wheel.perfect.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

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
        initView();
    }

    private void initView() {
        final List<String> mList = new ArrayList<>();
        final TestAdapter adapter = new TestAdapter(mList);
        recyclerView.setAdapter(adapter);
//        layoutView.setVisibility(View.VISIBLE);
//        layoutView.showEmpty(R.mipmap.ic_launcher_round, "搜索不到该相关商品！");
//        layoutView.showError(R.mipmap.ic_launcher_round, "搜索不到该相关商品！");
//        layoutView.showLoading(R.mipmap.ic_launcher_round, "搜索不到该相关商品！");
        swipeRefresh.setOnSwipeListener(new MySwipeRLView.OnSwipeListener() {
            @Override
            public void onRefresh() {

                mList.clear();
                for (int i = 0; i < 10; i++) {
                    mList.add("卧室的多多多" + i);
                }
                adapter.updateList(mList);
//                mList.add("卧室多");
//                mList.add("卧室的多");
//                mList.add("卧多多");
//                mList.add("卧室");
//                mList.add("卧室的多多多");
//                mList.add("卧室的多多多卧室的多多多");
//                mList.add("卧室的多多多");
//                mList.add("卧室的多多多");
//                mList.add("卧室的多多多");
//                mList.add("卧室的多多多卧室的多多多卧室的多多多");
//                mList.add("卧室的多多多卧室的多多多卧室的多多多卧室的多多多");

            }
        });

        recyclerView.setOnBottomListener(new MyRecyclerView.OnBottomListener() {
            @Override
            public void onLoadMore() {
                mList.add("卧室多");
                mList.add("卧室的多");
                mList.add("卧多多");
                mList.add("卧室");
                mList.add("卧室的多多多");
                mList.add("卧室的多多多卧室的多多多");
                mList.add("卧室的多多多");
                mList.add("卧室的多多多");
                mList.add("卧室的多多多");
                mList.add("卧室的多多多卧室的多多多卧室的多多多");
                mList.add("卧室的多多多卧室的多多多卧室的多多多卧室的多多多");
                for (int i = 0; i < 10; i++) {
                    mList.add("卧室的多多多" + i);
                }
                adapter.updateList(mList);
            }
        });

    }

}
