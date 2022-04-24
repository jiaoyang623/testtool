package guru.ioio.testtool2

import android.app.Activity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mengpeng.recyclerviewgallery.CarouselLayoutManager
import com.mengpeng.recyclerviewgallery.CarouselZoomPostLayoutListener
import com.mengpeng.recyclerviewgallery.CenterScrollListener
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : Activity() {
    private val mAdapter by lazy {
        GalleryAdapter().apply {
            val list = mutableListOf<String>()
            for (i in 0..40) {
                list.add("https://ichef.bbci.co.uk/news/976/cpsprodpb/15951/production/_117310488_16.jpg")
            }
            setList(list)
            onItemClickListener = { p ->
                gallery.smoothScrollToPosition(p) // 滑动
            }
        }
    }

    private val mLayoutManager by lazy {
        object : CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL) {}.apply {
            setPostLayoutListener(CarouselZoomPostLayoutListener())
            addOnItemSelectionListener { // 选中
                result.text = "selected: $it"
            }
            maxVisibleItems =5
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        gallery.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            addOnScrollListener(CenterScrollListener())
            adapter = mAdapter
        }

        left.setOnClickListener {
            val p = mLayoutManager.centerItemPosition
            if (p - 1 >= 0) {
                gallery.smoothScrollToPosition(p - 1)
            }
        }
        right.setOnClickListener {
            val p = mLayoutManager.centerItemPosition
            if (p + 1 < mAdapter.itemCount) {
                gallery.smoothScrollToPosition(p + 1)
            }

        }
    }
}

class GalleryAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_gallery) {

    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context).load(item).into(holder.getView(R.id.image))
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(holder.adapterPosition)
        }
    }

    var onItemClickListener: ((Int) -> Unit)? = null

}

