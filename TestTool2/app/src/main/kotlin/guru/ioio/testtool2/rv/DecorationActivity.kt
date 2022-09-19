package guru.ioio.testtool2.rv

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import guru.ioio.testtool2.R
import guru.ioio.testtool2.databinding.ActivityDecorationBinding

class DecorationActivity : Activity() {
    private lateinit var mBinding: ActivityDecorationBinding
    private val mAdapter by lazy { DecorationAdapter() }
    private val mLayoutManager by lazy {
        GridLayoutManager(
            this,
            3,
            GridLayoutManager.VERTICAL,
            false
        )
    }
    private val mDecoration by lazy {
        SpaceDecoration().apply {
            edgeRect.apply {
                left = 20
                right = 20
                top = 40
                bottom = 40
            }
            horizontalSpace = 10
            verticalSpace = 10
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDecorationBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.recycler.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            addItemDecoration(mDecoration)
        }

        val list = mutableListOf<String>()
        for (i in 0..50) {
            list.add(i.toString())
        }
        mAdapter.setList(list)
    }
}

class DecorationAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_decoration) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.content, item)
    }
}

