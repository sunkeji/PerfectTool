package com.wheel.perfect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.skj.wheel.album.activity.AlbumAllActivity;
import com.skj.wheel.definedview.MyRGView;
import com.skj.wheel.util.IntentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2018/2/2.
 */

public class LinkageGearActivity extends AppCompatActivity {


    @BindView(R.id.rg)
    MyRGView rg;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkage_gear);
        ButterKnife.bind(this);
//        IntentUtil.startActivity(LinkageGearActivity.this, AlbumAllActivity.class);
    }


    @OnClick(R.id.btn)
    public void onViewClicked() {
        IntentUtil.startActivity(LinkageGearActivity.this, AlbumAllActivity.class);
    }
}
