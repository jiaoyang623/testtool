package guru.ioio.testtool2.rv

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import guru.ioio.testtool2.R

class RvAdapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_rv),LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: Int) {
        holder.setText(R.id.content, item.toString())
    }
}