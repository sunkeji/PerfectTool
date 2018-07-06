package com.wheel.perfect.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skj.wheel.util.LogUtil;
import com.wheel.perfect.R;
import com.wheel.perfect.api.MyOkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 孙科技 on 2018/4/9.
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.upload)
    public void onViewClicked() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "name2.jpg");


        uploadFile("https://www.zhaoapi.cn/file/upload",
                file, "dash.jpg", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.e("+++", "onFailure: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        LogUtil.e("---", "onResponse: " + response.body().string());
                        if (response.isSuccessful()) {
                            TestActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TestActivity.this, "此时上传成功!!", Toast.LENGTH_SHORT).
                                            show();
                                }
                            });

                        }

                    }
                });
    }

    /**
     * post请求上传文件....包括图片....流的形式传任意文件...
     * 参数1 url
     * file表示上传的文件
     * fileName....文件的名字,,例如aaa.jpg
     * params ....传递除了file文件 其他的参数放到map集合
     */
    public static void uploadFile(String url, File file, String fileName, Callback callback) {
        //创建OkHttpClient请求对象

        OkHttpClient okHttpClient = MyOkHttpClient.getOkHttpClient();

        //MultipartBody多功能的请求实体对象,,,formBody只能传表单形式的数据


        //构建
        MultipartBody multipartBody = filesToMultipartBody(file);

        //创建Request
        Request request = new Request.Builder().url(url).post(multipartBody).build();

        //得到Call
        Call call = okHttpClient.newCall(request);
        //执行请求
        call.enqueue(callback);

    }


    public static MultipartBody filesToMultipartBody(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("uid", "4123");
        // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
        RequestBody requestBody = RequestBody.create
                (MediaType.parse("application/octet-stream"), file);
        builder.addFormDataPart("file", file.getName(), requestBody);

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    //    private boolean installNormal(Context context, String filePath) {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        File file = new File(filePath);
//        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
//            return false;
//        }
//        i.setDataAndType(Uri.parse("file://" + filePath),
//                "application/vnd.android.package-archive");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//        return true;
//    }
}
