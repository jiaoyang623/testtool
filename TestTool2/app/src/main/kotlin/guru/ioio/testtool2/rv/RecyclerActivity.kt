package guru.ioio.testtool2.rv

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import guru.ioio.testtool2.databinding.ActivityRecyclerBinding

class RecyclerActivity : Activity() {
    private val mBinding by lazy { ActivityRecyclerBinding.inflate(LayoutInflater.from(this)) }
    private val mAdapter by lazy {
        RvAdapter().apply {
            loadMoreModule.run {
                isEnableLoadMore = true
                enableLoadMoreEndClick = false
                isAutoLoadMore = true
                setOnLoadMoreListener {
                    addEnd()
                }
//            loadMoreView = CustomLoadMoreView()
            }
        }
    }
    private val mLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.recycler.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
        mBinding.addEndBtn.setOnClickListener {
            addEnd()
        }
        mBinding.addFrontBtn.setOnClickListener {
            addFront()
        }
        mBinding.clearBtn.setOnClickListener {
            mAdapter.setList(emptyList())
        }
        mBinding.rlCb.apply {
            isChecked = mLayoutManager.reverseLayout
            setOnCheckedChangeListener { _, isChecked ->
                mLayoutManager.reverseLayout = isChecked
            }
        }
        mBinding.sfeCb.apply {
            isChecked = mLayoutManager.stackFromEnd
            setOnCheckedChangeListener { _, isChecked ->
                mLayoutManager.stackFromEnd = isChecked
            }

        }
    }

    private fun addFront() {
        val end = (mAdapter.data.minOrNull() ?: 0)
        val list = mutableListOf<Int>()
        for (i in end - 10 until end) {
            list.add(i)
        }
        mAdapter.addData(0, list)
        mAdapter.loadMoreModule.loadMoreComplete()
    }

    private fun addEnd() {
        val start = mAdapter.data.maxOrNull() ?: 0 + 1
        val list = mutableListOf<Int>()
        for (i in start until start + 10) {
            list.add(i)
        }
        mAdapter.addData(list)
        mAdapter.loadMoreModule.loadMoreComplete()
    }
}