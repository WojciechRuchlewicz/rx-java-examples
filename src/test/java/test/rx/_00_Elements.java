package test.rx;


import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.Subscribers;
import test.rx.tools.Log;

import static test.rx.tools.Log.print;

public class _00_Elements {

    interface Observable<T> {
        Subscription subscribe(Observer<T> observer);
    }

    interface Observer<T> {
        void onNext(T t);
        void onCompleted();
        void onError(Throwable e);
    }

    interface Subscription {
        void unsubscribe();
        boolean isUnsubscribed();
    }
}
