package guru.ioio.tool.hook;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class AMSHookManager {
    private static AMSHookManager INSTANCE = new AMSHookManager();

    private AMSHookManager() {
        hookAMN();
    }

    public static AMSHookManager getInstance() {
        return INSTANCE;
    }

    private void hookAMN() {
        try {
            Object gDefault;
            if (Build.VERSION.SDK_INT < 26) {
                gDefault = RefInvoke.getStaticFieldObject(
                        "android.app.ActivityManagerNative", "gDefault");
            } else {
                gDefault = RefInvoke.getStaticFieldObject(
                        "android.app.ActivityManager", "IActivityManagerSingleton");
            }
            Object mInstance = RefInvoke.getFieldObject(
                    "android.util.Singleton", gDefault, "mInstance");
            Class<?> classB2Interface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{classB2Interface},
                    new HookHandler(mInstance)
            );
            RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class HookHandler implements InvocationHandler {
        private Object mBase;

        public HookHandler(Object base) {
            mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i("AMSHook", method.getName());
            for (OnInvokeListener l : mListenerList) {
                Object ret = l.invoke(mBase, proxy, method, args);
                if (ret != null) {
                    return ret;
                }
            }
            return method.invoke(mBase, args);
        }
    }

    public void addListener(OnInvokeListener l) {
        if (!mListenerList.contains(l)) {
            mListenerList.add(l);
        }
    }

    public void removeListener(OnInvokeListener l) {
        if (l != null) {
            mListenerList.remove(l);
        }
    }

    private List<OnInvokeListener> mListenerList = new ArrayList<>();

    public interface OnInvokeListener {
        Object invoke(Object base, Object proxy, Method method, Object[] args) throws Throwable;
    }

}
