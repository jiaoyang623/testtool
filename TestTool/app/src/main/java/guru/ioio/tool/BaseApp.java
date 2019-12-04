package guru.ioio.tool;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bun.miitmdid.core.JLibrary;

import guru.ioio.tool.apptrace.AppTraceSDK;

public class BaseApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        HHookManager.getInstance();
//        AMSHookManager.getInstance();
        JLibrary.InitEntry(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppTraceSDK.init(this);
    }

    @Override
    public Object getSystemService(String name) {
        Log.i("BaseApp", "getSystemService " + name);
        return super.getSystemService(name);
    }
}
