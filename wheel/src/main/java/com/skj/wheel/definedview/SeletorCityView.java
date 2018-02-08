package com.skj.wheel.definedview;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.skj.wheel.definedview.selector.OptionsPickerView;
import com.skj.wheel.definedview.selector.model.DistrictModel;
import com.skj.wheel.definedview.selector.model.LocalAreaBean;

import org.apache.commons.io.IOUtil;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 孙科技 on 2018/2/5.
 * 滑轮联动省市区选择器
 */

public class SeletorCityView {

    private static SeletorCityView cityView;

    private static ArrayList<String> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private static ArrayList<ArrayList<ArrayList<DistrictModel>>> options3Items = new ArrayList<ArrayList<ArrayList<DistrictModel>>>();

    public static SeletorCityView getInstance() {
        if (cityView == null) {
            cityView = new SeletorCityView();
        }
        return cityView;
    }

    /**
     * 省市区三级联动
     *
     * @param context
     * @param callBack CitysPickerCallBack
     */
    public void showOptions(Context context, final CitysPickerCallBack callBack) {

        //选项选择器
        OptionsPickerView pvOptions = new OptionsPickerView(context);
        // 初始化三个列表数据
        initData(context, options1Items, options2Items, options3Items);
        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(10, 1, 1);
//        pvOptions.setTextSize(25);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                //返回的分别是三个级别的选中位置
                callBack.onCitysSelect(options1Items.get(options1),
                        options2Items.get(options1).get(option2),
                        options3Items.get(options1).get(option2).get(options3).getName(),
                        options3Items.get(options1).get(option2).get(options3).getZipcode());
            }
        });
        pvOptions.show();
    }

    /**
     * 时间选择回调
     */
    public interface CitysPickerCallBack {
        void onCitysSelect(String province, String city, String district, String cityCode);
    }

    /**
     * 初始化三个选项卡数据。
     *
     * @param options1Items
     * @param options2Items
     * @param options3Items
     */

    public void initData(Context context, ArrayList<String> options1Items, ArrayList<ArrayList<String>> options2Items
            , ArrayList<ArrayList<ArrayList<DistrictModel>>> options3Items) {
        initProvinceData(context);
        for (int i = 0; i < mProvinceDatas.length; i++) {
            ArrayList<String> options2Items_01 = new ArrayList<String>();
            String[] citys = mCitisDatasMap.get(mProvinceDatas[i]);
            ArrayList<ArrayList<DistrictModel>> options3Items_01 = new ArrayList<ArrayList<DistrictModel>>();
            for (int j = 0; j < citys.length; j++) {
                options2Items_01.add(citys[j]);
                String[] districts = mDistrictDatasMap.get(citys[j]);
                ArrayList<DistrictModel> options3Items_01_01 = new ArrayList<DistrictModel>();
                for (int k = 0; k < districts.length; k++) {
                    DistrictModel districtModel = new DistrictModel();
                    districtModel.setName(districts[k]);
                    districtModel.setZipcode(mZipcodeDatasMap.get(mProvinceDatas[i] + citys[j] + districts[k]));
                    options3Items_01_01.add(districtModel);
                }
                options3Items_01.add(options3Items_01_01);
            }
            options1Items.add(mProvinceDatas[i]);
            options2Items.add(options2Items_01);
            options3Items.add(options3Items_01);

        }

    }
    /**
     * 解析省市区的XML数据
     */

    /**
     * 所有省
     */
    private String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    private void initProvinceData(Context context) {
        /**
         * 解析json赋值到list
         */
        List<LocalAreaBean.RootBean> provinceList = new ArrayList<>();
        String result = getStringFromAssert(context);
        LocalAreaBean areaBean = (LocalAreaBean) getGsonData(LocalAreaBean.class, result);
        provinceList = areaBean.getRoot();

        mProvinceDatas = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
            // 遍历所有省的数据
            mProvinceDatas[i] = provinceList.get(i).getProvince();
            List<LocalAreaBean.RootBean.CitiesBean> cityList = provinceList.get(i).getCities();
            String[] cityNames = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getCity();
                List<LocalAreaBean.RootBean.CitiesBean.AreasBean> districtList = cityList.get(j).getAreas();
                String[] distrinctNameArray = new String[districtList.size()];

                LocalAreaBean.RootBean.CitiesBean.AreasBean[] distrinctArray
                        = new LocalAreaBean.RootBean.CitiesBean.AreasBean[districtList.size()];
                for (int k = 0; k < districtList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    LocalAreaBean.RootBean.CitiesBean.AreasBean districtModel
                            = new LocalAreaBean.RootBean.CitiesBean.AreasBean(districtList.get(k).getArea(),
                            districtList.get(k).getAreaid());
                    // 区/县对于的邮编，保存到mZipcodeDatasMap
//                        JLogUtils.D("zipcode: " + mProvinceDatas[i] + cityNames[j] +
//                                districtList.get(k).getName() + "  " + districtList.get(k).getZipcode());
                    mZipcodeDatasMap.put(mProvinceDatas[i] + cityNames[j] +
                                    districtList.get(k).getArea(),
                            districtList.get(k).getAreaid() + "");
                    distrinctArray[k] = districtModel;
                    distrinctNameArray[k] = districtModel.getArea();
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
            }
            // 省-市的数据，保存到mCitisDatasMap
            mCitisDatasMap.put(provinceList.get(i).getProvince(), cityNames);
        }
    }

    /**
     * 读取getAssets的城市json
     *
     * @param context
     * @return
     */
    public static String getStringFromAssert(Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("city.json"));
            String result = IOUtil.toString(inputReader);
            return "{\"root\":" + result + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gson解析之自定义序列化和反序列化
     * 解决解析对象null指针
     *
     * @param backStr
     * @return
     */

    public Object getGsonData(Class object, String backStr) {
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            return gson.fromJson(backStr, object);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
