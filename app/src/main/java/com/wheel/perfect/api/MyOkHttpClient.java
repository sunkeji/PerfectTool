package com.wheel.perfect.api;

import android.os.Environment;


import com.skj.wheel.util.KLogUtil;
import com.wheel.perfect.MyApplication;
import com.wheel.perfect.api.downapi.DownloadProgressListener;
import com.wheel.perfect.api.downapi.DownloadResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * ClassName:	MyOkHttpClient
 * Function:	${TODO} okhttp网络请求配置,自定义拦截器
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/20 14:29
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class MyOkHttpClient {

    public static final String FILE_SDCARD = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cache";
    /**
     * 初始化OkHttp3
     */
    private static OkHttpClient okHttpClient;
    private static long timeout = 10 * 6;//网路连接、读、写超时时间设置
    /**
     * 设置缓存目录和大小
     */
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(new File(FILE_SDCARD), cacheSize);

    /**
     * 配置okhttp 请求
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)  //禁用okhttp自身的的缓存
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)//连接、读、写超时时间设置
                    .retryOnConnectionFailure(true)//错误重连
//                    .addInterceptor(new MyInterceptor())//添加拦截器
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            KLogUtil.i("okhttp:", "Url=" + message);
                            if (message.contains("--> GET"))
                                MyApplication.getInstance().copy(message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))//日志拦截器
                    .build();

        return okHttpClient;
    }

    /**
     * http 请求拦截器（为请求发生过程中拦截配置一些参数）
     */
    private static class MyInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            //添加header、配置缓存类型、对请求签名等等在网络请求收到回复前的操作
            Request request = builder
                    .cacheControl(CacheControl.FORCE_NETWORK)
//                    .addHeader("Accept", "application/json")
                    .build();
            Response originalResponse = chain.proceed(request);
            return originalResponse;
        }
    }

    /**
     * okhtttp下载拦截，添加下载进度监听
     */
    public class DownloadInterceptor implements Interceptor {

        private DownloadProgressListener listener;

        public DownloadInterceptor(DownloadProgressListener listener) {
            this.listener = listener;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            return originalResponse.newBuilder()
                    .body(new DownloadResponseBody(originalResponse.body(), listener))
                    .build();
        }
    }

    /**
     * 设置cookie 的接收和设置
     */
    public static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Cookie", "Cookie");
            return chain.proceed(builder.build());
        }
    }

    public static class ReceivedCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                KLogUtil.i("-------cookie:" + cookies);
            }
            return originalResponse;
        }
    }

}
