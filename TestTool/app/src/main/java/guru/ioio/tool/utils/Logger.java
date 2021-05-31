package guru.ioio.tool.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import guru.ioio.tool.BaseApp;
import guru.ioio.tool.BuildConfig;

public class Logger {
    private static boolean DEBUG = BuildConfig.DEBUG;
    //    private static boolean DEBUG = false;
    private String mTag = "Logger";

    private long mStartTime = 0;
    private long mLastTime = 0;
    private int mStackLevel = 0;
    private boolean mShowClass = false;


    public Logger(String tag) {
        mTag = tag;
        restart();
    }

    public Logger(Class clazz) {
        this(clazz == null ? null : clazz.getSimpleName());
    }

    /**
     * 设置获取调用栈的层级
     */
    public Logger setStackLevel(int level) {
        mStackLevel = level;
        return this;
    }

    public Logger setShowClass(boolean isShown) {
        mShowClass = isShown;
        return this;
    }

    private List<String> mCommonLog = new ArrayList<>();
    private String mCommonLogStr = "";

    public Logger addCommon(Object param) {
        if (param != null) {
            mCommonLog.add(param.toString());
            mCommonLogStr = null;
        }

        return this;
    }

    public Logger clearCommon() {
        mCommonLog.clear();
        mCommonLogStr = null;
        return this;
    }

    private String getCommonLog() {
        if (!DEBUG) {
            return "RELEASE";
        }
        if (mCommonLogStr == null) {
            StringBuilder builder = new StringBuilder("");
            for (String log : mCommonLog) {
                builder.append(log).append(", ");
            }
            mCommonLogStr = builder.toString();
        }
        return mCommonLogStr;
    }

    public void i(Object... param) {
        Log.i(mTag, parseWithCommon(param));
    }

    public void i2(Object... param) {
        try {
            String msg = parseWithCommon(param);
            int p = 2048;
            long length = msg.length();
            if (length < p || length == p)
                Log.i(mTag, msg);
            else {
                while (msg.length() > p) {
                    String logContent = msg.substring(0, p);
                    msg = msg.replace(logContent, "");
                    Log.i(mTag, logContent);
                }
                Log.i(mTag, "续上方-->" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ci(Object... param) {
        Log.i(mTag, getSimpleCaller() + ": " + parseWithCommon(param));

    }


    public void printStackTrace() {
        printStackTrace0(mTag);
    }

    public static void printStackTrace0(String tag) {
        StringWriter writer = new StringWriter();
        PrintWriter print = new PrintWriter(writer);
        new Throwable().printStackTrace(print);
        i0(tag, writer.toString());
        close(print);
        close(writer);
    }

    private String parseWithCommon(Object[] params) {
        if (!DEBUG) {
            return "RELEASE";
        }
        String out = parse(params);
        return getCommonLog() + out;
    }

    private static String parse(Object[] params) {
        if (!DEBUG) {
            return "RELEASE";
        }
        StringBuilder builder = new StringBuilder();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                builder.append(param == null ? "null" : param);
                if (i != params.length - 1) {
                    builder.append(", ");
                }
            }
        }
        return builder.toString();
    }


    public static String getSimpleCaller() {
        if (!DEBUG) {
            return "RELEASE";
        }
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace == null && trace.length == 0) {
            return "null";
        } else if (trace.length < 5) {
            return getMethod(trace[trace.length - 1]);
        } else {
            return getMethod(trace[4]);
        }
    }

    public static String getCaller() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace == null && trace.length == 0) {
            return "null";
        } else if (trace.length < 5) {
            return getSignature(trace[trace.length - 1]);
        } else {
            return getSignature(trace[4]);
        }
    }

    private static String getSignature(StackTraceElement element) {
        if (element == null) {
            return "null";
        } else {
            return element.getClassName() + "." + element.getMethodName() + "()";
        }
    }


    private static String getMethod(StackTraceElement element) {
        if (element == null) {
            return "null";
        } else {
            return element.getMethodName() + "()";
        }
    }

    private static int sPid = -1;

    private static int getPid() {
        return sPid == -1 ? (sPid = android.os.Process.myPid()) : sPid;
    }

    private static String sProcessName = null;

    public static String getProcessName() {
        if (sProcessName != null) {
            return sProcessName;
        }
        int pid = getPid();
        try {
            Context context = BaseApp.getInstance();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return sProcessName = appProcess.processName;
                }
            }
        } catch (Throwable t) {
        }

        return sProcessName = String.valueOf(pid);
    }

    public void in() {
        i("[in]", getProcessName(), getCaller());
    }

    public void out() {
        i("[out]", getProcessName(), getCaller());
    }

    public static void in(String tag) {
        Log.i(tag, "[in], " + getProcessName() + ", " + getCaller());
    }

    public static void out(String tag) {
        Log.i(tag, "[out], " + getProcessName() + ", " + getCaller());
    }

    public void d(Object... param) {
        Log.d(mTag, parseWithCommon(param));
    }

    public void e(Object... param) {
        Log.i(mTag, parseWithCommon(param));
    }

    public void v(Object... param) {
        Log.v(mTag, parseWithCommon(param));
    }

    public void w(Object... param) {
        Log.w(mTag, parseWithCommon(param));
    }

    public void restart() {
        mStartTime = mLastTime = System.currentTimeMillis();
    }

    public void tick(String... message) {
        long now = System.currentTimeMillis();
        long total = now - mStartTime;
        long step = now - mLastTime;
        mLastTime = now;
        String method = "";
        String clazz = "";
        try {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            if (stack != null && stack.length > 4 + mStackLevel) {
                StackTraceElement element = stack[3 + mStackLevel];
                method = element.getMethodName();
                if (mShowClass) {
                    clazz = element.getClassName();
                    clazz = clazz.substring(clazz.lastIndexOf('.') + 1);
                }
            }
        } catch (Throwable e) {
        }
        i(String.format(Locale.CHINA, "tick[%s.%s](%d,%d): %s", clazz, method, total, step, parseWithCommon(message)));
    }

    public static void i0(Class<?> clazz, Object... param) {
        i0(clazz.getSimpleName(), param);
    }

    public static void i0(String tag, Object... param) {
        Log.i(tag, parse(param));
    }

    public static void ci0(String tag, Object... param) {
        Log.i(tag, getSimpleCaller() + ": " + parse(param));
    }

    public static void ci0(Class clazz, Object... param) {
        Log.i(clazz.getSimpleName(), getSimpleCaller() + ": " + parse(param));
    }

    public static void e0(String tag, Object... param) {
        Log.e(tag, parse(param));
    }

    public static void v0(String tag, Object... param) {
        Log.v(tag, parse(param));
    }

    public static void w0(String tag, Object... param) {
        Log.w(tag, parse(param));
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
