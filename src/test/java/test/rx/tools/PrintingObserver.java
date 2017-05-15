package test.rx.tools;

import rx.Observer;

import static test.rx.tools.Log.print;


public class PrintingObserver implements Observer<Object> {

    private final String name;

    public PrintingObserver() {
        this.name = "";
    }

    public PrintingObserver(String name) {
        this.name = name + " ";
    }

    @Override
    public void onNext(Object o) {
        print(name + "onNext " + o);
    }

    @Override
    public void onError(Throwable e) {
        print(name + "onError " + e);
    }

    @Override
    public void onCompleted() {
        print(name + "onCompleted");
    }
}
