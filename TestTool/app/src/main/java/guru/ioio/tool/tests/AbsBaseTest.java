package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.ITest;
import io.reactivex.Observable;

public abstract class AbsBaseTest implements ITest {
    protected abstract String doClick(View v) throws Throwable;

    @Override
    public Observable<String> onClick(View v) {
        return Observable.create(emitter -> {
            try {
                String result = doClick(v);
                emitter.onNext(result == null ? "null" : result);
            } catch (Throwable throwable) {
                emitter.onError(throwable);
            } finally {
                emitter.onComplete();
            }
        });
    }

}
