package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;
import test.rx.tools.Threads;

import java.util.concurrent.TimeUnit;

public class SyntheticClient {

    public Observable<Integer> callService1() {
        return Observable
                .fromCallable(() -> {
                    Log.print("callService1");
                    Threads.sleep(1000);
                    return 1;
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<Integer> callService2() {
        return Observable
                .fromCallable(() -> {
                    Log.print("callService2");
                    Threads.sleep(2000);
                    return 1;
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<Integer> failingService() {
        return Observable
                .concat(
                        Observable.just(1, 2),
                        Observable.error(new Exception())
                );
    }

    public Observable<Long> fastSource() {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .take(3)
                .map(n -> n + 1);
    }

    public Observable<Long> slowSource() {
        return Observable
                .interval(2, TimeUnit.SECONDS)
                .take(3)
                .map(n -> n + 4);
    }
}
