package guru.ioio.tool.tests;

import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpTest extends BaseTest {
    @Override
    protected String doClick(View v) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url("https://www.baidu.com")
                .get()
                .build();

        Call call = client.newCall(req);
        Response resp = call.execute();
        return resp.body().string();
    }
}
