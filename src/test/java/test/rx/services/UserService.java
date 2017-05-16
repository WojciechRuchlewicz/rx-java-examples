package test.rx.services;


import rx.Observable;
import rx.schedulers.Schedulers;
import test.rx.tools.Log;
import test.rx.tools.Threads;

public class UserService {

    public Observable<UserDetailsResponse> getUser(int userIdn) {
        return Observable
                .fromCallable(() -> {
                    Log.print("Get user");
                    Threads.sleep(1000);
                    return new UserDetailsResponse("John Smith", "owner");
                })
                .subscribeOn(Schedulers.io());
    }

    public static class UserDetailsResponse {

        private final String name;
        private final String role;

        public UserDetailsResponse(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }
    }
}
