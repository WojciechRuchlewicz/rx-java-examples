package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;

import static test.rx.tools.Log.print;
import static test.rx.tools.Threads.sleep;

public class AccountClient {

    private final int callDuration;

    public AccountClient(int callDuration) {
        this.callDuration = callDuration;
    }

    public Observable<AccountBalance> getAccountBalance(String accountNo) {
        return Observable
                .fromCallable(() -> {
                    print("getAccountBalance START");
                    sleep(callDuration);
                    AccountBalance balance = new AccountBalance(1000);
                    print("getAccountBalance END");
                    return balance;
                })
                .subscribeOn(Schedulers.io());
    }

    public static class AccountBalance {

        private final double balance;

        AccountBalance(double balance) {
            this.balance = balance;
        }

        public double getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "AccountBalance{" +
                    "balance=" + balance +
                    '}';
        }
    }
}
