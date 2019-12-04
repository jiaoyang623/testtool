package guru.ioio.tool.hook;

import java.lang.reflect.Method;

public interface OnInvokeListener {
    Object invoke(Object base, Object proxy, Method method, Object[] args) throws Throwable;
}
