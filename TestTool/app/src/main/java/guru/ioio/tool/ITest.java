package guru.ioio.tool;

import android.view.View;

import io.reactivex.Observable;

public interface ITest {
    Observable<String> onClick(View v);
}
