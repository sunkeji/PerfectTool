package com.wheel.perfect.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skj.wheel.util.GlideUtil;
import com.wheel.perfect.R;
import com.wheel.perfect.util.TestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter1 extends BaseAdapter implements OnInitSelectedPosition {

    private Context mContext;
    private List<TestBean> mDataList;

    public TagAdapter1(Context context, List<TestBean> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_logo);
        TestBean bean = mDataList.get(position);
        textView.setText(bean.getName());
//        GlideUtil.getInstance().ImageLoader(imageView, "http://p4.so.qhmsg.com/t01e6a5e96377408709.jpg");
        return view;
    }

    public void onlyAddAll(List<TestBean> datas) {
        this.mDataList = datas;
        notifyDataSetChanged();
    }
//

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
}
