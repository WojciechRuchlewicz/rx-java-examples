package test.rx.services;


import rx.Observable;

import java.util.Random;

import static test.rx.tools.Threads.sleep;


public class TemperatureService {

    private Random random = new Random();

    public Observable<Integer> getTemperatureObservable() {
        return Observable
                .create(subscriber -> {
                    try {
                        while (true) {
                            if (subscriber.isUnsubscribed()) {
                                break;
                            } else {
                                subscriber.onNext(random.nextInt(3) + 20);
                            }
                            sleep(1);
                        }
                    } catch (Exception e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }
                });
    }

}
