package guru.ioio.testtool2

import android.app.Activity
import android.os.Bundle
import guru.ioio.testtool2.utils.Logger
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.*

class CoroutineActivity : Activity() {
    private val mLogger = Logger(CoroutineActivity::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        basic.setOnClickListener {
            GlobalScope.launch {
                showMessage("click")
            }
        }

        suspend_btn.setOnClickListener {
            mLogger.ci(1, Thread.currentThread().name)
            GlobalScope.launch(Dispatchers.Main) {
                mLogger.ci(2, Thread.currentThread().name)
                showMessage(suspendAction())
            }
        }
    }

    private suspend fun suspendAction(): String {
        mLogger.ci(3, Thread.currentThread().name)
        var msg = "none"
        withContext(Dispatchers.IO) {
            mLogger.ci(4, Thread.currentThread().name)
            delay(1000)
            msg = "io"
            mLogger.ci(5, Thread.currentThread().name)
        }
        mLogger.ci(6, Thread.currentThread().name)

        return msg;
    }

    private fun showMessage(message: String) {
        mLogger.ci(Thread.currentThread().name)
        result.text = message
    }
}