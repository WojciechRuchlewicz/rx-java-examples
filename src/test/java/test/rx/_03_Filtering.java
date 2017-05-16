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
    public void filterOperators() {

        Observable
                .range(1, 50)
                //.ignoreElements()
                //.first()
                //.last()
                //.take(3)
                //.takeLast(3)
                //.skip(47)
                //.filter(i -> i % 10 == 0)
                //.elementAtOrDefault(55, -1)
                .subscribe(subscriber);
    }

    @Test
    public void distinct() {
        Observable.just(1, 2, 2, 3, 3, 3, 2, 2, 2)
                //.distinct()
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
}
