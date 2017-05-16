package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

import java.util.concurrent.TimeUnit;

import static test.rx.tools.Threads.sleep;

public class _02_Creating_Observables {

    Observer<Object> subscriber = new PrintingObserver();


    // Observable from scratch by means of a function
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
    public void operators() {

        Observable
                .never()
                //.empty()
                //.error(new Exception())
                //.just(1)
                //.just(1, 2, 3)
                //.range(1, 3)
                //.from(new Integer[] { 1, 2, 3 })
                //.from(Arrays.asList(1, 2, 3))
                //.from(CompletableFuture.completedFuture(1))
                //.fromCallable(() -> 1)
                .subscribe(subscriber);
    }

    @Test
    public void time() throws Exception {

        Observable
                .interval(1, TimeUnit.SECONDS)
                //.timer(2, TimeUnit.SECONDS) // It emits one item after a specified delay
                .subscribe(subscriber);

        sleep(5000);
    }
}
