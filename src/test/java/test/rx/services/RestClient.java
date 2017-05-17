package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;
import test.rx.tools.Threads;

public class RestClient {

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
}
