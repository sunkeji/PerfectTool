package com.wheel.perfect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.skj.wheel.citylist.adapter.CityListAdapter;
import com.skj.wheel.citylist.adapter.ResultListAdapter;
import com.skj.wheel.citylist.db.DBManager;
import com.skj.wheel.citylist.model.City;
import com.skj.wheel.citylist.model.LocateState;
import com.skj.wheel.definedview.SideLetterBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙科技 on 2018/2/2.
 */

public class SelectActivity extends AppCompatActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @BindView(R.id.listview_all_city)
    ListView listviewAllCity;
    @BindView(R.id.side_letter_bar)
    SideLetterBarView sideLetterBar;
    @BindView(R.id.text_overly)
    TextView textOverly;
    @BindView(R.id.listview_search_result)
    ListView listviewSearchResult;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities = new ArrayList<>();
    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_city_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
//                back(name);
            }

            @Override
            public void onLocateClick() {
//                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                mCityAdapter.updateLocateState(LocateState.SUCCESS, "重庆市");
            }
        });
        mCityAdapter.updateLocateState(LocateState.SUCCESS, "重庆市");
        mResultAdapter = new ResultListAdapter(this, null);


        listviewAllCity.setAdapter(mCityAdapter);
        sideLetterBar.setOverlay(textOverly);
        listviewSearchResult.setAdapter(mResultAdapter);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBarView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                listviewAllCity.setSelection(position);
            }
        });
        listviewSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                back(mResultAdapter.getItem(position).getName());
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    ivSearchClear.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    listviewSearchResult.setVisibility(View.GONE);
                } else {
                    ivSearchClear.setVisibility(View.VISIBLE);
                    listviewSearchResult.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });
    }
}
