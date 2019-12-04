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
import guru.ioio.tool.tests.InfoTest;
import guru.ioio.tool.tests.MSATest;
import guru.ioio.tool.tests.MacAddress6Test;
import guru.ioio.tool.tests.MacAddress7Test;
import guru.ioio.tool.tests.MacAddress8Test;
import guru.ioio.tool.utils.ExceptionUtils;
import guru.ioio.tool.utils.RVBindingBaseAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TestActivity extends Activity {
    private static Class[] TEST_LIST = {
            DemoTest.class,
            DnsTest.class,
            InfoTest.class,
            MSATest.class,
            MacAddress6Test.class,
            MacAddress7Test.class,
            MacAddress8Test.class,
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
        Observable<String> observable = null;
        try {
            ITest test = clazz.newInstance();
            observable = test.onClick(v);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (observable == null) {
            result.set("ITest returns nothing");
            isLoading.set(false);
            return true;
        }
        Disposable d = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            result.set(s);
                            isLoading.set(false);
                        },
                        e -> {
                            result.set(ExceptionUtils.getStackTrace(e));
                            isLoading.set(false);
                        },
                        () -> isLoading.set(false));
        return true;
    }

}
