package com.wheel.perfect;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.skj.wheel.album.activity.AlbumActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2017/12/26.
 */

public class CollapsingActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_layout)
    Toolbar toolbarLayout;
    @BindView(R.id.collaps_layout)
    CollapsingToolbarLayout collapsLayout;
    @BindView(R.id.nestedscrollview_layout)
    NestedScrollView nestedscrollviewLayout;
    @BindView(R.id.text_content)
    TextView textContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_collapsing);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarLayout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowHomeEnabled(true);
        collapsLayout.setTitle("水果界面");
        textContent.setText(getContent());
    }

    private String getContent() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            content.append("woasdfjladsfj");
        }
        return content.toString();
    }

    /**
     * android6.0及以上的权限动态获取
     */
    private void checkPermission() {
        PermissionManager.getInstance(getApplicationContext()).execute(this,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO, Manifest.permission.PACKAGE_USAGE_STATS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance(getApplicationContext()).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.text_content)
    public void onViewClicked() {
        Intent intent = new Intent(CollapsingActivity.this, AlbumActivity.class);
        startActivity(intent);
    }
}
