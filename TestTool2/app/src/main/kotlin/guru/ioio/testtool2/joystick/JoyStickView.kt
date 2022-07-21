package guru.ioio.testtool2.joystick

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class JoyStickView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var onKeyEvent: ((keyCode: Int, isDown: Boolean) -> Unit)? = null

    val keyCodeLeft = 37
    val keyCodeUp = 38
    val keyCodeRight = 39
    val keyCodeDown = 40

    private val mPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
    }

    private var mIsPressed = false
    private var mX = 0f
    private var mY = 0f
    private var mRadius = 0f
    private var mCenterRadius = 0f
    private var mTriangleHeight = 0f
    private var mTriangleBottom = 0f
    private var mPath = Path()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mRadius = width.coerceAtMost(height) / 2f
        mCenterRadius = mRadius / 3
        mTriangleHeight = mRadius / 7.5f
        mTriangleBottom = height / 5f
        mPath = Path()
        mPath.moveTo(width / 2 - mTriangleHeight, mTriangleBottom)
        mPath.lineTo(width / 2 + mTriangleHeight, mTriangleBottom)
        mPath.lineTo(width / 2f, mTriangleBottom - mTriangleHeight)
        mPath.close()
        print("w=$width, h=$height, th=$mTriangleHeight, tb=$mTriangleBottom")

        if (!mIsPressed) {
            clearTouch()
        }
    }

    private fun clearTouch() {
        mIsPressed = false
        mX = width / 2f
        mY = height / 2f
        sendEvent()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsPressed = true
                move(event)
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                move(event)
                clearTouch()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                move(event)
                invalidate()
            }
        }
        return true
    }

    private fun move(event: MotionEvent) {
        val x = event.x - width / 2f
        val y = event.y - height / 2f
        val degree = atan2(x.toDouble(), y.toDouble())
        val r = mRadius - mCenterRadius
        val xy2 = x * x + y * y
        when {
            xy2 < mCenterRadius * mCenterRadius -> {
                mX = event.x
                mY = event.y
                sendEvent()
            }
            xy2 < r * r -> {
                mX = event.x
                mY = event.y
                mapEvent(degree)
            }
            else -> {
                mX = (r * sin(degree)).toFloat() + width / 2
                mY = (r * cos(degree)).toFloat() + height / 2
                mapEvent(degree)
            }
        }
    }

    private fun mapEvent(degree: Double) {
        when {
            degree > 7f / 8 * PI || degree <= -7f / 8 * PI -> sendEvent(up = true) // 上
            degree <= -5f / 8 * PI -> sendEvent(left = true, up = true) // 左上
            degree <= -3f / 8 * PI -> sendEvent(left = true) // 左
            degree <= -1f / 8 * PI -> sendEvent(left = true, down = true) // 左下
            degree <= 1f / 8 * PI -> sendEvent(down = true) // 下
            degree <= 3f / 8 * PI -> sendEvent(right = true, down = true)  // 右下
            degree <= 5f / 8 * PI -> sendEvent(right = true)  // 右
            degree <= 7f / 8 * PI -> sendEvent(right = true, up = true)  // 右上
        }
    }

    private var mUp: Boolean = false
    private var mDown: Boolean = false
    private var mLeft: Boolean = false
    private var mRight: Boolean = false

    private fun sendEvent(
        up: Boolean = false,
        down: Boolean = false,
        left: Boolean = false,
        right: Boolean = false
    ) {
        if (up != mUp) {
            mUp = up
            onKeyEvent?.invoke(keyCodeUp, up)
        }
        if (down != mDown) {
            mDown = down
            onKeyEvent?.invoke(keyCodeDown, down)
        }
        if (left != mLeft) {
            mLeft = left
            onKeyEvent?.invoke(keyCodeLeft, left)
        }
        if (right != mRight) {
            mRight = right
            onKeyEvent?.invoke(keyCodeRight, right)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        val cx = width / 2f
        val cy = height / 2f

        // background
        mPaint.color = Color.argb(if (mIsPressed) 51 else 31, 0, 0, 0)
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, mRadius, mPaint)
        if (mIsPressed) {
            mPaint.strokeWidth = 1f
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.WHITE
            canvas.drawCircle(cx, cy, mRadius, mPaint)
        }

        // joystick
        mPaint.color = Color.argb(if (mIsPressed) 225 else 164, 0xff, 0xff, 0xff)
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(mX, mY, mCenterRadius, mPaint)

        // triangle
        drawTriangle(canvas, 0f, mUp)
        drawTriangle(canvas, 90f, mRight)
        drawTriangle(canvas, 90f, mDown)
        drawTriangle(canvas, 90f, mLeft)
    }

    private fun drawTriangle(canvas: Canvas, degree: Float, isPressed: Boolean) {
        canvas.rotate(degree, width / 2f, height / 2f)
        mPaint.color = Color.argb(if (isPressed) 255 else 82, 0xff, 0xff, 0xff)
        canvas.drawPath(mPath, mPaint)
    }

    private fun print(vararg data: Any) {
        Log.i("JSV", data.joinToString(", "))
    }
}