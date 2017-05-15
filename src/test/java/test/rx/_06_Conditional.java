package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;
import test.rx.tools.Threads;

import java.util.concurrent.TimeUnit;


public class _06_Conditional {

    Observer<Object> subscriber = new PrintingObserver();


    // given two or more source Observables, emits all of the items from the first of these Observables to emit an item
    @Test
    public void amb() {

        Observable
                .amb(
                        Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS),
                        Observable.just(4, 5, 6).delay(1, TimeUnit.SECONDS),
                        Observable.just(7, 8, 9).delay(2, TimeUnit.SECONDS)
                )
                .subscribe(subscriber);

        Threads.sleep(2000);
    }


    // emit items from the source Observable, or emit a default item if the source Observable completes after emitting no items
    @Test
    public void defaultIfEmpty() {

        Observable
                .empty()
                .defaultIfEmpty(1)
                .subscribe(subscriber);
    }


    // discard items emitted by an Observable until a specified condition is false, then emit the remainder
    @Test
    public void skipWhile() {

        Observable
                .just(1, 2, 3, 4)
                .skipWhile(n -> n < 3)
                .subscribe(subscriber);
    }


    // emits the items from the source Observable until a second Observable emits an item or issues a notification
    @Test
    public void skipUntil() {

        Observable
                .interval(1, TimeUnit.SECONDS)
                .skipUntil(Observable.just(1).delay(3, TimeUnit.SECONDS))
                .subscribe(subscriber);

        Threads.sleep(7000);
    }


    // determine whether an Observable emits a particular item or not
    @Test
    public void contains() {

        Observable
                .just(1, 2, 3)
                .contains(2)
                .subscribe(subscriber);
    }


    // determine whether all items emitted by an Observable meet some criteria
    @Test
    public void all() {

        Observable
                .just(1, 2, 3)
                .all(n -> n < 10)
                .subscribe(subscriber);
    }


    // determine whether an Observable emits any items or not
    @Test
    public void isEmpty() {

        Observable
                .just(1, 2, 3)
                .isEmpty()
                .subscribe(subscriber);
    }


    // test the equality of the sequences emitted by two Observables
    @Test
    public void sequenceEqual() {

        Observable
                .sequenceEqual(
                        Observable.just(1, 2, 3),
                        Observable.just(1, 2, 3)
                )
                .subscribe(subscriber);
    }
}
