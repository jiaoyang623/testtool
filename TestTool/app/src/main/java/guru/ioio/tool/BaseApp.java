package guru.ioio.tool;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bun.miitmdid.core.JLibrary;

import guru.ioio.tool.apptrace.AppTraceSDK;
import guru.ioio.tool.dagger.ApplicationComponent;
import guru.ioio.tool.dagger.DaggerApplicationComponent;

public class BaseApp extends Application {
    private static BaseApp sInstance;

    public BaseApp() {
        sInstance = this;
    }

    public static BaseApp getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        HHookManager.getInstance();
//        AMSHookManager.getInstance();
        JLibrary.InitEntry(base);
    }

    private ApplicationComponent mDaggerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppTraceSDK.init(this);
        mDaggerComponent = DaggerApplicationComponent.create();
    }

    @Override
    public Object getSystemService(String name) {
        Log.i("BaseApp", "getSystemService " + name);
        return super.getSystemService(name);
    }

    public ApplicationComponent getDaggerComponent() {
        return mDaggerComponent;
    }
}
