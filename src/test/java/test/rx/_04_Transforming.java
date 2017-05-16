package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import test.rx.tools.PrintingObserver;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;

public class _04_Transforming {

    Observer<Object> subscriber = new PrintingObserver();


    @Test
    public void cast() {

        Observable
                .just((Object) 1)
                .cast(Integer.class)
                .filter(i -> i == 1)
                .subscribe(subscriber);
    }


    // Transform the items emitted by an Observable by applying a function to each of them
    @Test
    public void map() {

        Observable.just(1, 2, 3)
                .map(n -> "A" + n)
                .subscribe(subscriber);
    }


    // Transform the items emitted by an Observable into Observables (or Iterables), then flatten this into a single Observable
    @Test
    public void flatMap() {

        Observable.just(1, 2, 3)
                .flatMap(n -> Observable.just("a" + n, "b" + n))
                .subscribe(subscriber);
    }

    // Merge
    @Test
    public void flatMap2() {

        Observable.just("a", "b")
                .flatMap(n -> Observable
                        .interval(1, TimeUnit.SECONDS)
                        .take(2)
                        .map(i -> n + i)
                )
                .subscribe(subscriber);

        sleep(5000);
    }


    // Concat
    @Test
    public void concatMap() {

        Observable.just("a", "b")
                .concatMap(n -> Observable
                        .interval(1, TimeUnit.SECONDS)
                        .take(2)
                        .map(i -> n + i)
                )
                .subscribe(subscriber);

        sleep(5000);
    }


    @Test
    public void flatMapIterable() {

        Observable.just("a", "b")
                .flatMapIterable(n -> Arrays.asList(n + 1, n + 2))
                .subscribe(subscriber);
    }


    // Apply a function to each item emitted by an Observable, sequentially, and emit each successive value
    @Test
    public void scan() {

        Observable.just("a", "b", "c")
                .scan((n1, n2) -> n1 + n2)
                .subscribe(subscriber);
    }


    // Apply a function to each item emitted by an Observable, sequentially, and emit each successive value
    @Test
    public void groupBy() {

        Observable.just("a1", "a2", "b1", "b2")
                .groupBy(n -> n.charAt(0))
                .subscribe(n -> {
                    print("Group " + n.getKey());
                    n.subscribe(subscriber);
                });
    }


    // Periodically gather items from an Observable into bundles and emit these bundles rather than emitting the items one at a time
    @Test
    public void buffer() {

        Observable.just(1, 2, 3, 4, 5)
                .buffer(2)
                .subscribe(subscriber);
    }


    // Periodically subdivide items from an Observable into Observable windows and emit these windows rather than emitting the items one at a time
    @Test
    public void window() {

        Observable.just(1, 2, 3, 4)
                .window(2)
                .subscribe(subscriber);
    }
}
