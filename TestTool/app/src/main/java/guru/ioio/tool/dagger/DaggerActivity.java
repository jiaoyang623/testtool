package guru.ioio.tool.dagger;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import guru.ioio.tool.BaseApp;
import guru.ioio.tool.R;
import guru.ioio.tool.databinding.ActivityDaggerBinding;

public class DaggerActivity extends Activity {

    @Inject
    public DaggerBean bean;

    private ActivityDaggerBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.getInstance().getDaggerComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dagger);
        mBinding.setPresenter(this);
    }
}
