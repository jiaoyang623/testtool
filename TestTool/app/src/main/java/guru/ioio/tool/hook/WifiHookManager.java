package guru.ioio.tool.hook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class WifiHookManager {
    private static WifiHookManager INSTANCE = new WifiHookManager();

    public static WifiHookManager getInstance() {
        return INSTANCE;
    }

    private WifiHookManager() {
    }

    private List<String> mBanList = new ArrayList<>();

    public void addBanList(String methodName) {
        if (!TextUtils.isEmpty(methodName) && !mBanList.contains(methodName)) {
            mBanList.add(methodName);
        }
    }

    public void removeBanList(String methodName) {
        if (!TextUtils.isEmpty(methodName) && mBanList.contains(methodName)) {
            mBanList.remove(methodName);
        }
    }

    @SuppressLint("PrivateApi")
    public boolean hook(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return false;
        }
        Object service = RefInvoke.getFieldObject(wifiManager, "mService");

        if (service == null) {
            return false;
        }
        Class<?> iWifiManagerClass = null;
        try {
            iWifiManagerClass = Class.forName("android.net.wifi.IWifiManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (iWifiManagerClass == null) {
            Log.i("WifiHookManager", "IWifiManager not fount");
            return false;
        }

        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iWifiManagerClass},
                new HookHandler(service)
        );

        RefInvoke.setFieldObject(wifiManager, "mService", proxy);
        return true;
    }

    private class HookHandler implements InvocationHandler {
        private Object mBase;

        public HookHandler(Object base) {
            mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i("WifiHook", method.getName() + ", " + mBanList);

            if (mBanList.contains(method.getName())) {
                Log.i("WifiHookManager", "ban " + method.getName());
                return null;
            } else {
                return method.invoke(mBase, args);
            }
        }
    }

}
