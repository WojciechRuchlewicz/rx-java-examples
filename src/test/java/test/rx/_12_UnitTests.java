package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.concurrent.TimeUnit;


public class _12_UnitTests {

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
