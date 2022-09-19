package guru.ioio.testtool2.rv

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceDecoration : RecyclerView.ItemDecoration() {
    val edgeRect = Rect(0, 0, 0, 0)
    var horizontalSpace = 0
    var verticalSpace = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.layoutManager) {
            is GridLayoutManager -> {
                parseItemOffsetGrid(
                    outRect, view, parent, state,
                    parent.layoutManager as GridLayoutManager
                )
            }
            is LinearLayoutManager -> {
                parseItemOffsetLinear(
                    outRect, view, parent, state,
                    parent.layoutManager as LinearLayoutManager
                )
            }
        }
    }

    private fun parseItemOffsetLinear(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        manager: LinearLayoutManager,
    ) {
        manager.getPosition(view)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount

        if (position == RecyclerView.NO_POSITION || itemCount == null) return
        if (itemCount == 1) {
            outRect.set(edgeRect)
            return
        }

        if (manager.orientation == LinearLayoutManager.VERTICAL) {// 竖
            when (position) {
                0 -> {
                    outRect.set(edgeRect.left, edgeRect.top, edgeRect.right, verticalSpace / 2)
                }
                itemCount -> {
                    outRect.set(edgeRect.left, verticalSpace / 2, edgeRect.right, edgeRect.bottom)
                }
                else -> {
                    outRect.set(edgeRect.left, verticalSpace / 2, edgeRect.right, verticalSpace / 2)
                }
            }

        } else { // 横
            when (position) {
                0 -> {
                    outRect.set(edgeRect.left, edgeRect.top, horizontalSpace / 2, edgeRect.bottom)
                }
                itemCount -> {
                    outRect.set(horizontalSpace / 2, edgeRect.top, edgeRect.right, edgeRect.bottom)
                }
                else -> {
                    outRect.set(
                        horizontalSpace / 2,
                        edgeRect.top,
                        horizontalSpace / 2,
                        edgeRect.bottom
                    )
                }
            }

        }
    }

    private fun parseItemOffsetGrid(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        manager: GridLayoutManager,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount
        val spanCount = manager.spanCount

        if (position == RecyclerView.NO_POSITION || itemCount == null) return

        // 假设没有多span
        if (manager.orientation == GridLayoutManager.VERTICAL) { // vertical
            outRect.set(
                horizontalSpace / 2,
                verticalSpace / 2,
                horizontalSpace / 2,
                verticalSpace / 2
            )
            // left
            if (position % spanCount == 0) {
                outRect.left = edgeRect.left
            }
            // right
            if ((position + 1) % spanCount == 0) {
                outRect.right = edgeRect.right
            }

            // top
            if (position < spanCount) {
                outRect.top = edgeRect.top
            }
            // bottom
            if (position / spanCount == (itemCount - 1) / spanCount) {
                outRect.bottom = edgeRect.bottom
            }

        } else { // horizontal
            outRect.set(
                horizontalSpace / 2,
                verticalSpace / 2,
                horizontalSpace / 2,
                verticalSpace / 2
            )
            // top
            if (position % spanCount == 0) {
                outRect.top = edgeRect.top
            }
            // bottom
            if ((position + 1) % spanCount == 0) {
                outRect.bottom = edgeRect.bottom
            }

            // left
            if (position < spanCount) {
                outRect.left = edgeRect.left
            }
            // right
            if (position / spanCount == (itemCount - 1) / spanCount) {
                outRect.right = edgeRect.right
            }
        }
        Log.i("DA", "$position, $outRect")
    }
}