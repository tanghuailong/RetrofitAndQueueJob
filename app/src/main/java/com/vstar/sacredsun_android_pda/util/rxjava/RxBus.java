package com.vstar.sacredsun_android_pda.util.rxjava;

/**
 * Created by tanghuailong on 2017/2/4.
 */

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxJava实现RxBus用来代替EventBus,用来减少依赖
 */
public class RxBus {

    private final Subject<Object,Object> bus;

    public RxBus() {
        //PublishSubject 是线程不安全的，SerializedSubject 把它转换为线程安全的
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    //单例模式
    public static class SingleHolder {
        private static final RxBus rxbus = new RxBus();
    }

    public static RxBus getDefault() {
       return SingleHolder.rxbus;
    }

    public void post(Object o) {
        bus.onNext(o);
    }
    //返回给观察者
    public <T> Observable<T> tObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
