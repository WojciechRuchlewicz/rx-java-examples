package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

public class _05_Combining {

    Observer<Object> subscriber = new PrintingObserver();

    @Test
    public void combiningOperators() {
        Observable
                .just(1, 2, 3)
                //.startWith(-1, 0)
                //.mergeWith(Observable.just(4, 5, 6))
                //.concatWith(Observable.just(4, 5, 6))
                //.zipWith(Observable.just("a", "b"), (l, n) -> l + n)
                //.withLatestFrom(Observable.just("a", "b"), (l, n) -> l + n)
                //.ambWith() // TODO
                .subscribe(subscriber);
    }
}
