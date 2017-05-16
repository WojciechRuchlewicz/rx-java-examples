package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;
import test.rx.tools.Threads;

import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;


public class _08_Utility {

    Observer<Object> subscriber = new PrintingObserver();


    @Test
    public void cached() {

        Observable<String> observable = Observable
                .fromCallable(() -> {
                    print("Create");
                    return "result";
                });
                //cache();

        observable.subscribe(subscriber);
        observable.subscribe(subscriber);
    }


    @Test
    public void events() {

        Observable
                .concat(
                        // 1, 2, Error
                        Observable.just(1, 2),
                        Observable.error(new Exception())
                )
                .doOnNext(n -> print("doOnNext " + n))
                .doOnEach(n -> print("doOnEach " + n))
                .doOnCompleted(() -> print("doOnCompleted"))
                .doOnError(e -> print("doOnError " + e))
                .doOnTerminate(() -> print("doOnTerminate"))
                .doOnSubscribe(() -> print("doOnSubscribe"))
                .doOnUnsubscribe(() -> print("doOnUnsubscribe"))
                .finallyDo(() -> print("finallyDo"))
                .subscribe(subscriber);
    }


    @Test
    public void timeInterval() {

        Observable
                .interval(1, TimeUnit.SECONDS)
                .timeInterval()
                .subscribe(subscriber);

        Threads.sleep(5000);
    }


    //  shift the emissions from an Observable forward in time by a specified amount
    @Test
    public void delay() {

        Observable
                .just(1, 2, 3)
                .delay(1, TimeUnit.SECONDS)
                .subscribe(subscriber);

        Threads.sleep(2000);
    }


    @Test
    public void materialize() {

        Observable
                .just(1, 2, 3)
                .materialize()
                .subscribe(subscriber);
    }
}
