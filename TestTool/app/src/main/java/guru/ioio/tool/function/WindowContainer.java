package guru.ioio.tool.function;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import guru.ioio.tool.R;
import guru.ioio.tool.databinding.ContainerWindowBinding;
import guru.ioio.tool.databinding.WindowTestBinding;
import guru.ioio.tool.utils.Logger;

public class WindowContainer implements IContainer {
    private ContainerWindowBinding mBinding;
    private TestWindow mTestWindow;

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.container_window, parent, false);
            mBinding.setPresenter(this);
            mTestWindow = new TestWindow(context);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
    }


    public boolean openWindow(View v) {
        mTestWindow.show();
        return true;
    }


    public boolean closeWindow(View v) {
        mTestWindow.hide();
        return true;
    }

    public static class TestWindow {
        private boolean mIsShown = false;
        private WindowManager mWindowManager;
        private WindowTestBinding mBinding;

        public TestWindow(Context context) {
            mWindowManager = ((Activity) context).getWindowManager();
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_test, null, false);
            mBinding.setPresenter(this);
            mBinding.getRoot().setOnKeyListener((v, keyCode, event) -> {
                Logger.ci0("TestWindow", keyCode);
                return true;
            });
        }

        public boolean show() {
            if (!mIsShown) {
                mIsShown = true;
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.gravity = Gravity.CENTER;
                params.width = params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mWindowManager.addView(mBinding.getRoot(), params);
            }
            return true;
        }

        public boolean hide() {
            if (mIsShown) {
                mIsShown = false;
                mWindowManager.removeView(mBinding.getRoot());
            }
            return true;
        }
    }
}
