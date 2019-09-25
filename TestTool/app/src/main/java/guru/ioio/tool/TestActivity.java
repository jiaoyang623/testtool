package guru.ioio.tool;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import guru.ioio.tool.databinding.ActivityTestBinding;
import guru.ioio.tool.tests.DemoTest;
import guru.ioio.tool.tests.DnsTest;
import guru.ioio.tool.utils.ExceptionUtils;
import guru.ioio.tool.utils.RVBindingBaseAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends Activity {
    private static Class[] TEST_LIST = {
            DemoTest.class,
            DnsTest.class,
    };
    public ObservableField<String> result = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    private ActivityTestBinding mBinding;
    private RVBindingBaseAdapter<Class<? extends ITest>> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        mBinding.setPresenter(this);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_test, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mBinding.recycler.setAdapter(mAdapter);

        mAdapter.set(loadClass());
    }

    private List<Class<? extends ITest>> loadClass() {
        return Arrays.asList(TEST_LIST);
    }

    public boolean onItemClick(View v, Class<? extends ITest> clazz) {
        isLoading.set(true);
        Disposable d = Observable.create(emitter -> {
            try {
                ITest test = clazz.newInstance();
                emitter.onNext(test.onClick(v));
                emitter.onComplete();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> result.set(s == null ? "" : s.toString()),
                        e -> {
                            result.set(ExceptionUtils.getStackTrace(e));
                            isLoading.set(false);
                        },
                        () -> isLoading.set(false));
        return true;
    }

}
