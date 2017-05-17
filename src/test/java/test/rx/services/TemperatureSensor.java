package test.rx.services;


import rx.Observable;

import java.util.Random;

import static test.rx.tools.Threads.sleep;


public class TemperatureSensor {

    private Random random = new Random();
    private int temperature = 20;

    public Observable<Integer> getTemperatureStream() {
        return Observable
                .create(subscriber -> {
                    try {
                        while (true) {
                            if (subscriber.isUnsubscribed()) {
                                break;
                            } else {
                                switch (random.nextInt(10)) {
                                    case 0: temperature++; break;
                                    case 1: temperature--; break;
                                }
                                subscriber.onNext(temperature);
                            }
                            sleep(1000);
                        }
                    } catch (Exception e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }
                });
    }

}
