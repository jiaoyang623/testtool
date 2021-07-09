package guru.ioio.testtool2.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import guru.ioio.testtool2.utils.Logger

class LockScreenEventHelper {
    private var mIsRegistered = false
    private val mLockScreenReceiver = LockScreenReceiver()

    fun registerReceiver(context: Context) {
        if (!mIsRegistered) {
            mIsRegistered = true
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
                addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            }
            context.registerReceiver(mLockScreenReceiver, filter)
        }
    }
}

class LockScreenReceiver : BroadcastReceiver() {
    private val mLogger = Logger(LockScreenReceiver::class.java.simpleName)

    override fun onReceive(context: Context?, intent: Intent?) {
        mLogger.ci(intent!!.action!!)
    }
}