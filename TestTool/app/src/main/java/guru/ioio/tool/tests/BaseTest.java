package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.ITest;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseTest implements ITest {
    protected abstract String doClick(View v);

    @Override
    public Observable<String> onClick(View v) {
        return getObservable(v).subscribeOn(Schedulers.io());
    }

    private Observable<String> getObservable(View v) {
        return Observable.create(e -> e.onNext(doClick(v)));
    }
}
