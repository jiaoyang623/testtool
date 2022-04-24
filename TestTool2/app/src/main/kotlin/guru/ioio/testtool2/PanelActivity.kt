package guru.ioio.testtool2

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import guru.ioio.testtool2.databinding.ActivityPanelBinding
import guru.ioio.testtool2.utils.*
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

        initPrint()

        initImei()

        initRefresh()
    }


    private var mCount = 0
    private val mLoopRunner = object : Runnable {
        override fun run() {
            mCount++
            refresh_text.text = mCount.toString()
            mHandler.removeCallbacks(this)
            mHandler.postDelayed(this, 1000)
            mLogger.ci(mCount)
        }
    }
    private var mLock: PowerManager.WakeLock? = null


    private fun initRefresh() {

        refresh_looper.setOnCheckedChangeListener { _, isChecked ->
            mHandler.removeCallbacks(mLoopRunner)
            if (isChecked) {
                mHandler.postDelayed(mLoopRunner, 1000)
            }
        }
        refresh_lock.setOnCheckedChangeListener { _, isChecked ->
            if (mLock == null) {
                mLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(
                    PowerManager.ON_AFTER_RELEASE,
                    "guru.ioio.testtool2:WakeLock"
                )
            }
            if (isChecked) {
                mLock!!.acquire()
            } else {
                mLock!!.release()
            }
        }
    }

    private fun initImei() {
        imei_btn.setOnClickListener {
            val wifiManager =
                applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val mac = wifiManager.connectionInfo.macAddress
            mLogger.ci(mac, JavaUtils().testMessage)
        }
    }


    private fun initPrint() {
        print_btn.setOnClickListener {
            val count = print_time_et.text.toString()
            val c = count.toInt()
            val builder = StringBuilder()
            for (i in 1..c) {
                builder.clear()
                for (i in 1..4) {
                    builder.append("print $i ${System.currentTimeMillis()} ${System.nanoTime()}\n")
                }
                Log.i(
                    "PanelActivity",
                    builder.toString()
                )
            }
        }
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