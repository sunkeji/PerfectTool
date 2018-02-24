package com.skj.wheel.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skj.wheel.BaseActivity;
import com.skj.wheel.R;
import com.skj.wheel.album.utils.Bimp;
import com.skj.wheel.album.utils.ViewPagerFixed;
import com.skj.wheel.gestures.PhotoView;

import java.util.ArrayList;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class AlbumVPFixedActivity extends BaseActivity implements View.OnClickListener {
    //标题栏
    private TextView back, other, title;
    //展示图片放大
    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.album_viewpager_fixed_activity);// 切屏到主界面
        back = this.findViewById(R.id.bar_tv_back);
        title = this.findViewById(R.id.bar_tv_title);
        other = this.findViewById(R.id.bar_tv_other);

        other.setText("删除");
        other.setVisibility(View.VISIBLE);

        // 为发送按钮设置文字
        pager = findViewById(R.id.viewpager_fixed);
        pager.setOnPageChangeListener(pageChangeListener);
        setStatusBar();

        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
            initListViews(Bimp.tempSelectBitmap.get(i).getBitmap());
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin(10);
        int id = getIntent().getIntExtra("ID", 0);
        pager.setCurrentItem(id);

        title.setText(location + 1 + "/" + Bimp.tempSelectBitmap.size());
    }

    private void initData() {
        back.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bar_tv_back) {
            finish();
        } else if (i == R.id.bar_tv_other) {
            if (listViews.size() == 1) {
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                title.setText(location + 1 + "/" + Bimp.tempSelectBitmap.size());
                Intent intent = new Intent("data.broadcast.action");
                sendBroadcast(intent);
                finish();
            } else {
                Bimp.tempSelectBitmap.remove(location);
                Bimp.max--;
                pager.removeAllViews();
                listViews.remove(location);
                adapter.setListViews(listViews);
                title.setText(location + 1 + "/" + Bimp.tempSelectBitmap.size());
                adapter.notifyDataSetChanged();
            }
        }
    }

    // 当前的位置
    private int location = 0;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
            title.setText(location + 1 + "/" + Bimp.tempSelectBitmap.size());
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
