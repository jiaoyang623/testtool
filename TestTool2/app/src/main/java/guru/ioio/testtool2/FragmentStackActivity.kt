package guru.ioio.testtool2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import guru.ioio.testtool2.utils.RVBindingBaseAdapter
import kotlinx.android.synthetic.main.activity_fragment_stack.*
import kotlin.math.max

class FragmentStackActivity : FragmentActivity() {
    private val mAdapter = RVBindingBaseAdapter<Fragment>(R.layout.item_fragment_stack, BR.data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_stack)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = mAdapter
        mAdapter.addPresenter(BR.presenter, this)

        push_btn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    newFragment("f.${supportFragmentManager.fragments.size + 1}")
                )
                .commitNow()
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

    fun onItemClick(fragment: SampleFragment): Boolean {
        supportFragmentManager.beginTransaction().remove(fragment).commitNow()
        mAdapter.set(supportFragmentManager.fragments)
        return true
    }
}

class SampleFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    var mainText: String? = null
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
}
