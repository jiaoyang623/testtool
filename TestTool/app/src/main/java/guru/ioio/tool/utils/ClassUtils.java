package guru.ioio.tool.utils;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import guru.ioio.tool.BaseApp;

public class ClassUtils {
    private static final String TAG = "ClassUtils";

    public static List<Class<?>> getAllClass(String packageName) {
        List<Class<?>> list = new ArrayList<>();
        try {
            DexFile dexFile = new DexFile(BaseApp.getInstance().getPackageCodePath());
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.contains(packageName)) {
                    list.add(Class.forName(className));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
