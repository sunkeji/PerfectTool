package com.wheel.perfect.api;


import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * ClassName:	MyRequest
 * Function:	${TODO} 封装rxjava请求订阅回调
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 10:01
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class MyRequest {

    /**
     * 统一请求处理，网路请求成功统一处理返回结果成功和失败的数据
     *
     * @param observable
     * @param subscriber
     * @return
     */
    public static <T> Flowable post(Flowable observable, CommonResponseSubscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())//  io操作的线程, 通常io操作,如文件读写
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(subscriber);
        return observable;
    }
}
