package guru.ioio.testtool2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.battery.metrics.cpu.CpuMetricsCollector
import guru.ioio.testtool2.utils.Logger
import guru.ioio.testtool2.utils.RVBindingBaseAdapter
import kotlinx.android.synthetic.main.activity_fragment_stack.*
import kotlin.math.max

class FragmentStackActivity : FragmentActivity() {
    companion object {
        private val sCollector: CpuMetricsCollector = CpuMetricsCollector()
    }

    private val mLogger = Logger(FragmentStackActivity::class.java.simpleName)
    private val mInitialMetrics = sCollector.createMetrics()
    private val mFinalMetrics = sCollector.createMetrics()

    private val mAdapter = RVBindingBaseAdapter<Fragment>(R.layout.item_fragment_stack, BR.data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_stack)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = mAdapter
        mAdapter.addPresenter(BR.presenter, this)

        push_btn.setOnClickListener {
            val name = "f.${supportFragmentManager.fragments.size + 1}"
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    newFragment(name)
                )
                .commit()

            mAdapter.set(supportFragmentManager.fragments)
        }
        pop_btn.setOnClickListener {
            val list = supportFragmentManager.fragments
            if (list.isNotEmpty()) {
                val trans = supportFragmentManager.beginTransaction()
                val count = max(1, count_edit.text.toString().toInt())
                for (f in list.subList(list.size - count, list.size)) {
                    trans.remove(f)
                }

                trans.commitNow()
                mAdapter.set(supportFragmentManager.fragments)
            }
        }

    }

    private fun newFragment(content: String): Fragment {
        val f = SampleFragment(R.layout.fragment_stack)
        f.mainText = content
        return f
    }

    fun pop(fragment: SampleFragment): Boolean {
        supportFragmentManager.beginTransaction().remove(fragment).commitNow()
        mAdapter.set(supportFragmentManager.fragments)
        return true
    }

    fun show(fragment: SampleFragment): Boolean {
        supportFragmentManager.beginTransaction().show(fragment).commitNow()
        mAdapter.set(supportFragmentManager.fragments)
        return true
    }

    fun hide(fragment: SampleFragment): Boolean {
        supportFragmentManager.beginTransaction().hide(fragment).commitNow()
        mAdapter.set(supportFragmentManager.fragments)
        return true
    }

    override fun onResume() {
        super.onResume()
        sCollector.getSnapshot(mInitialMetrics)
    }

    override fun onPause() {
        super.onPause()
        sCollector.getSnapshot(mFinalMetrics)
        mLogger.ci(mFinalMetrics.diff(mInitialMetrics))
    }
}

class SampleFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    var mainText: String? = null
    private val mLogger = Logger("SampleFragment")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        v!!.setBackgroundResource(android.R.color.holo_blue_bright)
        v!!.findViewById<TextView>(R.id.fragment_discription).text = mainText
        return v
    }

    override fun onPause() {
        super.onPause()
        mLogger.ci(mainText)

    }

    override fun onResume() {
        super.onResume()
        mLogger.ci(mainText)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mLogger.ci(mainText, hidden)
    }
}

