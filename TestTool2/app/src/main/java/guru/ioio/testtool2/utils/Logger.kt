package guru.ioio.testtool2.utils

import android.util.Log

class Logger {
    private val mTag: String
    private var mCommonLogStr: String = ""
    private val mCommonLog: MutableList<String> = mutableListOf()

    constructor(tag: String?) {
        mTag = tag ?: "default_tag"
    }

    fun i(vararg params: Any) {
        Log.i(mTag, parseWithCommon(params))
    }

    fun ci(vararg params: Any) {
        Log.i(mTag, getSimpleCaller() + ": " + parseWithCommon(params))
    }


    private fun parseWithCommon(params: Array<out Any>): String {
        return getCommonLog() + parse(params)
    }

    private fun getCommonLog(): String {
        if (mCommonLogStr == "") {
            val builder = StringBuilder()
            for (p in mCommonLog) {
                builder.append(p).append(", ")
            }
            mCommonLogStr = builder.toString()
        }

        return mCommonLogStr
    }

    companion object {
        fun parse(params: Array<out Any>): String {
            val builder = StringBuilder()
            takeIf { params.isNotEmpty() }.let {
                for (p in params) {
                    builder.append(p).append(", ")
                }
            }
            return builder.toString()
        }

        private fun getSimpleCaller(): String {
            val trace = Thread.currentThread().stackTrace
            return when {
                trace.isEmpty() -> {
                    "null"
                }
                trace.size < 5 -> {
                    getMethod(trace[trace.size - 1])
                }
                else -> {
                    getMethod(trace[4])
                }
            }
        }

        private fun getMethod(stackTraceElement: StackTraceElement?): String {
            return stackTraceElement?.methodName + "()"
        }
    }
}