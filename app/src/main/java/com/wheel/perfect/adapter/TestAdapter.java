package com.wheel.perfect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wheel.perfect.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 孙科技 on 2018/2/8.
 */

public class TestAdapter extends RecyclerView.Adapter {
    List<String> mList;

    public TestAdapter(List<String> mList) {
        this.mList = mList;
    }

    public void updateList(List<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
//            R.layout.item_menu_right
            ButterKnife.bind(this, itemView);
        }
    }
}
