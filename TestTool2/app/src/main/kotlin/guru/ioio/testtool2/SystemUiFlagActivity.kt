package guru.ioio.testtool2

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_fullscreen.*

class SystemUiFlagActivity : Activity() {
    private val mAdapter by lazy {
        SystemUiFlagAdapter().apply {
            setList(
                listOf(
                    SystemUiFlagBean(View.SYSTEM_UI_FLAG_VISIBLE, "SYSTEM_UI_FLAG_VISIBLE"),
                    SystemUiFlagBean(View.SYSTEM_UI_FLAG_LOW_PROFILE, "SYSTEM_UI_FLAG_LOW_PROFILE"),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,
                        "SYSTEM_UI_FLAG_HIDE_NAVIGATION"
                    ),
                    SystemUiFlagBean(View.SYSTEM_UI_FLAG_FULLSCREEN, "SYSTEM_UI_FLAG_FULLSCREEN"),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE,
                        "SYSTEM_UI_FLAG_LAYOUT_STABLE"
                    ),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
                        "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION"
                    ),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN,
                        "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN"
                    ),
                    SystemUiFlagBean(View.SYSTEM_UI_FLAG_IMMERSIVE, "SYSTEM_UI_FLAG_IMMERSIVE"),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY,
                        "SYSTEM_UI_FLAG_IMMERSIVE_STICKY"
                    ),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR,
                        "SYSTEM_UI_FLAG_LIGHT_STATUS_BAR"
                    ),
                    SystemUiFlagBean(
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR,
                        "SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR"
                    ),
                )
            )

            onCheckedListener = { _, _ ->
                setListFlag()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        recycler.adapter = mAdapter
        set_btn.setOnClickListener {
            setListFlag()
        }

    }

    private fun setListFlag() {
        var flag = 0
        mAdapter.data.filter { it.isChecked }.forEach { bean ->
            flag = flag or bean.id
        }
        window.decorView.systemUiVisibility = flag
    }
}

class SystemUiFlagAdapter :
    BaseQuickAdapter<SystemUiFlagBean, BaseViewHolder>(R.layout.item_fullscreen) {
    override fun convert(holder: BaseViewHolder, item: SystemUiFlagBean) {
        holder.getView<CheckBox>(R.id.select_cb).let {
            it.isChecked = item.isChecked
            it.text = item.title
            it.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onCheckedListener?.invoke(item, isChecked)
            }
        }
    }

    var onCheckedListener: ((SystemUiFlagBean, Boolean) -> Unit)? = null
}

data class SystemUiFlagBean(
    val id: Int,
    val title: String,
    var isChecked: Boolean = false,
)