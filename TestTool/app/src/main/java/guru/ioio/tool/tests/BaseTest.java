package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.ITest;
import io.reactivex.Observable;

public abstract class BaseTest implements ITest {
    protected abstract String doClick(View v);

    @Override
    public Observable<String> onClick(View v) {
        return Observable.create(e -> e.onNext(doClick(v)));
    }
}
