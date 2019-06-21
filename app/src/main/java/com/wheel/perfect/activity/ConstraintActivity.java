package com.wheel.perfect.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skj.wheel.definedview.LayoutView;
import com.skj.wheel.swiperecyclerview.MyRecyclerView;
import com.skj.wheel.swiperecyclerview.MySwipeRLView;
import com.wheel.perfect.R;
import com.wheel.perfect.adapter.DownListAdapter;
import com.wheel.perfect.api.downapi.DbDownUtil;
import com.wheel.perfect.api.downapi.DownApkInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ClassName:	ConstraintActivity
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/19 17:15
 *
 * @author 孙科技
 * @version ${GRADLE_VERSION}
 * @see
 * @since JDK 1.8
 */
public class ConstraintActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    MyRecyclerView recyclerView;
    @BindView(R.id.layout_view)
    LayoutView layoutView;
    @BindView(R.id.swipe_refresh)
    MySwipeRLView swipeRefresh;

    private DownListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_view);
        ButterKnife.bind(this);
        swipeRefresh.setOnSwipeListener(new MySwipeRLView.OnSwipeListener() {
            @Override
            public void onRefresh() {
//                listData.clear();
//                initData();
//                adapter.updataList(listData);
            }
        });
        recyclerView.setOnBottomListener(new MyRecyclerView.OnBottomListener() {
            @Override
            public void onLoadMore() {
//                initData();
//                adapter.updataList(listData);
            }
        });
//        adapter = new DownListAdapter(listData);
//        recyclerView.setAdapter(adapter);
        initData();
    }

    private void getList() {
        for (int i = 0; i < 8; i++) {
            File outputFile = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS), "test" + i + ".mp4");
            DownApkInfo apkApi = new DownApkInfo();
            if (i == 0)
                apkApi.setApkUrl("http://47.104.135.91/yc.apk");
            else
                apkApi.setApkUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            apkApi.setId(i);
            apkApi.setUpdateProgress(true);
            apkApi.setApkSavePath(outputFile.getAbsolutePath());
            listData.add(apkApi);
        }
    }

    private List<DownApkInfo> listData = new ArrayList<>();
    private DbDownUtil dbUtil;

    private void initData() {
        dbUtil = DbDownUtil.getInstance();
        listData = dbUtil.queryAllList();
        /*第一次模拟服务器返回数据掺入到数据库中*/
        if (listData.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                File outputFile = new File
                        (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                "android" + i + ".apk");
                DownApkInfo apkApi = new DownApkInfo();
                if (i == 0)
                    apkApi.setApkUrl("http://imtt.dd.qq.com/16891/1003ECB6536079D8BE30B99D07D3B106.apk");
                else
                    apkApi.setApkUrl("http://imtt.dd.qq.com/16891/D7C2338128FB42016E1116F017BF31F0.apk");
                apkApi.setId(i);
                apkApi.setUpdateProgress(true);
                apkApi.setApkSavePath(outputFile.getAbsolutePath());
                dbUtil.save(apkApi);
            }
            listData = dbUtil.queryAllList();
        }
        DownListAdapter adapter = new DownListAdapter(listData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*记录退出时下载任务的状态-复原用*/
        if (dbUtil == null)
            return;
        for (DownApkInfo downInfo : listData) {
            dbUtil.update(downInfo);
        }
    }
}
