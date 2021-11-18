package guru.ioio.xposedprivacy.xposed

import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiInfo
import android.provider.Settings
import android.telephony.TelephonyManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.net.NetworkInterface

class HookPrivacy : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) {
            return
        }
        XposedBridge.log("load package <${lpparam.packageName}>")
        hookPhoneState(lpparam)
        hookMac(lpparam)
        hookAndroidId(lpparam)
        hookPackageManager(lpparam)
        hookLocationManager(lpparam)
    }

    private fun hookLocationManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookMethod(lpparam, LocationManager::class.java, "getCurrentLocation")
        hookMethod(lpparam, LocationManager::class.java, "getLastKnownLocation")
        hookMethod(lpparam, LocationManager::class.java, "getLastLocation")
    }

    private fun hookPackageManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookMethod(lpparam, PackageManager::class.java, "getInstalledPackages")
        hookMethod(lpparam, PackageManager::class.java, "getInstalledApplicstions")
    }

    private fun hookPhoneState(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookMethod(lpparam, TelephonyManager::class.java, "getImei")
        hookMethod(lpparam, TelephonyManager::class.java, "getDeviceId")
        hookMethod(lpparam, TelephonyManager::class.java, "getNetworkType")
        hookMethod(lpparam, TelephonyManager::class.java, "getSimSerialNumber")
        hookMethod(lpparam, TelephonyManager::class.java, "getSubscriberId")
    }

    private fun hookMac(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookMethod(lpparam, WifiInfo::class.java, "getMacAddress")
        hookMethod(lpparam, NetworkInterface::class.java, "getHardwareAddress")
    }

    private fun hookAndroidId(lpparam: XC_LoadPackage.LoadPackageParam) {
        val listener = object : IHook {
            override fun needToPrint(
                lpparam: XC_LoadPackage.LoadPackageParam,
                param: XC_MethodHook.MethodHookParam?
            ): Boolean {
                return param != null && param.args.isNotEmpty() && "android_id" == param.args[0]
            }
        }
        hookMethod(lpparam, Settings.System::class.java, "getString", listener)
        hookMethod(lpparam, Settings.Secure::class.java, "getString", listener)
    }

    private fun hookMethod(
        lpparam: XC_LoadPackage.LoadPackageParam,
        clazz: Class<*>,
        method: String
    ) {
        hookMethod(lpparam, clazz, method, null)
    }

    private fun hookMethod(
        lpparam: XC_LoadPackage.LoadPackageParam,
        clazz: Class<*>,
        method: String,
        listener: IHook?
    ) {
        try {
            XposedHelpers.findAndHookMethod(clazz, method, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (listener == null || listener.needToPrint(lpparam, param)) {
                        XposedBridge.log("call ${lpparam.packageName}->${clazz.simpleName}.$method")
                    }
                }
            })
        } catch (e: NoSuchMethodError) {
        }
    }

    interface IHook {
        fun needToPrint(
            lpparam: XC_LoadPackage.LoadPackageParam,
            param: XC_MethodHook.MethodHookParam?
        ): Boolean;
    }
}
