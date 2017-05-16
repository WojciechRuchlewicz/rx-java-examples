package test.rx;


import org.junit.Test;
import rx.*;
import rx.observers.Subscribers;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import test.rx.services.RestService;
import test.rx.tools.Log;
import test.rx.tools.PrintingObserver;
import test.rx.tools.Threads;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;

public class _Prelection {

    private final Observer<Object> subscriber = new PrintingObserver();

    /******************************************************************************************************************
     * Syntax
     ******************************************************************************************************************/

    @Test
    public void syntax() throws Exception {

        // Create observable
        Observable<Integer> observable = Observable.just(1);

        // Create subscriber
        Subscriber<Integer> subscriber = Subscribers.create(Log::print);

        // Subscribe
        Subscription subscription = observable.subscribe(subscriber);

        // Unsubscribe
        subscription.unsubscribe();
    }

    // Uproszczenie, lambda
    @Test
    public void observer() throws Exception {

        Observable
                .just(1)
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
    public void single() {

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

    /******************************************************************************************************************
     * Creating
     ******************************************************************************************************************/

    @Test
    public void createFromScratch() throws Exception {

        Observable.create((Observable.OnSubscribe<Integer>) observer -> {
            try {
                if (!observer.isUnsubscribed()) {
                    for (int i = 0; i < 3; i++) {
                        observer.onNext(i);
                    }
                    observer.onCompleted();
                }
            } catch (Exception e) {
                observer.onError(e);
            }
        }).subscribe(subscriber);
    }


    @Test
    public void createByOperators() {

        Observable
                .never()
                //.empty()
                //.error(new Exception())
                //.just(1)
                //.just(1, 2, 3)
                //.range(1, 3)
                //.from(new Integer[] { 1, 2, 3 })
                //.from(Arrays.asList(1, 2, 3))
                //.from(CompletableFuture.completedFuture(1))
                //.fromCallable(() -> 1)
                .subscribe(subscriber);
    }

    /******************************************************************************************************************
     * Filtering
     ******************************************************************************************************************/

    @Test
    public void filterByOperators() {

        Observable
                .range(1, 50)
                //.ignoreElements()
                //.first()
                //.last()
                //.take(3)
                //.takeLast(3)
                //.skip(47)
                //.filter(i -> i % 10 == 0)
                //.elementAtOrDefault(55, -1)
                .subscribe(subscriber);
    }

    @Test
    public void filterDistinct() {
        Observable.just(1, 2, 2, 3, 3, 3, 2, 2, 2)
                //.distinct()
                //.distinctUntilChanged()
                .subscribe(subscriber);
    }

    /******************************************************************************************************************
     * Transforming
     ******************************************************************************************************************/

    @Test
    public void transform() {

        Observable.just(1, 2)
                //.map(n -> "a" + n)
                //.flatMap(n -> Observable.just("a" + n, "b" + n))
                //.flatMapIterable(n -> Arrays.asList("a" + n, "b" + n))
                .subscribe(subscriber);
    }

    /******************************************************************************************************************
     * Aggregating
     ******************************************************************************************************************/

    @Test
    public void aggregationOperators() {
        Observable
                .just(1, 2, 3)
                //.count()
                //.reduce((a, b) -> a + b)
                //.collect(ArrayList::new, ArrayList::add)
                //.toList()
                //.toMap(n -> n % 2)
                //.toMultimap(n -> n % 2)
                //.buffer(2)
                //.window(2)
                //.groupBy(n -> n % 2)
                .subscribe(subscriber);
    }

    /******************************************************************************************************************
     * Combining
     ******************************************************************************************************************/

    @Test
    public void combineOperators() {
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

    /******************************************************************************************************************
     * Conditional
     ******************************************************************************************************************/

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


    @Test
    public void defaultIfEmpty() {

        Observable
                .empty()
                .defaultIfEmpty(1)
                .subscribe(subscriber);
    }


    @Test
    public void skipWhile() {

        Observable
                .just(1, 2, 3, 4)
                .skipWhile(n -> n < 3)
                .subscribe(subscriber);
    }


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
    public void assertions() {

        Observable
                .just(1, 2, 3)
                //.isEmpty()
                //.contains(2)
                //.all(n -> n < 10)
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

    /******************************************************************************************************************
     * Utility
     ******************************************************************************************************************/

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

    /******************************************************************************************************************
     * Threading
     ******************************************************************************************************************/

    RestService restService = new RestService();

    @Test
    public void multithreading() {
        Observable
                .fromCallable(() -> { print("Observable"); return 1; })
                //.subscribeOn(Schedulers.computation())
                .filter(n -> { print("Operator 1"); return true; })
                .filter(n -> { print("Operator 2"); return true; })
                //.observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        print("Done");
        sleep(1000);
    }


    @Test
    public void multithreading_subscribeOn_4() {
        restService.callService1()
                .zipWith(restService.callService2(), (a, b) -> a + b)
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        print("Done");
        sleep(2000);
    }

    private void schedulers() {

        Schedulers.newThread(); // Creates and returns a Scheduler that creates a new Thread for each unit of work.
        Schedulers.io(); // Creates and returns a Scheduler intended for IO-bound work. The implementation is backed by an Executor thread-pool that will grow as needed. This can be used for asynchronously performing blocking IO. Schedulers.io( ) by default is a CachedThreadScheduler, which is something like a new thread scheduler with thread caching.
        Schedulers.computation(); // Creates and returns a Scheduler intended for computational work. This can be used for event-loops, processing callbacks and other computational work. the number of threads, by default, is equal to the number of processors.
        Schedulers.immediate(); // Creates and returns a Scheduler that executes work immediately on the current thread.
        Schedulers.trampoline(); // Creates and returns a Scheduler that queues work on the current thread to be executed after the current work completes.
        Schedulers.from(Executors.newFixedThreadPool(4)); // uses the specified Executor as a Scheduler
        Schedulers.test(); // Creates and returns a TestScheduler, which is useful for debugging. It allows you to test schedules of events by manually advancing the clock at whatever pace you choose.
    }

    /******************************************************************************************************************
     * Error handling
     ******************************************************************************************************************/

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

    /******************************************************************************************************************
     * Subjects
     ******************************************************************************************************************/

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

    /******************************************************************************************************************
     * Unit Tests
     ******************************************************************************************************************/

    @Test public void testSubscriber() {

        TestSubscriber<Long> testSubscriber = TestSubscriber.create();

        Observable
                .interval(1, TimeUnit.SECONDS)
                .take(2)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(0L, 1L);
    }

    @Test public void testScheduler() {

        TestScheduler testScheduler = Schedulers.test();

        TestSubscriber<Long> testSubscriber = TestSubscriber.create();

        Observable
                .interval(1, TimeUnit.SECONDS, testScheduler)
                .take(2)
                .subscribe(testSubscriber);

        testScheduler.advanceTimeTo(1999, TimeUnit.MILLISECONDS);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(0L);
        testSubscriber.assertNotCompleted();

        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(0L, 1L);
        testSubscriber.assertCompleted();
    }
}
