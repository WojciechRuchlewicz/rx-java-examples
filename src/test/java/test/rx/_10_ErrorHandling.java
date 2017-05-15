package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import test.rx.tools.PrintingObserver;

import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;


public class _10_ErrorHandling {

    Observer<Object> subscriber = new PrintingObserver();


    @Test public void default_error_handling_0() {

        Observable
                .error(new Exception())
                .subscribe(n  -> print("onNext " + n)); // Brak obsługi błędów
    }


    @Test public void default_error_handling_1() {

        Observable
                .error(new Exception())
                .subscribe(
                        n  -> print("onNext " + n),
                        e  -> print("onError " + e) // Obsługa błędów
                );
    }


    @Test public void default_error_handling_2() {

        Observable
                .just(1, 2, 3)
                .map(text -> { throw new RuntimeException(); })
                .subscribe(
                        n  -> print("onNext " + n),
                        e  -> print("onError " + e)
                );
    }

    @Test public void doOnError() {

        Observable
                .error(new Exception())
                .doOnError(t -> print("doOnError: " + t))
                .subscribe(subscriber);
    }

    // Recovery Mechanisms:

    // closing the stream gracefully

    // Instructs an Observable to emit an item (returned by a specified function) rather than invoking
    // {@link Observer#onError onError} if it encounters an error.
    @Test public void onErrorReturn() {

        Observable
                .concat(
                        Observable.just(1, 2),
                        Observable.error(new Exception())
                )
                .onErrorReturn(throwable -> -1) // Handling error by returning a value indicating error
                .subscribe(subscriber);
    }


    /**
     * Using a backup
     *
     * Instructs an Observable to pass control to another Observable rather than invoking
     * {@link Observer#onError onError} if it encounters an error.
     */
    @Test public void onErrorResumeNext() {

        Observable
                .concat(
                        Observable.just(1, 2),
                        Observable.error(new Exception())
                )
                .onErrorResumeNext(Observable.just(3, 4)) // Resuming with a backup service
                .subscribe(subscriber);
    }


    // if a source Observable emits an error, resubscribe to it in the hopes that it will complete without error
    @Test public void retry() {

        Observable
                .fromCallable(() -> {
                    if (Math.random() > 0.2) {
                        print("Error thrown");
                        throw new Exception();
                    } else {
                        return 1;
                    }
                })
                .retry(5)
                .subscribe(subscriber);
    }


    /*
     * Returns an Observable that emits the same values as the source observable with the exception of an
     * {@code onError}. An {@code onError} notification from the source will result in the emission of a
     * {@link Throwable} item to the Observable provided as an argument to the {@code notificationHandler}
     * function. If that Observable calls {@code onComplete} or {@code onError} then {@code retry} will call
     * {@code onCompleted} or {@code onError} on the child subscription. Otherwise, this Observable will
     * resubscribe to the source Observable.
     */
    @Test public void retryWhen() {

        Observable<Long> retryObservable = Observable.interval(1, TimeUnit.SECONDS).take(5);

        Observable
                .fromCallable(() -> {
                    print("create: exception");
                    throw new Exception();
                })
                .retryWhen(errorStream -> retryObservable)
                .subscribe(subscriber);

        sleep(7000);
    }
}
