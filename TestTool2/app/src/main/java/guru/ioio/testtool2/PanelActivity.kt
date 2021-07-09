package guru.ioio.testtool2

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import guru.ioio.testtool2.databinding.ActivityPanelBinding
import guru.ioio.testtool2.utils.ActivityUtils
import guru.ioio.testtool2.utils.KeyboardUtils
import guru.ioio.testtool2.utils.Logger
import guru.ioio.testtool2.utils.NotificationHelper
import kotlinx.android.synthetic.main.activity_panel.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class PanelActivity : AppCompatActivity() {
    private val mLogger = Logger(PanelActivity::class.simpleName)

    private lateinit var mBinding: ActivityPanelBinding;
    private val mHandler = Handler(Looper.getMainLooper())
    private val mProxyHandler =
        InvocationHandler() { any: Any, method: Method, arrayOfAnys: Array<Any> ->
            method.invoke(window.callback, *arrayOfAnys)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var mNotificationHelper: NotificationHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNotificationHelper = NotificationHelper(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_panel)
        mBinding.presenter = this
        gta_btn.setOnClickListener {
            postDelay(5000) {
                val name = ActivityUtils.getTopActivityName(this@PanelActivity)
                if (name == null) {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                } else {
                    top_activity_edit.setText(
                        name,
                        TextView.BufferType.EDITABLE
                    )
                }
            }
        }

        initCoroutine()
    }

    val handler = Handler(Looper.getMainLooper())

    private fun postDelay(delay: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delay)
    }

    fun onSelectAllClick(v: View): Boolean {
        input.requestFocus()
        input.selectAll()
        return true
    }

    fun onChangeModeClick(v: View): Boolean {
        when (v) {
            pan -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            resize -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            nothing -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        return true
    }

    private var isInputUp = false
    private val mGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
        mLogger.ci("onGlobalLayoutChanged")
        mHandler.post {
            val isUp = KeyboardUtils.isShowing(mBinding.root)
            if (isUp == isInputUp) {
                return@post
            }
            if (isUp) {
//                popUp()
            } else {
                pushDown()
            }
        }
    }

    private fun popUp() {
        val startY = 0f
        val endY = -500f
        val anim = ObjectAnimator.ofFloat(bottom_input, "translationY", startY, endY)
        anim.start()
    }

    private fun pushDown() {

    }

    fun notificationLevel(isMax: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationHelper.sendWithChannel(
                this,
                if (isMax) NotificationHelper.CHANNEL_MAX else NotificationHelper.CHANNEL_MIN,
                if (isMax) 2 else 3
            )
        }
        return true
    }

    fun notificationNow(isNow: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationHelper.sendLater(this, if (isNow) 0 else 1, !isNow)
        }
        return true
    }

    private fun initCoroutine() {
        coroutine_start.setOnClickListener {
            GlobalScope.launch {
                mLogger.ci(Thread.currentThread().name)
            }
        }
    }
}