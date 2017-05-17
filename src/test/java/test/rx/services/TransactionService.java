package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;
import test.rx.tools.Threads;

import java.util.Random;

import static test.rx.tools.Threads.sleep;


public class TransactionService {

    public Observable<TransactionUpdatedEvent> getTransactionsUpdates() {
        return Observable
                .just(
                        new TransactionUpdatedEvent(1, "1234", TransactionState.PENDING),
                        new TransactionUpdatedEvent(1, "1234", TransactionState.COMPLETED)
                )
                .subscribeOn(Schedulers.io());
    }

    public Observable<TransactionDetails> getTransactionDetails(int transactionId) {
        return Observable
                .fromCallable(() -> {
                    Log.print("getTransactionDetails START");
                    sleep(300);
                    TransactionDetails details = new TransactionDetails(2, 151.2);
                    Log.print("getTransactionDetails END");
                    return details;
                })
                .subscribeOn(Schedulers.io());
    }

    public static class TransactionUpdatedEvent {

        private final int id;
        private final String accountNo;
        private final TransactionState state;

        public TransactionUpdatedEvent(int id, String accountNo, TransactionState state) {
            this.id = id;
            this.accountNo = accountNo;
            this.state = state;
        }

        public int getTransactionId() {
            return id;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public TransactionState getState() {
            return state;
        }
    }

    public static class TransactionDetails {

        private final int quantity;
        private final double price;

        public TransactionDetails(int quantity, double price) {
            this.quantity = quantity;
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    public enum TransactionState {
        PENDING,
        COMPLETED
    }
}
