package com.wheel.perfect.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.skj.wheel.definedview.SelectorSingleView;
import com.wheel.perfect.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2018/4/9.
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.image)
    public void onViewClicked() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("男");
        strings.add("女");
        SelectorSingleView.alertBottomWheelOption(TestActivity.this, strings, new SelectorSingleView.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {

            }
        });
    }
}
