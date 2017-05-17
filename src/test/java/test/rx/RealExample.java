package test.rx;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import test.rx.services.AccountClient;
import test.rx.services.TransactionService;
import test.rx.services.TransactionService.TransactionState;
import test.rx.tools.PrintingObserver;
import test.rx.tools.Threads;

import java.util.concurrent.TimeUnit;

public class RealExample {

    private final TransactionService transactionService = new TransactionService();
    private final AccountClient accountService = new AccountClient(1000);
    private final AccountClient accountBackupService = new AccountClient(250);

    private final Observer<Object> subscriber = new PrintingObserver();

    @Test
    public void test() {

        transactionService.getTransactionsUpdates()
                .filter(event -> event.getState() == TransactionState.COMPLETED)
                .flatMap(event -> Observable
                        .zip(
                                transactionService.getTransactionDetails(event.getTransactionId())
                                        .timeout(500, TimeUnit.MILLISECONDS)
                                        .retry(3),
                                accountService.getAccountBalance(event.getAccountNo())
                                        .timeout(500, TimeUnit.MILLISECONDS)
                                        .onErrorResumeNext(accountBackupService.getAccountBalance(event.getAccountNo())),
                                (transaction, account) -> "Transaction " + event.getTransactionId() + " completed. Price: " + transaction.getPrice() + ", current balance: " + account.getBalance()
                        )
                )
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);

        Threads.sleep(5000);
    }

}
