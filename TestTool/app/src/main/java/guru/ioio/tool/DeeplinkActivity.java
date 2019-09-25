package guru.ioio.tool;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import guru.ioio.tool.databinding.ActivityDeeplinkBinding;

public class DeeplinkActivity extends AppCompatActivity {
    public ObservableField<String> input = new ObservableField<>();
    private ActivityDeeplinkBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deeplink);
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

    public boolean onTestProviderClick() {
        Cursor c = getContentResolver().query(Uri.parse("content://guru.ioio.tool.test"), null, null, null, null);
        if (c != null) {
            Log.i("DeeplinkActivity", "TestProvider: " + Arrays.toString(c.getColumnNames()));
            c.close();
        }
        return true;
    }
}
