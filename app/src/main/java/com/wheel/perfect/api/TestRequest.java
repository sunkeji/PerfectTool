package com.wheel.perfect.api;

import android.app.Activity;

import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * ClassName:	TestRequest
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 13:52
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class TestRequest extends MyRetrofit {
//    public static String HOST = "http://www.kuaidi100.com/";
        public static String HOST = "http://10.64.24.20:8080/";
    public static TestApi testApi = getRequest(TestApi.class, HOST);


    public static void getSSREResult(Map<String, Object> map, CommonResponseSubscriber subscriber) {
        MyRequest.post(testApi.getSSREResult(map), subscriber);
    }

    public static void getTest1(Map<String, Object> map, CommonResponseSubscriber subscriber) {
        MyRequest.post(testApi.testUrl1(map), subscriber);
    }
}
