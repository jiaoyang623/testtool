package guru.ioio.tool;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;

import guru.ioio.tool.databinding.ActivityTestListBinding;
import guru.ioio.tool.tests.DemoTest;
import guru.ioio.tool.tests.MacAddress6Test;
import guru.ioio.tool.tests.MacAddress7Test;
import guru.ioio.tool.tests.MacAddress8Test;
import guru.ioio.tool.tests.OKHttpTest;
import guru.ioio.tool.utils.RVBindingBaseAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestListActivity extends Activity {
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableField<String> result = new ObservableField<>("");

    private ActivityTestListBinding mBinding;
    private RVBindingBaseAdapter<Class<? extends ITest>> mAdapter;

    private Class<? extends ITest>[] mClassList = new Class[]{
            DemoTest.class,
            MacAddress6Test.class,
            MacAddress7Test.class,
            MacAddress8Test.class,
            OKHttpTest.class,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_list);
        mBinding.setPresenter(this);
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_test, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mAdapter.add(Arrays.asList(mClassList));
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
    }

    private Disposable mLastTask = null;

    public boolean onItemClick(View v, Class<? extends ITest> clazz) {
        if (mLastTask != null) {
            return true;
        }
        try {
            isLoading.set(true);
            ITest test = clazz.newInstance();
            mLastTask = test.onClick(v).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(r -> result.set(r),
                            e -> {
                                result.set(e.toString());
                                isLoading.set(false);
                                mLastTask = null;
                            },
                            () -> {
                                isLoading.set(false);
                                mLastTask = null;
                            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return true;
    }
}
