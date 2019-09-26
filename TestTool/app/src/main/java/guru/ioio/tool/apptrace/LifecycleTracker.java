package guru.ioio.tool.apptrace;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class LifecycleTracker {
    private Application.ActivityLifecycleCallbacks mCallback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.i("LT", "onActivityCreated " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.i("LT", "onActivityStarted " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.i("LT", "onActivityResumed " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.i("LT", "onActivityPaused " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.i("LT", "onActivityStopped " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.i("LT", "onActivitySaveInstanceState " + activity.getClass().getSimpleName());

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.i("LT", "onActivityDestroyed " + activity.getClass().getSimpleName());

        }
    };

    public void register(Application app) {
        app.unregisterActivityLifecycleCallbacks(mCallback);
        app.registerActivityLifecycleCallbacks(mCallback);
    }


}
