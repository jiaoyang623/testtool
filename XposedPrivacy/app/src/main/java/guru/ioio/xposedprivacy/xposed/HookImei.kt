package guru.ioio.xposedprivacy.xposed

import android.net.wifi.WifiManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookImei : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        XposedBridge.log("HookImei ++ current package: " + lpparam?.packageName)

        if (lpparam?.packageName == "guru.ioio.testtool2") {
            hookCustomCode(lpparam)
            hookSystemCode(lpparam)
        }
    }

    private fun hookCustomCode(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod("guru.ioio.testtool2.utils.JavaUtils",
            lpparam.classLoader,
            "getTestMessage",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    XposedBridge.log("HookImei >> getTestMessage")
                }
            })

    }

    private fun hookSystemCode(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            WifiManager::class.java,
            "getConnectionInfo",
            arrayOf(
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        XposedBridge.log("HookImei >> getConnectionInfo")
                    }
                })
        )

    }
}