package com.example.basemoudle.util.rxjava;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chenlei2 on 2017/4/18 0018.
 */

public abstract class SimpleObersever<T> implements Observer<T>{

    private static  final String TAG  = SimpleObersever.class.getSimpleName();


    @Override
    public void onSubscribe(Disposable d) {
    }



    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
    }
}
