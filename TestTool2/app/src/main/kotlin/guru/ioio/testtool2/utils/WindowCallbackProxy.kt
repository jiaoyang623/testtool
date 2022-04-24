package guru.ioio.testtool2.utils

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class WindowCallbackProxy(private val target: Any) : InvocationHandler {
    private val mLogger = Logger(WindowCallbackProxy::class.simpleName)

    fun newProxyInstance(): Any {
        val instance = Proxy.newProxyInstance(
            target.javaClass.classLoader,
            target.javaClass.interfaces,
            this
        )
        return instance
    }

    fun <T> newProxyInstance(clazz: Class<T>): T {
        val instance: T = Proxy.newProxyInstance(
            target.javaClass.classLoader,
            arrayOf(clazz),
            this
        ) as T
        return instance
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val name = method?.name
        name?.let { mLogger.ci(it) }
        return method?.invoke(target, *(args ?: emptyArray()))
    }
}