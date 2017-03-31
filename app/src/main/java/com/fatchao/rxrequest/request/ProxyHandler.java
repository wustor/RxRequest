/*
 * Copyright (C) 2016 david.wei (lighters)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatchao.rxrequest.request;
/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.fatchao.rxrequest.BaseApplication;
import com.fatchao.rxrequest.bean.LoginBean;
import com.fatchao.rxrequest.constants.Constant;
import com.fatchao.rxrequest.utils.HintUtils;
import com.fatchao.rxrequest.utils.NetworkManager;
import com.fatchao.rxrequest.utils.PrefUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.http.FieldMap;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


public class ProxyHandler implements InvocationHandler {
    private final static int REFRESH_TOKEN_VALID_TIME = 30;
    private static long tokenChangedTime = 0;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;
    private boolean isCache;

    private Object mProxyObject;
    private HashMap<String, String> hashMap = new HashMap<>();
    int cacheTime = 10;
    public CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(cacheTime, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
            .build();

    public ProxyHandler(Object proxyObject, boolean isCache) {
        this.isCache = isCache;
        hashMap.put("account", PrefUtils.getString(BaseApplication.getContext(), Constant.USER_PHONE, ""));
        hashMap.put("client", "app");
        hashMap.put("password", PrefUtils.getString(BaseApplication.getContext(), Constant.USER_PASSWORD, ""));
        mProxyObject = proxyObject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(null).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                if (isCache) {
                    try {
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (NetworkManager.isNetworkAvailable(BaseApplication.getContext())) {
                        try {
                            try {
                                if (mIsTokenNeedRefresh) {
                                    Log.d("_t------->", PrefUtils.getString(BaseApplication.getContext(), Constant.USER_TOKEN, ""));
                                    updateMethodToken(method, args);
                                }
                                return (Observable<?>) method.invoke(mProxyObject, args);

                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return Observable.error(new NetworkErrorException());
                    }

                }

                return null;
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            int code = httpException.code();
                            if (code == 401) {
                                if (PrefUtils.getBoolean(BaseApplication.getContext(), "is402", true)) {
                                    return goToLogin(throwable);
                                }
                                return refreshTokenWhenTokenInvalid();
                            } else if (code == 402) {
                                //异地登陆
                                return goToLogin(throwable);
                            } else if (code == 502) {
                                HintUtils.showToast(BaseApplication.getContext(), "服务端正在重启");
                                return Observable.error(throwable);

                            } else if (code == 504) {
                                HintUtils.showToast(BaseApplication.getContext(), "缓存读取失败");
                                return Observable.error(throwable);
                            } else {
//                                HintUtils.showToast(BaseApplication.getContext(), "网络异常，请检查你的网络设置");
                                return Observable.error(throwable);
                            }
                        } else if (throwable instanceof NetworkErrorException) {
                            HintUtils.showToast(BaseApplication.getContext(), "网络异常");
                            return Observable.error(throwable);
                        }
                        return Observable.error(throwable);

                    }
                });
            }
        });
    }

    @NonNull
    private Observable<?> goToLogin(Throwable throwable) {
        PrefUtils.setBoolean(BaseApplication.getContext(), "is402", true);
        HintUtils.showToast(BaseApplication.getContext(), "您的账号已经在另外一台设备上登陆，如果不是本人操作，请尽快修改密码");
        restartApplication();
        return Observable.error(throwable);
    }


    private void restartApplication() {
        //TODO 处理异地登陆逻辑
//        Intent i = new Intent();
//        i.setClass(BaseApplication.getContext(), LoginActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        BaseApplication.getContext().startActivity(i);
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                return Observable.just(true);
            } else {
                RxRequest.getInstance().getProxy(false).login(hashMap)
                        .subscribe(new Subscriber<LoginBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mRefreshTokenError = e;
                            }

                            @Override
                            public void onNext(LoginBean model) {
                                if (model != null) {
                                    tokenChangedTime = new Date().getTime();
                                    PrefUtils.setString(BaseApplication.getContext(), Constant.USER_TOKEN, model.getData().getToken());
                                    Log.d("token-----put------>", PrefUtils.getString(BaseApplication.getContext(), Constant.USER_TOKEN, ""));
                                    mIsTokenNeedRefresh = true;
                                }
                            }
                        });
            }
            if (mRefreshTokenError != null) {
                return Observable.error(mRefreshTokenError);
            } else {
                return Observable.just(true);
            }

        }
    }

    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(PrefUtils.getString(BaseApplication.getContext(), Constant.USER_TOKEN, ""))) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof FieldMap) {
                            HashMap<String, String> hashMap = (HashMap<String, String>) args[i];
                            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                                if (stringStringEntry.getKey().equals("_t")) {
                                    hashMap.put("_t", PrefUtils.getString(BaseApplication.getContext(), Constant.USER_TOKEN, ""));
                                }
                            }
                            args[i] = hashMap;
//
                        }
                        //Get请求遍历方式
//                        if (annotation instanceof Query) {
//                            if (TOKEN.equals(((Query) annotation).value())) {
//                                args[i] = GlobalToken.getToken();
//                            }
//                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }
}
