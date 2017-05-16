package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import test.rx.services.AccountService;
import test.rx.services.TransactionService;
import test.rx.services.UserService;
import test.rx.tools.Log;
import test.rx.tools.PrintingObserver;
import test.rx.tools.Threads;

import java.util.concurrent.TimeUnit;

public class RealExample {

    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();

    Observer<Object> subscriber = new PrintingObserver();

    @Test
    public void test() {

        transactionService.getTransactionsObservable()
                .doOnNext(Log::print)
                .flatMap(transaction -> Observable
                        .zip(
                                userService.getUser(transaction.getUserId())
                                        .timeout(500, TimeUnit.MILLISECONDS)
                                        .retry(3)
                                        .onErrorReturn(throwable -> new UserService.UserDetailsResponse("mock", "owner")),
                                accountService.getAccount(transaction.getAccountId()),
                                (user, account) -> createTransactionAggregate(transaction, user, account)
                        )
                )
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        Threads.sleep(5000);
    }

    private TransactionAggregate createTransactionAggregate(TransactionService.Transaction transaction, UserService.UserDetailsResponse user, AccountService.AccountDetailsResponse account) {
        return new TransactionAggregate();
    }

    static class TransactionAggregate {
    }
}
