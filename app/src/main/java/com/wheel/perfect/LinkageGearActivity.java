package com.wheel.perfect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2018/2/2.
 */

public class LinkageGearActivity extends AppCompatActivity {
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkage_gear);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.text)
    public void onViewClicked() {
    }


}
