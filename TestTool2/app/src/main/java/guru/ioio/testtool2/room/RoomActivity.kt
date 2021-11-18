package guru.ioio.testtool2.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import guru.ioio.testtool2.R
import guru.ioio.testtool2.db.model.ImageBean
import kotlinx.android.synthetic.main.activity_room.*

class RoomActivity : AppCompatActivity() {
    private val mViewModel = RoomViewModel()
    private val mAdapter = RoomAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        initListener()
        initRecycler()
    }

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = mAdapter
        mViewModel.imageList.observe(this) {
            mAdapter.setData(it)
        }
    }

    private fun initListener() {
        add.setOnClickListener {
            mViewModel.addImage(
                ImageBean(
                    "image-${mViewModel.imageList.value?.size ?: 0}",
                    "https://wallpapershome.com/images/pages/pic_v/11849.jpg"
                )
            )
        }

        remove.setOnClickListener {
            mViewModel.imageList.value?.last()?.let {
                mViewModel.removeImage(it)
            }
        }

    }
}