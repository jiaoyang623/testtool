package guru.ioio.testtool2

import android.app.Application
import android.content.Context
import guru.ioio.testtool2.lockscreen.LockScreenEventHelper
import guru.ioio.testtool2.nt.ClassLoaderManager

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
        ClassLoaderManager.replaceSystemClassLoader(this)
    }

    override fun onCreate() {
        super.onCreate()
        mLockScreenEventHelper.registerReceiver(this)
    }
}