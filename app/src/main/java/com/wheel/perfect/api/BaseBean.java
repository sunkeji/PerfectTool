package com.wheel.perfect.api;

import java.io.Serializable;

/**
 * ClassName:	BaseBean
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 10:02
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class BaseBean<T> implements Serializable {

    /**
     * success : true
     * code : 0000
     * msg : 操作成功
     * result : xxxx
     * results : null
     */

    private boolean success;
    private String code;
    private String msg;
    private String result;
    private T results;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
