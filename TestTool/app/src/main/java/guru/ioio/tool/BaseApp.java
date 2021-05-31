package guru.ioio.tool;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.bun.miitmdid.core.JLibrary;

import guru.ioio.tool.apptrace.AppTraceSDK;
import guru.ioio.tool.dagger.ApplicationComponent;
import guru.ioio.tool.dagger.DaggerApplicationComponent;
import guru.ioio.tool.utils.Logger;

public class BaseApp extends Application {
    private static BaseApp sInstance;
    private Logger mLogger = new Logger(getClass());

    public BaseApp() {
        mLogger.in();
        sInstance = this;
        mLogger.out();
    }

    public static BaseApp getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        mLogger.in();
        super.attachBaseContext(base);
        MultiDex.install(this);
        JLibrary.InitEntry(base);
        mLogger.out();
    }

    private ApplicationComponent mDaggerComponent;

    @Override
    public void onCreate() {
        mLogger.in();
        super.onCreate();
        AppTraceSDK.init(this);
        mDaggerComponent = DaggerApplicationComponent.create();
        mLogger.out();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    public ApplicationComponent getDaggerComponent() {
        return mDaggerComponent;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
