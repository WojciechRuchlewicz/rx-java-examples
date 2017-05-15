package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

public class _05_Combining {

    Observer<Object> subscriber = new PrintingObserver();


    // combine multiple Observables into one
    @Test
    public void merge() {
        Observable
                .merge(
                        Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6)
                )
                .subscribe(subscriber);
    }


    // combine multiple Observables into one
    @Test
    public void concat() {
        Observable
                .concat(
                        Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6)
                )
                .subscribe(subscriber);
    }


    // emit a specified sequence of items before beginning to emit the items from the Observable
    @Test
    public void startWith() {

        Observable
                .just(2, 3, 4)
                .startWith(0, 1)
                .subscribe(subscriber);
    }


    // combine sets of items emitted by two or more Observables together via a specified function and emit items based on the results of this function
    @Test
    public void zip() {
        Observable
                .zip(
                        Observable.just("a", "b", "c"),
                        Observable.just(1, 2, 3, 4),
                        (l, n) -> l + n
                )
                .subscribe(subscriber);
    }


    // when an item is emitted by either of two Observables, combine the latest item emitted by each Observable via a specified function and emit items based on the results of this function
    @Test
    public void combineLatest() {

        Observable
                .combineLatest(
                        Observable.just("a", "b", "c"),
                        Observable.just(1, 2, 3, 4),
                        (l, n) -> l + n
                )
                .subscribe(subscriber);
    }
}
