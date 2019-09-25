package guru.ioio.tool.hook;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

public class HHookManager {
    private static HHookManager INSTANCE = new HHookManager();

    public static HHookManager getInstance() {
        return INSTANCE;
    }

    private HHookManager() {
        hook();
    }

    private void hook() {
        Object currentActivityThread = RefInvoke.getStaticFieldObject(
                "android.app.ActivityThread", "sCurrentActivityThread");
        Handler mH = (Handler) RefInvoke.getFieldObject(
                "android.app.ActivityThread", currentActivityThread, "mH");
        RefInvoke.setFieldObject(Handler.class, mH, "mCallback", new HCallback(mH));
    }

    private static class HCallback implements Handler.Callback {
        private Handler mBase;

        public HCallback(Handler base) {
            mBase = base;
        }

        @Override
        public boolean handleMessage(Message msg) {
            mBase.handleMessage(msg);
            Log.i("HHook", translate(msg.what));
            return true;
        }
    }

    private static SparseArray<String> sMap = new SparseArray<>();

    static {
        sMap.put(110, "BIND_APPLICATION");
        sMap.put(111, "EXIT_APPLICATION");
        sMap.put(113, "RECEIVER");
        sMap.put(114, "CREATE_SERVICE");
        sMap.put(115, "SERVICE_ARGS");
        sMap.put(116, "STOP_SERVICE");
        sMap.put(118, "CONFIGURATION_CHANGED");
        sMap.put(119, "CLEAN_UP_CONTEXT");
        sMap.put(120, "GC_WHEN_IDLE");
        sMap.put(121, "BIND_SERVICE");
        sMap.put(122, "UNBIND_SERVICE");
        sMap.put(123, "DUMP_SERVICE");
        sMap.put(124, "LOW_MEMORY");
        sMap.put(127, "PROFILER_CONTROL");
        sMap.put(128, "CREATE_BACKUP_AGENT");
        sMap.put(129, "DESTROY_BACKUP_AGENT");
        sMap.put(130, "SUICIDE");
        sMap.put(131, "REMOVE_PROVIDER");
        sMap.put(132, "ENABLE_JIT");
        sMap.put(133, "DISPATCH_PACKAGE_BROADCAST");
        sMap.put(134, "SCHEDULE_CRASH");
        sMap.put(135, "DUMP_HEAP");
        sMap.put(136, "DUMP_ACTIVITY");
        sMap.put(137, "SLEEPING");
        sMap.put(138, "SET_CORE_SETTINGS");
        sMap.put(139, "UPDATE_PACKAGE_COMPATIBILITY_INFO");
        sMap.put(141, "DUMP_PROVIDER");
        sMap.put(142, "UNSTABLE_PROVIDER_DIED");
        sMap.put(143, "REQUEST_ASSIST_CONTEXT_EXTRAS");
        sMap.put(144, "TRANSLUCENT_CONVERSION_COMPLETE");
        sMap.put(145, "INSTALL_PROVIDER");
        sMap.put(146, "ON_NEW_ACTIVITY_OPTIONS");
        sMap.put(149, "ENTER_ANIMATION_COMPLETE");
        sMap.put(150, "START_BINDER_TRACKING");
        sMap.put(151, "STOP_BINDER_TRACKING_AND_DUMP");
        sMap.put(154, "LOCAL_VOICE_INTERACTION_STARTED");
        sMap.put(155, "ATTACH_AGENT");
        sMap.put(156, "APPLICATION_INFO_CHANGED");
        sMap.put(158, "RUN_ISOLATED_ENTRY_POINT");
        sMap.put(159, "EXECUTE_TRANSACTION");
        sMap.put(160, "RELAUNCH_ACTIVITY");

    }

    private static String translate(int what) {
        return sMap.get(what);
    }
}
