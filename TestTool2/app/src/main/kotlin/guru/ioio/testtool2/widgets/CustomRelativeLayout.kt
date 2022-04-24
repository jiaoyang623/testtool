package guru.ioio.testtool2.widgets

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import guru.ioio.testtool2.utils.KeyboardUtils
import guru.ioio.testtool2.utils.Logger

class CustomRelativeLayout : RelativeLayout {
    private val mLogger = Logger(CustomRelativeLayout::class.simpleName)
    private val mHandler = Handler(Looper.getMainLooper())
    private val mLocation: IntArray = intArrayOf(-1, -1)

    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)

    constructor(context: Context, attributes: AttributeSet, style: Int) : super(
        context,
        attributes,
        style
    )

    private val printRunnable = Runnable {
        val y = mLocation[1]
        getLocationInWindow(mLocation)
        if (y != mLocation[1]) {
            mLogger.ci("location_changed", mLocation[1])
        }

        postDelay()
    }

    private fun postDelay() {
        mHandler.removeCallbacks(printRunnable)
        mHandler.postDelayed(printRunnable, 10)
    }



    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mHandler.removeCallbacks(printRunnable)
        mHandler.post(printRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacks(printRunnable)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mLogger.ci(l, t, r, b)
    }
}