package guru.ioio.tool;

import android.app.Application;
import android.content.Context;

import com.bun.miitmdid.core.JLibrary;

import guru.ioio.tool.apptrace.AppTraceSDK;
import guru.ioio.tool.hook.AMSHookManager;
import guru.ioio.tool.hook.HHookManager;

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
}
