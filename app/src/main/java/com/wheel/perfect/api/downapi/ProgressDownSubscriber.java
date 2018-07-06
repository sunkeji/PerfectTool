package com.wheel.perfect.api.downapi;


import android.os.Handler;


import java.lang.ref.SoftReference;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * ClassName:	ProgressDownSubscriber
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/26 16:50
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class ProgressDownSubscriber<T> extends ResourceSubscriber<T>
        implements DownloadProgressListener {
    //弱引用结果回调
    private SoftReference<HttpDownOnNextListener> mSubscriberOnNextListener;
    /*下载数据*/
    private DownApkInfo downInfo;
    private Handler handler;

    public ProgressDownSubscriber(DownApkInfo downInfo, Handler handler) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
        this.handler = handler;
    }


    public void setDownInfo(DownApkInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }

    /**
     * 订阅回调将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    /**
     * 对返回错误进行统一处理
     *
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(t);
        }
        //关闭下载请求
        HttpDownManager.getInstance().remove(downInfo);
        downInfo.setState(DownState.ERROR);
        DbDownUtil.getInstance().update(downInfo);
    }

    @Override
    public void onComplete() {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onComplete();
        }
        //关闭下载请求
        HttpDownManager.getInstance().remove(downInfo);
        downInfo.setState(DownState.FINISH);
        DbDownUtil.getInstance().update(downInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onStart();
        }
        downInfo.setState(DownState.START);
    }

    @Override
    public void update(long read, long count, boolean done) {
        if (downInfo.getApkTotalLength() > count)
            read = downInfo.getApkTotalLength() - count + read;
        else
            downInfo.setApkTotalLength(count);
        downInfo.setApkDownLength(read);
        if (mSubscriberOnNextListener.get() == null || !downInfo.isUpdateProgress()) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                if (downInfo.getState() == DownState.PAUSE || downInfo.getState() == DownState.STOP)
                    return;
                downInfo.setState(DownState.DOWN);
                mSubscriberOnNextListener.get().updateProgress(downInfo.getApkDownLength(), downInfo.getApkTotalLength());
            }
        });
    }
}
