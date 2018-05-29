package com.wheel.perfect.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wheel.perfect.R;
import com.wheel.perfect.test.FlowTagLayout;
import com.wheel.perfect.test.TagAdapter1;
import com.wheel.perfect.util.SelectorIntervalView;
import com.wheel.perfect.util.TestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2018/4/9.
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    private TagAdapter1 mSizeTagAdapter;
    List<TestBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_item);
        ButterKnife.bind(this);
//        //尺寸
//        mSizeTagAdapter = new TagAdapter1(this, list);
//        sizeFlowLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
//        sizeFlowLayout.setAdapter(mSizeTagAdapter);
//        sizeFlowLayout.setOnTagSelectListener(new OnTagSelectListener() {
//            @Override
//            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
//                if (selectedList != null && selectedList.size() > 0) {
//                    StringBuilder sb = new StringBuilder();
//                    for (int i : selectedList) {
//                        sb.append(parent.getAdapter().getItem(i));
//                        sb.append(":");
//                    }
//                    Snackbar.make(parent, "移动研发:" + sb.toString(), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                } else {
//                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            }
//        });

//        for (int i = 0; i < 5; i++) {
//            TestBean testBean = new TestBean();
//            testBean.setName("我疯了" + i);
//            testBean.setImgUrl("");
//            list.add(testBean);
//        }
//        mSizeTagAdapter.onlyAddAll(list);
    }

    @OnClick(R.id.img_logo)
    public void onViewClicked() {

    }
}
