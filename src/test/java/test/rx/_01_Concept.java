package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.Subscribers;
import test.rx.tools.Log;

import static test.rx.tools.Log.print;

public class _01_Concept {

    @Test
    public void syntax() throws Exception {

        // Create observable
        Observable<Integer> observable = Observable.just(1, 2, 3);

        // Create subscriber
        Subscriber<Integer> subscriber = Subscribers.create(Log::print);

        // Subscribe
        Subscription subscription = observable.subscribe(subscriber);

        // Unsubscribe
        subscription.unsubscribe();
    }


    @Test
    public void simpleSyntax() throws Exception {

        Observable
                .just(1, 2, 3)
                .subscribe(Log::print);
    }


    @Test
    public void observer() throws Exception {

        Observable
                .just(1, 2, 3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer o) {
                        print("onNext " + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("onError " + e);
                    }

                    @Override
                    public void onCompleted() {
                        print("onCompleted");
                    }
                });
    }


    @Test
    public void lambdaSubscriber() throws Exception {

        Observable
                .just(1, 2, 3)
                .subscribe(
                        n  -> print("onNext " + n),
                        e  -> print("onError " + e),
                        () -> print("onCompleted")
                );
    }
}
