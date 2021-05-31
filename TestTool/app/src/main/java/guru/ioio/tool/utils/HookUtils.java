package guru.ioio.tool.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class HookUtils {
    public static void hookActivityManager() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            hookActivityManagerV25();
        } else {
//            hookActivityManagerV26();
            hook26();
        }
    }

    private static void hookActivityManagerV25() {
        try {
            Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative", "gDefault");
            Object rawIActivityManager = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance");
            @SuppressLint("PrivateApi")
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManagerInterface},
                    new HookHandler(rawIActivityManager));
            RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hookActivityManagerV26() {
        try {
            // 这个iActivityManagerSingleton对象实际上就是一个Singleton对象
            Object iActivityManagerSingleton = RefInvoke.getStaticFieldObject(
                    "android.app.ActivityManager", "IActivityManagerSingleton");
            Object rawIActivityManager = RefInvoke.getFieldObject("android.util.Singleton", iActivityManagerSingleton,
                    "mInstance");
            @SuppressLint("PrivateApi")
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManagerInterface},
                    new HookHandler(rawIActivityManager));
            RefInvoke.setFieldObject("android.util.Singleton", rawIActivityManager, "mInstance", proxy);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void hook26() {

        try {

            Class<?> amClass = Class.forName("android.app.ActivityManager");
            Field gDefaultField = amClass.getDeclaredField("IActivityManagerSingleton");
            gDefaultField.setAccessible(true);
            // 这个iActivityManagerSingleton对象实际上就是一个Singleton对象
            Object iActivityManagerSingleton = gDefaultField.get(null);


            // 反射Singleton类
            @SuppressLint("PrivateApi")
            Class<?> singleClass = Class.forName("android.util.Singleton");
            Field instanceField = singleClass.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object rawIActivityManager = instanceField.get(iActivityManagerSingleton);

            // 注释留空处
            @SuppressLint("PrivateApi")
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object newProxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerInterface},
                    new HookHandler(rawIActivityManager));

            // 将IActivityManagerSingleton中的mInstance字段替换成proxy对象
            instanceField.set(iActivityManagerSingleton, newProxyInstance);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public static class HookHandler implements InvocationHandler {
        private Object mBase;

        public HookHandler(Object base) {
            mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i("HookHandler", "[AMS] " + mBase.getClass().getSimpleName() + "." + method.getName() + "() " + Arrays.toString(args));
            if (method.getName().startsWith("startActivit")) {
                for (Object obj : args) {
                    if (obj instanceof Intent) {
                        Intent intent = (Intent) obj;
                        if ("application/vnd.android.package-archive".equals(intent.getType())) {
                            Log.i("HookHandler", "ban install");
                            return null;
                        }
                    }
                }
                Log.i("HookHandler", "startActivity: " + method + ": " + args);
            }
            return method.invoke(mBase, args);
        }
    }

    public static Object getActivityThread() {
        Context c;
        return RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
    }

    public static void attachBaseContext() {
        Handler mH = (Handler) RefInvoke.getFieldObject(getActivityThread(), "mH");
        RefInvoke.setFieldObject(Handler.class, mH, "mCallback", new HCallback(mH));
    }

    public static class HCallback implements Handler.Callback {

        private Handler mBase;

        public HCallback(Handler base) {
            mBase = base;
        }

        @Override
        public boolean handleMessage(Message msg) {
            Log.i("HookUtils", "[H] " + msg.what + ", " + msg.obj);
            mBase.handleMessage(msg);
            return true;
        }
    }
}
