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

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;

public class Examples {

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
    public void create() throws Exception {

        Observable.create(observer -> {
            if (!observer.isUnsubscribed()) {
                try {
                    for (int i = 0; i < 3; i++) {
                        observer.onNext(i);
                    }
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
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
    public void filter() {

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
    public void distinct() {
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
    public void aggregate() {
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
    public void combine() {
        Observable
                .just(1, 2, 3)
                //.startWith(-1, 0)
                //.mergeWith(Observable.just(4, 5, 6))
                //.concatWith(Observable.just(4, 5, 6))
                //.ambWith(Observable.just(4, 5, 6))
                //.zipWith(Observable.just("a", "b"), (l, n) -> l + n)
                //.withLatestFrom(Observable.just("a", "b"), (l, n) -> l + n)
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

        sleep(4000);
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

        sleep(7000);
    }


    @Test
    public void assertions() {

        Observable
                .just(1, 2, 3)
                //.isEmpty()
                //.contains(2)
                //.all(n -> n < 10)
                .subscribe(subscriber);
    }


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
                //.cache();

        observable.subscribe(subscriber);
        observable.subscribe(subscriber);
    }


    @Test
    public void events() {

        Observable
                .just(1, 2)
                .concatWith(Observable.error(new Exception())) // 1, 2, Error

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

    /******************************************************************************************************************
     * Time
     ******************************************************************************************************************/

    @Test
    public void delay() {

        print("start");

        Observable
                .just(1, 2, 3)
                .delay(1, TimeUnit.SECONDS)
                .subscribe(subscriber);

        sleep(2000);
    }

    /******************************************************************************************************************
     * Threading
     ******************************************************************************************************************/

    private RestService restService = new RestService();

    @Test
    public void multithreading1() {
        Observable
                .fromCallable(() -> { print("create"); return 1; })
                .filter(n -> { print("filter"); return true; })
                .map(n -> { print("map"); return n; })
                //.subscribeOn(Schedulers.computation())
                //.observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        print("Done");
        sleep(1000);
    }


    @Test
    public void multithreading2() {
        restService.callService1()
                .zipWith(restService.callService2(), (a, b) -> a + b)
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        print("Done");
        sleep(2000);
    }

    private void schedulers() {

        Schedulers.newThread();
        Schedulers.io();
        Schedulers.computation();
        Schedulers.immediate();
        Schedulers.trampoline();
        Schedulers.from(Executors.newFixedThreadPool(4));
        Schedulers.test();
    }

    /******************************************************************************************************************
     * Error handling
     ******************************************************************************************************************/

    @Test public void noErrorHandling() {

        Observable
                .error(new Exception())
                .subscribe(n  -> print("onNext " + n));
    }


    @Test public void defaultErrorHandling1() {

        Observable
                .error(new Exception())
                .subscribe(
                        n  -> print("onNext " + n),
                        e  -> print("onError " + e)
                );
    }


    @Test public void defaultErrorHandling2() {

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

    // Recovery Mechanisms

    @Test public void onErrorReturn() {

        Observable.just(1, 2)
                .concatWith(Observable.error(new Exception())) // 1, 2, Error
                .onErrorReturn(throwable -> -1)
                .subscribe(subscriber);
    }

    @Test public void onErrorResumeNext() {

        Observable.just(1, 2)
                .concatWith(Observable.error(new Exception())) // 1, 2, Error
                .onErrorResumeNext(Observable.just(3, 4)) // backup service
                .subscribe(subscriber);
    }

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


    @Test public void retryWhen() {

        Observable<Long> retryObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5);

        Observable
                .fromCallable(() -> {
                    print("Error thrown");
                    throw new Exception();
                })
                .retryWhen(errorStream -> retryObservable)
                .subscribe(subscriber);

        sleep(7000);
    }

    /******************************************************************************************************************
     * Subjects
     ******************************************************************************************************************/

    @Test public void asyncSubject() {

        AsyncSubject<Integer> subject = AsyncSubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1")); // 3
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2")); // 3
        subject.onNext(3);
        subject.onCompleted();
    }


    @Test public void behaviorSubject() {

        BehaviorSubject<Integer> subject = BehaviorSubject.create(0); // Default value

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1")); // 1, 2, 3
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2")); // 2, 3
        subject.onNext(3);
        subject.onCompleted();
    }


    @Test public void publishSubject() {

        PublishSubject<Integer> subject = PublishSubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1")); // 2, 3
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2")); // 3
        subject.onNext(3);
        subject.onCompleted();
    }


    @Test public void replaySubject() {

        ReplaySubject<Integer> subject = ReplaySubject.create();

        subject.onNext(1);
        subject.subscribe(new PrintingObserver("Subscriber 1")); // 1, 2, 3
        subject.onNext(2);
        subject.subscribe(new PrintingObserver("Subscriber 2")); // 1, 2, 3
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
