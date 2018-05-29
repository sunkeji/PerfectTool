package com.wheel.perfect.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wheel.perfect.R;

import java.util.List;

import butterknife.BindView;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).layoutTag.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
//                    if (MaxLimitUtil.limitList.size() == 0) {
//                        ((ItemViewHolder) holder).textName.setTextColor(R.color.main_color);
//                        ((ItemViewHolder) holder).layoutTag.setBackgroundResource(R.drawable.shape_foc_color1);
//                        MaxLimitUtil.limitList.add(position);
//                    } else {
//                        if (position == (Integer) MaxLimitUtil.limitList.get(0)) {
//                            ((ItemViewHolder) holder).textName.setTextColor(R.color.text_color);
//                            ((ItemViewHolder) holder).layoutTag.setBackgroundResource(R.drawable.shape_foc_color2);
//                            MaxLimitUtil.limitList.remove(MaxLimitUtil.limitList.get(0));
//                        } else {
//                            ((ItemViewHolder) holder).textName.setTextColor(R.color.main_color);
//                            ((ItemViewHolder) holder).layoutTag.setBackgroundResource(R.drawable.shape_foc_color1);
//                            MaxLimitUtil.limitList.add(position);
//                        }
//                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_logo)
        ImageView imgLogo;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.layout_tag)
        LinearLayout layoutTag;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            R.layout.item_shop_cart
            ButterKnife.bind(this, itemView);
        }
    }
}
