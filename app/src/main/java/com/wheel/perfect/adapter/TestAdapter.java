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
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout_btn, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
//            ((ItemViewHolder) holder).itemMenuSwipe.updateView(holder,nowOpen);
//            ((ItemViewHolder) holder).itemMenuSwipe.setOnOnSwipeListener(new SwipeLayout.OnSwipeListener() {
//
//
//                @Override
//                public void onStartOpen() {
//                    if (holder != null && (holder != ((ItemViewHolder) holder))) {
//                        nowOpen.itemMenuSwipe.close();
////                        nowOpen = null;
//                    }
//                }
//
//                @Override
//                public void onOpen() {
//                    nowOpen = ((ItemViewHolder) holder);
//                    canDelete = false;
//                    ((ItemViewHolder) holder).itemMenuDelete.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onStartClose() {
//
//                }
//
//
//                @Override
//                public void onClose() {
//                    if (nowOpen == holder) {
////                        nowOpen = null;
//                    }
//                    ((ItemViewHolder) holder).itemMenuDelete.setVisibility(View.GONE);
//                }
//            });
//            ((ItemViewHolder) holder).itemMenuLin.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (nowOpen != null) {
//                        nowOpen.itemMenuSwipe.close();
//                        return true;
//                    } else {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            canDelete = true;
//                        }
//
//                    }
//                    return false;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private ItemViewHolder nowOpen = null;
    private boolean canDelete = true;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.item_menu_delete)
//        Button itemMenuDelete;
//        @BindView(R.id.checkbox_sigle_shopcart)
//        CheckBox checkboxSigleShopcart;
//        @BindView(R.id.img_icon_shopcart)
//        ImageView imgIconShopcart;
//        @BindView(R.id.text_name_shopcart)
//        TextView textNameShopcart;
//        @BindView(R.id.text_price_shopcart)
//        TextView textPriceShopcart;
//        @BindView(R.id.item_menu_lin)
//        RelativeLayout itemMenuLin;
//        @BindView(R.id.item_menu_swipe)
//        SwipeLayout itemMenuSwipe;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            R.layout.item_menu_right
            ButterKnife.bind(this, itemView);
        }
    }
}
