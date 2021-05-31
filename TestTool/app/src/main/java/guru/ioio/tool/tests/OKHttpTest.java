package guru.ioio.tool.tests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.io.File;
import java.io.IOException;

import guru.ioio.tool.BaseApp;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpTest extends AbsBaseTest {
    @Override
    protected String doClick(View v) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(getCacheDir(v.getContext(), "netcache"), 64L * 1024 * 1024))
                .addInterceptor(new TestCacheInterceptor())
                .build();
        Request req = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .get()
                .build();

        Call call = client.newCall(req);
        Response resp = call.execute();
        return resp.body().string();
    }

    private File getCacheDir(Context context, String name) {
        File base = context.getCacheDir();
        File target = new File(base.getAbsolutePath() + File.separator + name);
        if (!target.exists()) {
            target.mkdirs();
        }
        return target;
    }

    public static class TestCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            int cacheTime = 7 * 24 * 24 * 60;
            Request request = chain.request();
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (isNetworkAvailable()) {
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + cacheTime)
                        .build();
            } else {
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + cacheTime)
                        .build();
            }
            return response;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }

}
