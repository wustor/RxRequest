package com.fatchao.rxrequest.constants;

import com.fatchao.rxrequest.bean.LoginBean;
import com.fatchao.rxrequest.bean.OneBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
public interface RxUrl {

    //    ------------服务器地址------------------//
    String SERVER_ADDRESS = "http://rest.wufazhuce.com/";

    //GET请求
    @GET("OneForWeb/one/getHpinfo")
    Observable<OneBean> getData(@QueryMap Map<String, String> map);


    //POST请求
    @FormUrlEncoded
    @POST("OneForWeb/one/getHpinfo")
    Observable<OneBean> postData(@FieldMap Map<String, String> map);


    @FormUrlEncoded  //登陆
    @POST("server/account/common/login.json")
    Observable<LoginBean> login(@FieldMap Map<String, String> map);

}
