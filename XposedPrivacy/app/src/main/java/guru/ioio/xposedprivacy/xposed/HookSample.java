package guru.ioio.xposedprivacy.xposed;

import android.net.wifi.WifiManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookSample implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        XposedBridge.log("HookSample >> " + param.packageName + " " + param.processName);
        Log.i("HookSample", param.packageName);

        if ("guru.ioio.testtool2".equals(param.packageName)) {
            hookCustomCode(param);
            hookSystem();
        }
    }

    private void hookCustomCode(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("guru.ioio.testtool2.utils.JavaUtils",
                param.classLoader,
                "getTestMessage",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        XposedBridge.log("HookSample >> getTestMessage");
                    }
                });
    }

    private void hookSystem() {
        XposedHelpers.findAndHookMethod(WifiManager.class, "getConnectionInfo", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log("HookSample >> getConnectionInfo");
            }
        });
    }
}
