package guru.ioio.tool.function;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

public class LiveDataContainer extends BaseContainer {
    private LiveDataModel model;

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        View out = super.onCreateView(context, inflater, parent);
        if (model == null && context instanceof FragmentActivity) {
            model = ViewModelProviders.of((FragmentActivity) context).get(LiveDataModel.class);
            model.getData().observe((LifecycleOwner) context, this::set);
        }
        mBinding.textResult.setText("click");
        mBinding.textResult.setOnClickListener(view -> model.getData().setValue(new Date().toString()));
        return out;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static class LiveDataModel extends ViewModel {
        private MutableLiveData<String> data;

        public MutableLiveData<String> getData() {
            if (data == null) {
                data = new MutableLiveData<>();
            }
            return data;
        }
    }
}
