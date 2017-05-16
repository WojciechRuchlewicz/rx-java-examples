package test.rx.services;


import rx.Observable;

import java.util.Random;

import static test.rx.tools.Threads.sleep;


public class TransactionService {

    private Random random = new Random();

    public Observable<Transaction> getTransactionsObservable() {
        return Observable.just(new Transaction(1, 2, 3));
    }

    public static class Transaction {

        private final int id;
        private final int userId;
        private final int accountId;

        public Transaction(int id, int userId, int accountId) {
            this.id = id;
            this.userId = userId;
            this.accountId = accountId;
        }

        public int getId() {
            return id;
        }

        public int getUserId() {
            return userId;
        }

        public int getAccountId() {
            return accountId;
        }
    }
}
