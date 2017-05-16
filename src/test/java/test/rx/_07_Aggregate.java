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
                .just(1, 2, 3)
                //.count()
                //.reduce((a, b) -> a + b)
                //.collect(ArrayList::new, ArrayList::add)
                //.toList()
                //.toMap(n -> n % 2)
                //.toMultimap(n -> n % 2)
                .subscribe(subscriber);
    }

}
