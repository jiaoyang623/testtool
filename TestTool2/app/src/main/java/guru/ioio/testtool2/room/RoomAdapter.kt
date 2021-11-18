package guru.ioio.testtool2.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import guru.ioio.testtool2.R
import guru.ioio.testtool2.db.model.ImageBean

class RoomAdapter : RecyclerView.Adapter<RoomViewHolder>() {
    private var mList: MutableList<ImageBean> = mutableListOf()

    fun setData(list: List<ImageBean>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_room_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun getItemCount() = mList.size

}

class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image = view.findViewById<ImageView>(R.id.image)
    val title = view.findViewById<TextView>(R.id.title)

    fun bindData(imageBean: ImageBean) {
        title.text = imageBean.title
        Glide.with(image).load(imageBean.image).into(image)
    }
}