package guru.ioio.tool.function;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import guru.ioio.tool.R;
import guru.ioio.tool.databinding.ContainerBaseBinding;

public class BaseContainer implements IContainer {
    protected ContainerBaseBinding mBinding;

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.container_base, parent, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {

    }

    protected void set(String text) {
        if (mBinding != null) {
            mBinding.textResult.setText(text);
        }
    }
}
