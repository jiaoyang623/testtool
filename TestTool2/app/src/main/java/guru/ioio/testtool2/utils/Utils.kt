package guru.ioio.testtool2.utils

import guru.ioio.testtool2.BaseApp
import java.io.Closeable

class Utils {
    companion object {
        inline fun <R> notNull(vararg args: Any?, block: () -> R) =
            when (args.filterNotNull().size) {
                args.size -> block()
                else -> null
            }

        fun close(closeable: Closeable?) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

        fun getCachePath(): String {
            return BaseApp.getInstance().externalCacheDir!!.absolutePath
        }
    }
}