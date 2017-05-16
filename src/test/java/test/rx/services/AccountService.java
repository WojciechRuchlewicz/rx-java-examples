package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;
import test.rx.tools.Threads;

public class AccountService {

    public Observable<AccountDetailsResponse> getAccount(int accountIdn) {
        return Observable
                .fromCallable(() -> {
                    Log.print("Get user");
                    Threads.sleep(1000);
                    return new AccountDetailsResponse("1234", 1000);
                })
                .subscribeOn(Schedulers.io());
    }

    public static class AccountDetailsResponse {

        private final String no;
        private final double balance;

        public AccountDetailsResponse(String no, double balance) {
            this.no = no;
            this.balance = balance;
        }

        public String getNo() {
            return no;
        }

        public double getBalance() {
            return balance;
        }
    }
}
