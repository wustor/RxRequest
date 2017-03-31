package com.fatchao.rxrequest.request;

import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
@SuppressWarnings("unchecked")
public class RequestManager {
    private volatile static RequestManager INSTANCE;

    //构造方法私有
    private RequestManager() {
    }

    //获取单例
    public static RequestManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RequestManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RequestManager();
                }
            }
        }
        return INSTANCE;
    }

    public void sendRequest(Observable observable, RxSubscriber subscriber) {
        if (observable != null)
            observable.compose(subscriber.getActivity().bindToLifecycle())//绑定生命周期
                    .compose(subscriber.getActivity().bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())//操作线程
                    .unsubscribeOn(Schedulers.io())//解绑线程
                    .observeOn(AndroidSchedulers.mainThread())//回调线程
                    .subscribe(subscriber);

    }
}
