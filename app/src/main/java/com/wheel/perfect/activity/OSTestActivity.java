package com.wheel.perfect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skj.wheel.util.LogUtil;
import com.wheel.perfect.MyApplication;
import com.wheel.perfect.R;
import com.wheel.perfect.api.BaseBean;
import com.wheel.perfect.api.CommonResponseSubscriber;
import com.wheel.perfect.api.TestRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassName:	OSTestActivity
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/28 9:49
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class OSTestActivity extends AppCompatActivity {


    @BindView(R.id.edit_token)
    EditText editToken;
    @BindView(R.id.edit_ing)
    EditText editIng;
    @BindView(R.id.edit_lat)
    EditText editLat;
    @BindView(R.id.edit_coord_stype)
    EditText editCoordStype;
    @BindView(R.id.rb_push_type1)
    RadioButton rbPushType1;
    @BindView(R.id.rb_push_type2)
    RadioButton rbPushType2;
    @BindView(R.id.rb_push_type3)
    RadioButton rbPushType3;
    @BindView(R.id.rg_push_type)
    RadioGroup rgPushType;
    @BindView(R.id.rb_check_type1)
    RadioButton rbCheckType1;
    @BindView(R.id.rb_check_type2)
    RadioButton rbCheckType2;
    @BindView(R.id.rb_check_type3)
    RadioButton rbCheckType3;
    @BindView(R.id.rg_check_type)
    RadioGroup rgCheckType;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.rb_push_type4)
    RadioButton rbPushType4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        ButterKnife.bind(this);

        huToken = Settings.System.getString(getContentResolver(), "changan_access_token");
        if (huToken == null) {
            sendBroadcast(new Intent("changan.token.past"));
            huToken = "";
        }
        editToken.setText(huToken);
        LogUtil.i("--huToken:" + huToken);

        rgCheckType.setVisibility(View.GONE);
        checkType = "";
        rgPushType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCheckType.setVisibility(View.GONE);
                checkType = "";
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_push_type1:
                        pushType = "power_on";
                        break;
                    case R.id.rb_push_type2:
                        pushType = "drive_check";
                        checkType = "FGDV";
                        rgCheckType.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_push_type3:
                        pushType = "style";
                        break;
                    case R.id.rb_push_type4:
                        pushType = "";
                        break;
                }
            }
        });
        rgCheckType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_check_type1:
                        checkType = "FGDV";
                        break;
                    case R.id.rb_check_type2:
                        checkType = "HPPY";
                        break;
                    case R.id.rb_check_type3:
                        checkType = "DPSN";
                        break;
                }
            }
        });
    }


    @OnClick(R.id.button)
    public void onViewClicked() {
        getSSREResult();
    }

    /**
     * 推荐引擎
     */
    private String huToken = "92ab989c-675c-4ad1-865f-f5672b747a1f";
    private String ing = "106.55";
    private String lat = "29.58";
    private int coordstype = 5;
    private String pushType = "power_on";
    private String checkType = "FGDV";

    private void getSSREResult() {
        Map<String, Object> map = new HashMap<>();
        if (TextUtils.isEmpty(editToken.getText().toString()))
            map.put("huToken", huToken);
        else
            map.put("huToken", editToken.getText().toString());

        if (TextUtils.isEmpty(editIng.getText().toString()))
            map.put("ing", ing);
        else
            map.put("ing", editIng.getText().toString());

        if (TextUtils.isEmpty(editLat.getText().toString()))
            map.put("lat", lat);
        else
            map.put("lat", editLat.getText().toString());

        if (TextUtils.isEmpty(editCoordStype.getText().toString()))
            map.put("coordstype", coordstype);
        else
            map.put("coordstype", Integer.valueOf(editCoordStype.getText().toString()));

        if (!TextUtils.isEmpty(pushType)) {
            map.put("pushType", pushType);
        }
        if (!TextUtils.isEmpty(checkType)) {
            map.put("checkType", checkType);
        }

        TestRequest.getSSREResult(map, new CommonResponseSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                String result = new Gson().toJson(baseBean).toString();
                LogUtil.i("onNext" + result);
                tvMsg.setText("返回结果：\n\n" + MyApplication.getInstance().duplicate()
                        + "\n\n" + result);
            }
        });
    }
}
