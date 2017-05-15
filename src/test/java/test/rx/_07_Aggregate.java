package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

import java.util.ArrayList;


public class _07_Aggregate {

    Observer<Object> subscriber = new PrintingObserver();


    @Test
    public void count() {
        Observable
                .just("a", "b", "c")
                .count()
                .subscribe(subscriber);
    }

    @Test
    public void reduce() {
        Observable
                .just(1, 2, 3)
                .reduce((s, s2) -> s + s2)
                .subscribe(subscriber);
    }

    // Collects items emitted by the source Observable into a single mutable data structure and returns an Observable that emits this structure.
    @Test
    public void collect() {
        Observable
                .just(1, 2, 3)
                .collect(ArrayList::new, ArrayList::add)
                .subscribe(subscriber);
    }

    // Collects items emitted by the source Observable into a single mutable data structure and returns an Observable that emits this structure.
    @Test
    public void toList() {
        Observable
                .just(1, 2, 3)
                .toList()
                .subscribe(subscriber);
    }

    //
    @Test
    public void toMap() {
        Observable
                .just("A1", "A2", "B1")
                .toMap(n -> n.charAt(0))
                .subscribe(subscriber);
    }

    @Test
    public void toMultimap() {
        Observable
                .just("A1", "A2", "B1")
                .toMultimap(n -> n.charAt(0))
                .subscribe(subscriber);
    }
}
