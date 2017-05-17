package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;

import static test.rx.tools.Threads.sleep;

public class UserDao {

    public UserDetails getUser(int userId) {
        Log.print("getUser START");
        sleep(1000);
        UserDetails details = new UserDetails("John Smith", "owner");
        Log.print("getUser END");
        return details;
    }

    public Observable<UserDetails> getUserObservable(int userId) {
        return Observable.fromCallable(() -> getUser(userId));
    }

    public static class UserDetails {

        private final String name;
        private final String role;

        public UserDetails(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "UserDetails{" +
                    "name='" + name + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
}
