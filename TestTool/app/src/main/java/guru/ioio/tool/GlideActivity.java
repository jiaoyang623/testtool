package guru.ioio.tool;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import guru.ioio.tool.databinding.ActivityGlideBinding;

public class GlideActivity extends Activity {
    private ActivityGlideBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_glide);
        mBinding.setPresenter(this);
        Glide.with(this).load("").into(mBinding.img);

    }
}
