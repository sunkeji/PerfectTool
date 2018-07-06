package com.skj.wheel.album.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.skj.wheel.BaseActivity;
import com.skj.wheel.R;
import com.skj.wheel.album.adapter.AlbumAdapter;
import com.skj.wheel.album.utils.Bimp;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.album.utils.PublicWay;
import com.skj.wheel.util.ActivityListUtil;
import com.skj.wheel.util.IntentUtil;
import com.skj.wheel.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class AlbumFloderImageActivity extends BaseActivity implements View.OnClickListener {


    //标题栏
    private TextView back, other, title;
    // 显示手机里的所有图片的列表控件
    private GridView gridView;
    private AlbumAdapter gridImageAdapter;
    // 当手机里没有图片时，提示用户没有图片的控件
    private TextView textEmpty;
    //预览、完成栏
    private TextView preview, selectNum;
    private LinearLayout layoutOk;
    //图片管理
    public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.album_main_acitivty);

        back = this.findViewById(R.id.bar_tv_back);
        title = this.findViewById(R.id.bar_tv_title);
        other = this.findViewById(R.id.bar_tv_other);

        preview = this.findViewById(R.id.text_preview);
        selectNum = this.findViewById(R.id.text_select);
        layoutOk = this.findViewById(R.id.layout_ok);

        gridView = this.findViewById(R.id.grid_list);
        textEmpty = this.findViewById(R.id.text_empty);

        setStatusBar();

        String folderName = getIntent().getStringExtra("folderName");
        if (!folderName.equals("")) {
            if (folderName.length() > 8) {
                folderName = folderName.substring(0, 9) + "...";
            }
            title.setText(folderName);
        }

        gridImageAdapter = new AlbumAdapter(this, dataList, Bimp.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        gridView.setEmptyView(textEmpty);
        selectNum.setText(Bimp.tempSelectBitmap.size() + "");
        other.setVisibility(View.VISIBLE);
        other.setText("取消");

        other.setOnClickListener(this);
        back.setOnClickListener(this);
        preview.setOnClickListener(this);
        layoutOk.setOnClickListener(this);
        gridImageAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button button) {
                if (Bimp.tempSelectBitmap.size() >= PublicWay.num && isChecked) {
                    button.setVisibility(View.GONE);
                    toggleButton.setChecked(false);
                    ToastUtil.TextToast(AlbumFloderImageActivity.this, "超出可选图片张数");
                    return;
                }

                if (isChecked) {
                    button.setVisibility(View.VISIBLE);
                    Bimp.tempSelectBitmap.add(dataList.get(position));
                    selectNum.setText(Bimp.tempSelectBitmap.size() + "");
                } else {
                    button.setVisibility(View.GONE);
                    Bimp.tempSelectBitmap.remove(dataList.get(position));
                    selectNum.setText(Bimp.tempSelectBitmap.size() + "");
                }
                selectNum.setText(Bimp.tempSelectBitmap.size() + "");
            }
        });

    }

    private void initData() {
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bar_tv_back) {
            finish();
        } else if (i == R.id.bar_tv_other) {
            Bimp.tempSelectBitmap.clear();
            ActivityListUtil.finishAllActivity();
        } else if (i == R.id.text_preview) {
            if (Bimp.tempSelectBitmap.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("position", "1");
                IntentUtil.startActivity(activity, AlbumVPFixedActivity.class, map);
            } else {
                ToastUtil.TextToast(this, "您未选择图片");
            }
        } else if (i == R.id.layout_ok) {
            ActivityListUtil.finishAllActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectNum.setText(Bimp.tempSelectBitmap.size() + "");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
