package test.rx.tools;


import java.util.concurrent.TimeUnit;

public class Threads {

    public static void sleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
