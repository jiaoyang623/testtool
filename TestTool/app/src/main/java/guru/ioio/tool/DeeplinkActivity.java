package guru.ioio.tool;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.tool.databinding.ActivityDeeplinkBinding;
import guru.ioio.tool.utils.RVBindingBaseAdapter;

public class DeeplinkActivity extends AppCompatActivity {
    public ObservableField<String> input = new ObservableField<>();
    private ActivityDeeplinkBinding mBinding;
    private RVBindingBaseAdapter<DeeplinkBean> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deeplink);
        mBinding.setPresenter(this);
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_deeplink, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (!BuildConfig.FOR_PARTNER) {
            List<DeeplinkBean> list = new ArrayList<>();
            for (String item : getResources().getStringArray(R.array.deeplink_list)) {
                list.add(DeeplinkBean.valueOf(item));
            }
            mAdapter.add(list);
        }
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

    public boolean onBrowseClick() {
        String url = input.get();
        String innerUrl = "qhvideo://vapp.360.cn/webview?url=" + Uri.encode(url);
        String outerUrl = "qhvideo://vapp.360.cn/home?" + Uri.encode(innerUrl);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(outerUrl));
        startActivity(intent);
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

    public static class DeeplinkBean {
        public static DeeplinkBean valueOf(String data) {
            if (data != null && data.contains("|")) {
                String[] value = data.split("\\|");
                DeeplinkBean bean = new DeeplinkBean();
                bean.title = value[0];
                bean.link = value[1];
                return bean;
            }
            return new DeeplinkBean();
        }

        public String title;
        public String link;
    }
}
