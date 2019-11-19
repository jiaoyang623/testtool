package guru.ioio.tool;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import guru.ioio.tool.databinding.ActivityFunctionTestBinding;
import guru.ioio.tool.function.BaseContainer;
import guru.ioio.tool.function.IContainer;
import guru.ioio.tool.function.LifecycleContainer;
import guru.ioio.tool.function.LiveDataContainer;
import guru.ioio.tool.function.PagingContainer;
import guru.ioio.tool.utils.RVBindingBaseAdapter;

public class FunctionTestActivity extends AppCompatActivity {
    private Class[] mContainers = {
            BaseContainer.class,
            LifecycleContainer.class,
            LiveDataContainer.class,
            PagingContainer.class,
    };

    private ActivityFunctionTestBinding mBinding;
    private Map<Class, IContainer> mCache = new HashMap<>();

    private RVBindingBaseAdapter<Class> mAdapter;
    private IContainer mCurrentContainer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_function_test);
        mBinding.setPresenter(this);
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_function, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter.set(new ArrayList<>(Arrays.asList(mContainers)));

        mBinding.drawer.openDrawer(Gravity.START);
    }

    public boolean onItemClick(Class clazz) {
        if (clazz != null && (mCurrentContainer == null || mCurrentContainer.getClass() != clazz)) {
            IContainer container = mCache.get(clazz);
            if (container == null) {
                try {
                    container = (IContainer) clazz.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            if (container != null) {
                mBinding.holder.removeAllViews();
                if (mCurrentContainer != null) {
                    mCurrentContainer.onDestroyView();
                }

                mBinding.holder.addView(container.onCreateView(this, LayoutInflater.from(this), mBinding.holder));
                mCurrentContainer = container;
            }
        }
        mBinding.drawer.closeDrawer(Gravity.START);
        return true;
    }
}
