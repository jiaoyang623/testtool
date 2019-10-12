package guru.ioio.tool;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import guru.ioio.tool.databinding.ActivityDeeplinkBinding;
import guru.ioio.tool.utils.RVBindingBaseAdapter;

public class DeeplinkActivity extends AppCompatActivity {
    public ObservableField<String> input = new ObservableField<>();
    private ActivityDeeplinkBinding mBinding;
    private RVBindingBaseAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deeplink);
        mBinding.setPresenter(this);
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_deeplink, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String[] data = getResources().getStringArray(R.array.deeplink_list);
        mAdapter.add(Arrays.asList(data));
    }

    public boolean onGoClick() {
        String text = input.get();
        if (!TextUtils.isEmpty(text)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse(text.trim()));
            startActivity(intent);
        }
        return true;
    }

    public boolean onClearClick() {
        input.set("");
        return true;
    }

    public boolean onItemClick(String data) {
        input.set(data);
        return true;
    }
}
