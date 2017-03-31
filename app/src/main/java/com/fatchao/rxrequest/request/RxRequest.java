
package com.fatchao.rxrequest.request;

import android.os.Build;
import android.util.Log;

import com.fatchao.rxrequest.BaseApplication;
import com.fatchao.rxrequest.constants.RxUrl;
import com.fatchao.rxrequest.utils.NetworkManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.CacheControl.FORCE_CACHE;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
@SuppressWarnings("unchecked")

public class RxRequest {
    private static Retrofit sRetrofit, cacheRetrofit;
    private static OkHttpClient sOkHttpClient, cacheOkhttpClient;
    private static RxRequest instance;
    private final static Object mRetrofitLock = new Object();
    private boolean isCache;
    //这是设置在多长时间范围内获取缓存里面
    public static int cacheTime = 10;
    public static CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(cacheTime, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
            .build();


    //get请求缓存(不带token)
    private static Retrofit getCacheRetrofit() {
        if (cacheRetrofit == null) {
            synchronized (mRetrofitLock) {
                if (cacheRetrofit == null) {
                    //设置缓存
                    File httpCacheDirectory = new File(BaseApplication.getContext().getCacheDir(), "tajiaCache");
                    Cache cache = null;
                    try {
                        cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
                    } catch (Exception e) {
                        Log.e("OKHttp", "Could not create http cache", e);
                    }
                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (NetworkManager.isNetworkAvailable(BaseApplication.getContext())) {//有网时
                                Response response = chain.proceed(request);
                                String cacheControl = request.cacheControl().toString();
                                Log.e("yjbo-cache", "在线缓存在1分钟内可读取" + cacheControl);
                                return response.newBuilder()
                                        .removeHeader("Pragma")
                                        .removeHeader("Cache-Control")
                                        .header("Cache-Control", "public, max-age=" + 0)
                                        .build();
                            } else {
                                //无网时
                                request = request.newBuilder()
                                        .cacheControl(FORCE_CACHE)//此处不设置过期时间
                                        .build();
                                Response response = chain.proceed(request);
                                //下面注释的部分设置也没有效果，因为在上面已经设置了
                                return response.newBuilder()
                                        .header("Cache-Control", "public, only-if-cached")
                                        .removeHeader("Pragma")
                                        .build();
                            }

                        }

                    };
                    OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    clientBuilder
                            .cache(cache)
                            .addInterceptor(httpLoggingInterceptor)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(interceptor);
                    cacheOkhttpClient = clientBuilder
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    String header = Build.DEVICE + "-" + Build.BRAND + "-" + Build.MODEL + "-" + Build.VERSION.RELEASE + "-" + Build.TIME + "-" + Build.VERSION.INCREMENTAL;
                                    Request request = chain.request()
                                            .newBuilder()
                                            .addHeader("User-Agent", header)
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                    cacheRetrofit = new Retrofit.Builder().client(cacheOkhttpClient)
                            .baseUrl(RxUrl.SERVER_ADDRESS)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return cacheRetrofit;
    }


    //post请求不缓存

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            synchronized (mRetrofitLock) {
                if (sRetrofit == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    clientBuilder.addInterceptor(httpLoggingInterceptor);
                    sOkHttpClient = clientBuilder
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    String header = Build.DEVICE + "-" + Build.BRAND + "-" + Build.MODEL + "-" + Build.VERSION.RELEASE + "-" + Build.TIME + "-" + Build.VERSION.INCREMENTAL;
                                    Request request = chain.request()
                                            .newBuilder()
                                            .addHeader("User-Agent", header)
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                    sRetrofit = new Retrofit.Builder().client(sOkHttpClient)
                            .baseUrl(RxUrl.SERVER_ADDRESS)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return sRetrofit;
    }


    //获取请求类实例
    public static RxRequest getInstance() {
        if (instance == null) {
            synchronized (RxRequest.class) {
                if (instance == null) {
                    instance = new RxRequest();
                }
            }
        }
        return instance;
    }


    public RxUrl getProxy(boolean isCache) {
        RxUrl mRxUrl = null;
        if (isCache) {
            mRxUrl = getCacheRetrofit().create(RxUrl.class);
        } else {
            mRxUrl = getRetrofit().create(RxUrl.class);
        }
        return (RxUrl) Proxy.newProxyInstance(RxUrl.class.getClassLoader(), new Class<?>[]{RxUrl.class}, new ProxyHandler(mRxUrl, false));
    }


}
