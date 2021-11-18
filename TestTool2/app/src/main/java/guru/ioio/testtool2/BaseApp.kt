package guru.ioio.testtool2

import android.app.Application
import android.content.Context
import guru.ioio.testtool2.lockscreen.LockScreenEventHelper

class BaseApp : Application() {
    companion object {
        private var INSTANCE: BaseApp? = null
        fun getInstance(): BaseApp {
            return INSTANCE!!
        }
    }

    init {
        INSTANCE = this
    }

    private val mLockScreenEventHelper = LockScreenEventHelper()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mLockScreenEventHelper.registerReceiver(this)
    }
}