package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import test.rx.services.RestService;
import test.rx.tools.PrintingObserver;

import java.util.concurrent.Executors;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;


public class _09_Threading {

    Observer<Object> subscriber = new PrintingObserver();
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
}
