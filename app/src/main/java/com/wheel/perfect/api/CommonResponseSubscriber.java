package com.wheel.perfect.api;

import android.app.Activity;
import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.skj.wheel.util.KLogUtil;
import com.skj.wheel.util.KToastUtil;
import com.wheel.perfect.MyApplication;
import com.wheel.perfect.util.DialogUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;


/**
 * ClassName:	CommonResponseSubscriber
 * Function:	${TODO} 封装请求回调处理请求失败类型、成功回到参数
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 10:04
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class CommonResponseSubscriber<T> extends ResourceSubscriber<T> {
    public CommonResponseSubscriber() {
    }

    private DialogUtil dialogUtil;

    public CommonResponseSubscriber(Activity activity) {
        dialogUtil = new DialogUtil(activity);
    }

    @Override
    public void onNext(T t) {
        dialogUtil.closeLoading();
    }

    @Override
    public void onError(Throwable e) {
        dialogUtil.closeLoading();
        if (e == null) {
            return;
        }
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        KLogUtil.e("异常提示:" + e.getMessage() + "-" + e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {
        KLogUtil.i("onCompleted");
        dialogUtil.closeLoading();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogUtil.startLoading();
        KLogUtil.i("onStart");
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                KToastUtil.TextToast(MyApplication.mContext, "连接错误");
                break;
            case CONNECT_TIMEOUT:
                KToastUtil.TextToast(MyApplication.mContext, "连接超时");
                break;
            case BAD_NETWORK:
                KToastUtil.TextToast(MyApplication.mContext, "网络问题");
                break;
            case PARSE_ERROR:
                KToastUtil.TextToast(MyApplication.mContext, "解析数据失败");
                break;
            case UNKNOWN_ERROR:
            default:
                KToastUtil.TextToast(MyApplication.mContext, "未知错误");
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
