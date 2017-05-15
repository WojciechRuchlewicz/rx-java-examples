package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;

public class _02_Creating_Observables {

    Observer<Object> subscriber = new PrintingObserver();


    // Create an Observable from scratch by means of a function
    @Test
    public void create() throws Exception {

        Observable.create((Observable.OnSubscribe<Integer>) observer -> {
            try {
                if (!observer.isUnsubscribed()) {
                    for (int i = 0; i < 3; i++) {
                        observer.onNext(i);
                    }
                    observer.onCompleted();
                }
            } catch (Exception e) {
                observer.onError(e);
            }
        }).subscribe(subscriber);
    }


    @Test
    public void never() {

        Observable
                .never()
                .subscribe(subscriber);
    }


    @Test
    public void empty() {

        Observable
                .empty()
                .subscribe(subscriber);
    }


    @Test
    public void error() {
        Observable.error(new Exception())
                .subscribe(subscriber);
    }


    @Test
    public void just() throws Exception {

        Observable
                .just("a", "b", "c")
                .subscribe(subscriber);
    }


    @Test
    public void range() throws Exception {

        Observable
                .range(10, 3)
                .subscribe(subscriber);
    }


    @Test
    public void fromArray() throws Exception {

        Observable
                .from(new String[] { "a", "b", "c" })
                .subscribe(subscriber);
    }


    @Test
    public void fromIterable() throws Exception {

        Observable
                .from(Arrays.asList("a", "b", "c"))
                .subscribe(subscriber);
    }


    @Test
    public void fromCompletableFuture() throws Exception {

        Observable
                .from(CompletableFuture.completedFuture("result"))
                .subscribe(subscriber);
    }


    @Test
    public void fromCallable() throws Exception {

        Observable
                .fromCallable(() -> "result")
                .subscribe(subscriber);
    }


    // Do not create the Observable until a Subscriber subscribes; create a fresh Observable on each subscription
    @Test
    public void defer() throws Exception {

        Observable
                .defer(() -> Observable.just("result"))
                .subscribe(subscriber);
    }


    // Interval operates by default on the computation Scheduler
    @Test
    public void interval() throws Exception {

        Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe(subscriber);

        sleep(5000);
    }


    // It emits one item after a specified delay
    @Test
    public void timer() throws Exception {

        print("Start");

        Observable
                .timer(2, TimeUnit.SECONDS)
                .subscribe(subscriber);

        sleep(5000);
    }
}
