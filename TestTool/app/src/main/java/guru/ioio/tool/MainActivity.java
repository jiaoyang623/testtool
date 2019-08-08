package guru.ioio.tool;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import guru.ioio.tool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ObservableField<String> input = new ObservableField<>();
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setPresenter(this);
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
}
