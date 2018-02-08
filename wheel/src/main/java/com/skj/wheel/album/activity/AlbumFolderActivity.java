package com.skj.wheel.album.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.skj.wheel.BaseActivity;
import com.skj.wheel.R;
import com.skj.wheel.album.adapter.AlbumFolderAdapter;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.util.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class AlbumFolderActivity extends BaseActivity implements View.OnClickListener {
    //标题栏
    private TextView title, back, other;
    // 显示手机里的所有图片文件夹的列表控件
    private GridView gridView;
    private AlbumFolderAdapter folderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.album_folder_activity);

        back = this.findViewById(R.id.bar_tv_back);
        title = this.findViewById(R.id.bar_tv_title);
        other = this.findViewById(R.id.bar_tv_other);
        title.setText("选择相册");
        gridView = findViewById(R.id.fileGridView);

        setStatusBar();

        folderAdapter = new AlbumFolderAdapter(this);
        gridView.setAdapter(folderAdapter);
    }

    private void initData() {
        back.setOnClickListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumFloderImageActivity.dataList = (ArrayList<ImageItem>) AlbumAllActivity.contentList.get(position).imageList;
                String folderName = AlbumAllActivity.contentList.get(position).bucketName;
                Map<String, Object> map = new HashMap<>();
                map.put("folderName", folderName);
                IntentUtil.startActivity(activity, AlbumFloderImageActivity.class, map);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bar_tv_back) {
            finish();
        }
    }
}
