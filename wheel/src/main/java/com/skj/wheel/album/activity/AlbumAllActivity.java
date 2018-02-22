package com.skj.wheel.album.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.skj.wheel.BaseActivity;
import com.skj.wheel.R;
import com.skj.wheel.album.adapter.AlbumAdapter;
import com.skj.wheel.album.utils.AlbumHelper;
import com.skj.wheel.album.utils.Bimp;
import com.skj.wheel.album.utils.ImageBucket;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.album.utils.PublicWay;
import com.skj.wheel.util.IntentUtil;
import com.skj.wheel.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class AlbumAllActivity extends BaseActivity implements View.OnClickListener {
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
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
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

        gridView.setEmptyView(textEmpty);
        selectNum.setText(Bimp.tempSelectBitmap.size() + "");
        other.setVisibility(View.VISIBLE);
        other.setText("相册");
        title.setText("全部图片");


        other.setOnClickListener(this);
        back.setOnClickListener(this);
        preview.setOnClickListener(this);
        layoutOk.setOnClickListener(this);
    }

    private void initData() {
        loadData();
        // 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plugin_camera_no_pictures);

        selectNum.setText(Bimp.tempSelectBitmap.size() + "");
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
            Bimp.tempSelectBitmap.clear();
            finish();
        } else if (i == R.id.bar_tv_other) {
            IntentUtil.startActivity(activity, AlbumFolderActivity.class);
        } else if (i == R.id.text_preview) {
            if (Bimp.tempSelectBitmap.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("position", "1");
                IntentUtil.startActivity(activity, AlbumVPFixedActivity.class, map);
            } else {
                ToastUtil.TextToast("您未选择图片");
            }
        } else if (i == R.id.layout_ok) {
            finish();
        }
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contentList = helper.getImagesBucketList(false);
                dataList = new ArrayList<ImageItem>();
                for (int i = 0; i < contentList.size(); i++) {
                    dataList.addAll(contentList.get(i).imageList);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        gridImageAdapter = new AlbumAdapter(AlbumAllActivity.this, dataList,
                                Bimp.tempSelectBitmap);
                        gridView.setAdapter(gridImageAdapter);
                        gridImageAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked,
                                                    Button chooseBt) {
                                if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
                                    toggleButton.setChecked(false);
                                    chooseBt.setVisibility(View.GONE);
                                    if (!removeOneData(dataList.get(position))) {
                                        Toast.makeText(AlbumAllActivity.this, R.string.only_choose_num, Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                                if (isChecked) {
                                    chooseBt.setVisibility(View.VISIBLE);
                                    Bimp.tempSelectBitmap.add(dataList.get(position));
                                    selectNum.setText(Bimp.tempSelectBitmap.size() + "");
                                } else {
                                    Bimp.tempSelectBitmap.remove(dataList.get(position));
                                    chooseBt.setVisibility(View.GONE);
                                    selectNum.setText(Bimp.tempSelectBitmap.size() + "");
                                }
                                selectNum.setText(Bimp.tempSelectBitmap.size() + "");
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private boolean removeOneData(ImageItem imageItem) {
        if (Bimp.tempSelectBitmap.contains(imageItem)) {
            Bimp.tempSelectBitmap.remove(imageItem);
            selectNum.setText(Bimp.tempSelectBitmap.size() + "");
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        selectNum.setText(Bimp.tempSelectBitmap.size() + "");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
