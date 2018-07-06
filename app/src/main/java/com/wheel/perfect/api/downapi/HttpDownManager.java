package com.wheel.perfect.api.downapi;

import android.os.Handler;
import android.os.Looper;

import com.skj.wheel.util.LogUtil;
import com.skj.wheel.util.NetUtil;
import com.wheel.perfect.MyApplication;
import com.wheel.perfect.api.MyOkHttpClient;
import com.wheel.perfect.api.MyRetrofit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * ClassName:	HttpDownManager
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/26 16:45
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class HttpDownManager {
    /*记录下载数据*/
    private Set<DownApkInfo> downInfos;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;
    /*单利对象*/
    private volatile static HttpDownManager INSTANCE;
    /*数据库类*/
    private DbDownUtil db;
    /*下载进度回掉主线程*/
    private Handler handler;

    /**
     * 初始化下载配置
     */
    public HttpDownManager() {
        downInfos = new HashSet<>();
        subMap = new HashMap<>();
        db = DbDownUtil.getInstance();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单列
     *
     * @return
     */
    public static HttpDownManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDownManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDownManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 下载
     *
     * @param info
     */
    public void startDown(final DownApkInfo info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getApkUrl()) != null) {
            subMap.get(info.getApkUrl()).setDownInfo(info);
            return;
        }
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info, handler);
        /*记录回调sub*/
        subMap.put(info.getApkUrl(), subscriber);
        /*获取service，多次请求公用一个sercie*/
        HttpDownService httpService;
        if (downInfos.contains(info)) {
            httpService = info.getService();
        } else {
            DownloadInterceptor interceptor = new DownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);
            builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.i("okhttp:", "Url=" + message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY));//日志拦截器

            httpService = MyRetrofit.getRequest(HttpDownService.class,
                    NetUtil.getBasUrl(info.getApkUrl()), builder.build());
            info.setService(httpService);
            downInfos.add(info);
        }
        httpService.download("bytes=" + info.getApkDownLength() + "-", info.getApkUrl())
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*失败后的retry配置*/
//                .retryWhen(new RetryWhenNetworkException())
//                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Function<ResponseBody, DownApkInfo>() {
                    @Override
                    public DownApkInfo apply(ResponseBody responseBody) throws Exception {
                        writeCaches(responseBody, new File(info.getApkSavePath()), info);
                        return info;
                    }

                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(subscriber);
    }

    /**
     * 停止下载
     */
    public void stopDown(DownApkInfo info) {
        if (info == null) return;
        info.setState(DownState.STOP);
        info.getListener().onStop();
        if (subMap.containsKey(info.getApkUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getApkUrl());
            subscriber.dispose();
            subMap.remove(info.getApkUrl());
        }
        /*保存数据库信息和本地文件*/
        db.save(info);
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(DownApkInfo info) {
        if (info == null) return;
        info.setState(DownState.PAUSE);
        info.getListener().onPuase();
        if (subMap.containsKey(info.getApkUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getApkUrl());
            subscriber.dispose();
            subMap.remove(info.getApkUrl());
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
        db.update(info);
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (DownApkInfo downInfo : downInfos) {
            stopDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (DownApkInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<DownApkInfo> getDownInfos() {
        return downInfos;
    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove(DownApkInfo info) {
        subMap.remove(info.getApkUrl());
        downInfos.remove(info);
    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCaches(ResponseBody responseBody, File file, DownApkInfo info) {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                long allLength = 0 == info.getApkTotalLength() ?
                        responseBody.contentLength() : info.getApkDownLength() + responseBody
                        .contentLength();

                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                        info.getApkDownLength(), allLength - info.getApkDownLength());
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
