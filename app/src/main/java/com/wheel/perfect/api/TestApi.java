package com.wheel.perfect.api;

import com.skj.wheel.util.SPUtil;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * ClassName:	TestApi
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/25 9:54
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public interface TestApi {
    //    http://www.kuaidi100.com/query?type=yuantong&postid=11111111111
//    @Headers({
//            "Accept: application/json"
//    })

    /**
     * 3.1获取推荐信息
     *
     * @param map
     * @return
     */
    @GET("ssre/external/api/get_ssre_result_v1")
    Flowable<BaseBean> getSSREResult(@QueryMap Map<String, Object> map);

    @GET("query")
    Flowable<Object> testUrl1(@QueryMap Map<String, Object> map);
}
