package test.rx;

import org.junit.Test;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import test.rx.tools.PrintingObserver;

import static test.rx.tools.Log.print;


public class _11_Subjects {

    Observer<Object> subscriber = new PrintingObserver();


    @Test public void single() {

        Single
                .just(1)
                .subscribe(new SingleSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        print("onSuccess: " + value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        print("onError: " + error);
                    }
                });
    }


    // An AsyncSubject emits the last value emitted by the source Observable, and only after that source Observable completes. (If the source Observable does not emit any values, the AsyncSubject also completes without emitting any values.)
    @Test public void asyncSubject() {

        AsyncSubject<Integer> subject = AsyncSubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1"));
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2"));
        subject.onNext(3);
        subject.onCompleted();
    }


    // When an observer subscribes to a BehaviorSubject, it begins by emitting the item most recently emitted by the source Observable (or a seed/default value if none has yet been emitted) and then continues to emit any other items emitted later by the source Observable(s).
    @Test public void behaviorSubject() {

        BehaviorSubject<Integer> subject = BehaviorSubject.create(0); // Default value

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1"));
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2"));
        subject.onNext(3);
        subject.onCompleted();
    }


    // PublishSubject emits to an observer only those items that are emitted by the source Observable(s) subsequent to the time of the subscription.
    @Test public void publishSubject() {

        PublishSubject<Integer> subject = PublishSubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1"));
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2"));
        subject.onNext(3);
        subject.onCompleted();
    }


    // ReplaySubject emits to any observer all of the items that were emitted by the source Observable(s), regardless of when the observer subscribes.
    @Test public void replaySubject() {

        ReplaySubject<Integer> subject = ReplaySubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1"));
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2"));
        subject.onNext(3);
        subject.onCompleted();
    }
}
