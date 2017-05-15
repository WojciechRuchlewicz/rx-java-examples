package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

import java.util.concurrent.TimeUnit;

import static test.rx.tools.Threads.sleep;

public class _03_Filtering {

    Observer<Object> subscriber = new PrintingObserver();


    @Test
    public void filter() {

        Observable
                .range(1, 50)
                .filter(i -> i % 10 == 0)
                .subscribe(subscriber);
    }


    @Test
    public void first() {

        Observable
                .range(1, 50)
                .first()
                .subscribe(subscriber);
    }


    @Test
    public void last() {

        Observable
                .range(1, 50)
                .last()
                .subscribe(subscriber);
    }


    @Test
    public void take() {

        Observable
                .range(1, 50)
                .take(3)
                .subscribe(subscriber);
    }


    @Test
    public void takeLast() {

        Observable
                .range(1, 50)
                .takeLast(3)
                .subscribe(subscriber);
    }


    @Test
    public void skip() {
        Observable
                .range(1, 3)
                .skip(1)
                .subscribe(subscriber);
    }


    @Test
    public void elementAtOrDefault() {
        Observable
                .just(1, 2, 3)
                .elementAtOrDefault(4, 5)
                .subscribe(subscriber);
    }


    @Test
    public void distinct() {

        Observable.just(1, 2, 3)
                .repeat(5)
                //.distinct()
                .subscribe(subscriber);
    }


    @Test
    public void distinctUntilChanged() {

        Observable.just(1, 2, 2, 3, 3, 3, 2, 2, 2)
                //.distinctUntilChanged()
                .subscribe(subscriber);
    }


    @Test
    public void takeLastBuffer() {

        Observable.just(1, 2, 3)
                .takeLastBuffer(2)
                .subscribe(subscriber);
    }


    @Test
    public void throttleFirst() {

        Observable.interval(1, TimeUnit.SECONDS)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(subscriber);

        sleep(10000);
    }


    @Test
    public void ignoreElements() {

        Observable.just(1, 2, 3)
                .ignoreElements()
                .subscribe(subscriber);
    }
}
