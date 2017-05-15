package test.rx.tools;


import java.time.LocalTime;

public class Log {

    public static void print(Object o) {
        System.out.println(
                LocalTime.now().toString()
                + " | " + Thread.currentThread().getName()
                + " | " + o
        );
    }
}
