package guru.ioio.testtool2.utils

class Utils {
    companion object {
        inline fun<R> notNull(vararg args: Any?, block:()->R)=
            when (args.filterNotNull().size) {
                args.size -> block()
                else -> null
            }
    }
}