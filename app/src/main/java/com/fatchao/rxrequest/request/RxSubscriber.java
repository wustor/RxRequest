package com.fatchao.rxrequest.request;

import com.fatchao.rxrequest.callback.Callback;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;

import rx.Subscriber;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
@SuppressWarnings("unchecked")
public class RxSubscriber<T> extends Subscriber<T> {
    private SoftReference<Callback> rxListener;
    private SoftReference<RxAppCompatActivity> mActivity;

    public RxSubscriber(RxAppCompatActivity rxAppCompatActivity, Callback<T> listener) {
        this.mActivity = new SoftReference(rxAppCompatActivity);
        this.rxListener = new SoftReference(listener);
    }

    @Override
    public void onStart() {
        //TODO 可是做一些初始化操作,比如说谈一个对话框或者进度条
    }

    @Override
    public void onNext(T t) {
        if (rxListener.get() != null) {
            rxListener.get().onSuccess(t);
        }
    }

    @Override
    public void onCompleted() {
        //TODO 请求完成时走此方法

    }

    @Override
    public void onError(Throwable e) {
        // TODO 请求发生错误时走此方法
        if (rxListener.get() != null) {
            rxListener.get().onError(e);
        }
    }


    public RxAppCompatActivity getActivity() {
        return mActivity.get();
    }

}