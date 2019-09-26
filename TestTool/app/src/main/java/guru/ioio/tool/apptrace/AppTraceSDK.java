package guru.ioio.tool.apptrace;

import android.app.Application;

public class AppTraceSDK {
    private static boolean isInitialized = false;
    private static LifecycleTracker mLifecycleTracker = new LifecycleTracker();

    public static void init(Application app) {
        if (isInitialized) {
            return;
        } else {
            isInitialized = true;
        }

        mLifecycleTracker.register(app);
    }
}
