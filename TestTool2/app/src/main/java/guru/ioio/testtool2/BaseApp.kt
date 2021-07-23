package guru.ioio.testtool2

import android.content.Context
import guru.ioio.testtool2.battery.BatteryApplication
import guru.ioio.testtool2.lockscreen.LockScreenEventHelper

class BaseApp : BatteryApplication() {
    private val mLockScreenEventHelper = LockScreenEventHelper()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mLockScreenEventHelper.registerReceiver(this)
    }
}