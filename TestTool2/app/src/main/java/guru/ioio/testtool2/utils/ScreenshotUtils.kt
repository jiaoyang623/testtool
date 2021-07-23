package guru.ioio.testtool2.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import guru.ioio.testtool2.BaseApp
import guru.ioio.testtool2.R
import java.io.FileOutputStream

class ScreenshotUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun takeShot(root: View, listener: ITakeShot): Bitmap? {
//            return root.drawingCache
            val loc = intArrayOf(0, 0)
            root.getLocationInWindow(loc)
            val bitmap = Bitmap.createBitmap(root.width, root.height, Bitmap.Config.RGB_565)
            val window = (root.context as Activity).window
            PixelCopy.request(
                window,
                Rect(loc[0], loc[1], loc[0] + root.width, loc[1] + root.height),
                bitmap,
                { result ->
                    listener.onTaken(if (result == PixelCopy.SUCCESS) bitmap else null)
                },
                Handler(Looper.myLooper()!!)
            )
            return BitmapFactory.decodeResource(BaseApp.getInstance().resources, R.drawable.bing)
        }

        fun save(path: String, bitmap: Bitmap?) {
            if (bitmap != null) {
                val fos = FileOutputStream(path)
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, fos)
                Utils.close(fos)
            }
        }
    }
}

interface ITakeShot {
    fun onTaken(bitmap: Bitmap?)
}