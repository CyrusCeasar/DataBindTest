package com.example.basemoudle.util.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenlei2 on 2017/4/18 0018.
 */

public class RxJavaUtil {

    public static void asyncRequest(ObservableOnSubscribe onSubscribe, Observer<?> observer){
        Observable.create(onSubscribe).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
