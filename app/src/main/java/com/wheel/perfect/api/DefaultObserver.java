package com.wheel.perfect.api;

import android.app.Activity;
import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * ClassName:	DefaultObserver
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 15:34
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public abstract class DefaultObserver<T extends BaseBean> implements Observer<T> {

    private Activity activity;
    //  Activity 是否在执行onStop()时取消订阅
    private boolean isAddInStop = false;

    //    private CommonDialogUtils dialogUtils;
    public DefaultObserver(Activity activity) {
        this.activity = activity;
//        dialogUtils=new CommonDialogUtils();
//        dialogUtils.showProgress(activity);
    }

    public DefaultObserver(Activity activity, boolean isShowLoading) {
        this.activity = activity;
//        dialogUtils=new CommonDialogUtils();
//        if (isShowLoading) {
//            dialogUtils.showProgress(activity,"Loading...");
//        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        dismissProgress();
//        if (!t.getMessage().equals("")) {
//            onSuccess(t);
//        } else {
//            onFail(t);
//        }
    }

    private void dismissProgress() {
//        if(dialogUtils!=null){
//            dialogUtils.dismissProgress();
//        }
    }


    @Override
    public void onError(@NonNull Throwable e) {
//        LogUtils.e(e.getMessage());
        dismissProgress();
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
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    public void onFail(T response) {
//        String message = response.getMessage();
//        if (TextUtils.isEmpty(message)) {
//            ToastUtils.show(R.string.response_return_error);
//        } else {
//            ToastUtils.show(message);
//        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                break;

            case CONNECT_TIMEOUT:
                break;

            case BAD_NETWORK:
                break;

            case PARSE_ERROR:
                break;

            case UNKNOWN_ERROR:
            default:
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
