package guru.ioio.testtool2.rv

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import guru.ioio.testtool2.databinding.ActivityRecyclerBinding

class RecyclerActivity : Activity() {
    private val mBinding by lazy { ActivityRecyclerBinding.inflate(LayoutInflater.from(this)) }
    private val mAdapter by lazy { RvAdapter() }
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
            for (i in 0..10) {
                mAdapter.addData((mAdapter.data.maxOrNull() ?: 0) + 1)
            }
        }
        mBinding.addFrontBtn.setOnClickListener {
            for (i in 0..10) {
                mAdapter.addData(0, (mAdapter.data.minOrNull() ?: 0) - 1)
            }
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
}