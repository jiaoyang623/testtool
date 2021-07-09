package guru.ioio.testtool2.utils

import android.graphics.Rect
import android.view.View
import kotlin.math.abs

class KeyboardUtils {
    companion object {
        fun isShowing(view: View): Boolean {
            val rootView = view.rootView ?: return false
            val appRect = Rect()
            rootView.getWindowVisibleDisplayFrame(appRect)
            val density = view.context.resources.displayMetrics.density
            val bottomMargin = abs(rootView.height - appRect.height()) / density

            return bottomMargin > 100
        }
    }
}