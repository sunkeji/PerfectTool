package com.wheel.perfect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skj.wheel.album.activity.AlbumAllActivity;
import com.skj.wheel.definedview.SelectorDateView;
import com.skj.wheel.definedview.SelectorSingleView;
import com.skj.wheel.definedview.SeletorCityView;
import com.skj.wheel.definedview.SideLetterBarView;
import com.skj.wheel.definedview.MyTGView;
import com.skj.wheel.definedview.selector.TimePickerView;
import com.skj.wheel.util.IntentUtil;
import com.yalantis.ucrop.UCrop;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙科技 on 2018/1/3.
 */

public class PictrueActivity extends AppCompatActivity {
    @BindView(R.id.btn_carmera)
    Button btnCarmera;
    @BindView(R.id.btn_image)
    Button btnImage;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tg_text1)
    MyTGView tgText1;
    @BindView(R.id.side_letter_bar)
    SideLetterBarView sideLetterBarView;
    @BindView(R.id.text_overly)
    TextView textOverly;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictrue);
        ButterKnife.bind(this);
        initView();
    }
    List<String> stringList1;
    private TreeMap<String, Object> letterIndexes;
    String[] b = {"A", "B", "D", "E", "F", "H", "I", "K", "M", "N", "O", "P", "T", "U", "V", "W", "X", "Y", "Z"};

    private void initView() {

        letterIndexes = new TreeMap<>();
        for (int index = 0; index < b.length; index++) {
            letterIndexes.put(b[index], index);
        }
        sideLetterBarView.setLetterList(letterIndexes);
        textOverly.setVisibility(View.VISIBLE);
        sideLetterBarView.setOverlay(textOverly);
        sideLetterBarView.setOnLetterChangedListener(new SideLetterBarView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
            }
        });
         stringList1 = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
        stringList1.add("我是的基督教");
        stringList1.add("基督教");
        stringList1.add("我");
        stringList1.add("我是的基");
        stringList1.add("督教");
        stringList1.add("我是的基");
//        }
        tgText1.setTags(stringList1);
        SelectorDateView.alertTimerPicker(this, TimePickerView.Type.YEAR_MONTH_DAY, "yyyy-MM-dd", new SelectorDateView.TimerPickerCallBack() {
            @Override
            public void onTimeSelect(String date) {

            }
        });
    }


    @OnClick({R.id.btn_carmera, R.id.btn_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_carmera:
//                carmera();
//                IntentUtil.startActivity(PictrueActivity.this, AlbumAllActivity.class);
                SeletorCityView.getInstance().showOptions(this, new SeletorCityView.CitysPickerCallBack() {
                    @Override
                    public void onCitysSelect(String province, String city, String district, String cityCode) {

                    }
                });
                break;
            case R.id.btn_image:
//                image();
                SelectorSingleView.alertBottomWheelOption(this, (ArrayList<?>) stringList1, new SelectorSingleView.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {

                    }
                });
                break;
        }
    }


    private void carmera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        ContentValues values = new ContentValues();
//        imageUri = getContentResolver().insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);
    }

    private void image() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    Uri uritempFile;

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
        //裁剪成功后调用
        switch (requestCode) {
            case 1:
                // 裁剪图片
                Uri imageUri = data.getData();
//                image.setImageURI(imageUri);
                cropPhoto(imageUri);
                break;
            case 2:
                // 裁剪图片
//                final Uri uri = imageUri;
                Bundle extras = data.getExtras();
                if (extras != null) {
                    //获得拍的照片
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null, null));
//                    image.setImageURI(uri);
                    cropPhoto(uri);
                }
//

                break;
            case 3:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                Bundle extras = data.getExtras();
//                Bitmap head = extras.getParcelable("data");
//                if (head != null) {
//                    GlideUtil.getInstance().ImageCircleLoader(imgHead, head);// 用ImageView显示出来
//                    List<File> images = new ArrayList<>();
//                    images.add(PictureUtil.getImagePath(head));
//                    uploadImages(images);
//                }
                break;
            case UCrop.REQUEST_CROP:
//                Bundle extras = data.getExtras();
//                Bitmap head = extras.getParcelable("data");
//                if (head != null) {
//                    GlideUtil.getInstance().ImageCircleLoader(imgHead, head);// 用ImageView显示出来
//                    List<File> images = new ArrayList<>();
//                    images.add(PictureUtil.getImagePath(head));
//                    uploadImages(images);
//                }
                final Uri resultUri = UCrop.getOutput(data);
                //设置裁剪完成后的图片显示
                image.setImageURI(resultUri);
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                break;

//            }
        }
    }
}
