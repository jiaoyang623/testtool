package guru.ioio.tool.function;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class LifecycleContainer implements IContainer, LifecycleObserver {
    private TextView tv;
    private StringBuilder builder = new StringBuilder();
    private Lifecycle mLifecycle = null;
    private Application mApp = null;


    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        builder.setLength(0);
        if (tv == null) {
            tv = new TextView(context);
        }
        if (context instanceof AppCompatActivity) {
            mLifecycle = ((AppCompatActivity) context).getLifecycle();
            mLifecycle.addObserver(this);
        }
        if (context.getApplicationContext() instanceof Application) {
            mApp = (Application) context.getApplicationContext();
            mApp.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
        }
        return tv;
    }

    @Override
    public void onDestroyView() {
        if (mLifecycle != null) {
            mLifecycle.removeObserver(this);
            mLifecycle = null;
        }
        if (mApp != null) {
            mApp.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {
        setText("compat_" + event.name());
    }

    private void setText(String text) {
        builder.append(text).append('\n');
        tv.setText(builder.toString());
    }

    private Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            setText(activity.getClass().getSimpleName() + '_' + "onCreated");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setText(activity.getClass().getSimpleName() + '_' + "onStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setText(activity.getClass().getSimpleName() + '_' + "onResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            setText(activity.getClass().getSimpleName() + '_' + "onPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            setText(activity.getClass().getSimpleName() + '_' + "onStopped");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            setText(activity.getClass().getSimpleName() + '_' + "onSaveInstance");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            setText(activity.getClass().getSimpleName() + '_' + "onDestroy");
        }
    };
}
