package guru.ioio.testtool2.utils

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import guru.ioio.testtool2.utils.RVBindingBaseAdapter.RViewHolder
import java.util.*

/**
 * Created by jiaoyang on 16/06/2017.
 * base adapter for RecyclerView.Adapter
 */
class RVBindingBaseAdapter<T> : RecyclerView.Adapter<RViewHolder> {
    private val sValueId: Int
    private var mList: MutableList<T>? = ArrayList()
    private val mPresenterMap: MutableMap<Int, Any> = HashMap()
    private var mTypeMap: SparseIntArray

    constructor(layoutId: Int, valueId: Int) {
        mTypeMap = SparseIntArray()
        mTypeMap.put(super.getItemViewType(0), layoutId)
        sValueId = valueId
    }

    constructor(typeMap: SparseIntArray, valueId: Int) {
        mTypeMap = typeMap
        sValueId = valueId
    }

    fun set(data: MutableList<T>?): RVBindingBaseAdapter<T> {
        mList?.clear()
        if (data != null) {
            mList?.addAll(checkList(data)!!)
            notifyDataSetChanged()
        }
        return this
    }

    private fun checkType(t: T): Boolean {
        return if (t is IType) {
            mTypeMap[(t as IType).type] != 0
        } else {
            true
        }
    }

    private fun checkList(list: MutableList<T>?): List<T>? {
        if (list != null && list.size > 0) {
            val removeList: MutableList<T> = ArrayList()
            for (t in list) {
                if (!checkType(t)) {
                    removeList.add(t)
                }
            }
            list.removeAll(removeList)
        }
        return list
    }

    fun addToTail(t: T): RVBindingBaseAdapter<T> {
        return add(t, mList!!.size)
    }

    fun addToHead(t: T): RVBindingBaseAdapter<T> {
        return add(t, 0)
    }

    fun add(t: T): RVBindingBaseAdapter<T> {
        add(t, mList!!.size)
        return this
    }

    fun add(t: T?, position: Int): RVBindingBaseAdapter<T> {
        Utils.notNull(t, mList) {
            if (mList!!.contains(t) && position < mList!!.size && checkType(t!!)) {
                mList?.add(position, t!!)
                notifyDataSetChanged()
            }
        }
        return this
    }

    @JvmOverloads
    fun add(data: MutableList<T>?, position: Int = mList!!.size): RVBindingBaseAdapter<T> {
        if (data == null) {
            return this
        }
        mList?.let {
            data.removeAll(it)
        }
        checkList(data)
        if (data.size > 0) {
            mList!!.addAll(position, data)
            notifyItemRangeInserted(position, data.size)
        }
        return this
    }

    val list: List<T>?
        get() = mList

    fun remove(position: Int): RVBindingBaseAdapter<T> {
        if (position in 0 until itemCount) {
            mList?.run {
                removeAt(position)
                notifyDataSetChanged()
            }
        }
        return this
    }

    fun remove(t: T?): RVBindingBaseAdapter<T> {
        mList?.let {
            remove(indexOf(t))
        }
        return this
    }

    fun indexOf(t: T?): Int {
        return mList?.indexOf(t) ?: -1
    }

    fun clear() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    fun addToHead(data: MutableList<T>?): RVBindingBaseAdapter<T> {
        add(data, 0)
        return this
    }

    fun addToTail(data: MutableList<T>?): RVBindingBaseAdapter<T> {
        add(data, mList!!.size)
        return this
    }

    fun addPresenter(variableId: Int, presenter: Any): RVBindingBaseAdapter<T> {
        mPresenterMap[variableId] = presenter
        return this
    }

    fun removePresenter(variableId: Int): RVBindingBaseAdapter<T> {
        if (mPresenterMap.containsKey(variableId)) {
            mPresenterMap.remove(variableId)
        }
        return this
    }

    override fun onViewDetachedFromWindow(holder: RViewHolder) {
        val pos = holder.adapterPosition
        for (l in mOnItemThroughListeners) {
            l.onOut(pos)
        }
    }

    override fun onViewAttachedToWindow(holder: RViewHolder) {
        val pos = holder.adapterPosition
        for (l in mOnItemThroughListeners) {
            l.onIn(pos)
        }
    }

    private var mAdaptArgs = arrayOfNulls<Any>(0)

    fun setAdaptArguments(vararg args: Any?): RVBindingBaseAdapter<T> {
        mAdaptArgs = arrayOf(args)
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), mTypeMap[viewType], parent, false
        )
        binding.root.tag = binding
        return RViewHolder(binding, sValueId, mPresenterMap)
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        mList?.let {
            if (it.size > position) {
                val t = it[position]
                if (t is Adaptable<*>) {
                    // FIXME
//                    t.adapt(position, it, holder.binding, *mAdaptArgs)
                }
                holder.bind(t)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        mList?.let {
            if (it.size > position) {
                val t = it[position]
                if (t is IType) {
                    return@getItemViewType (t as IType).type
                }
            }
        }
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    fun getItem(position: Int): T? {
        mList?.let {
            if (it.size > 0 && position in 0 until it.size) {
                return@getItem it[position]
            }
        }
        return null
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class RViewHolder : RecyclerView.ViewHolder {
        var binding: ViewDataBinding? = null
        private var valueId = 0
        private var presenterMap: Map<Int, Any>? = null

        constructor(binding: ViewDataBinding, valueId: Int, presenterMap: Map<Int, Any>?) : super(
            binding.root
        ) {
            this.binding = binding
            this.valueId = valueId
            this.presenterMap = presenterMap
        }

        constructor(v: View?) : super(v!!) {}

        fun bind(t: Any?) {
            if (t is Bindable) {
                t.binding = binding
            }
            binding!!.setVariable(valueId, t)
            if (presenterMap != null && presenterMap!!.size > 0) {
                for ((key, value) in presenterMap!!) {
                    binding!!.setVariable(key, value)
                }
            }
            binding!!.executePendingBindings()
        }
    }

    interface Adaptable<T> {
        fun adapt(position: Int, list: MutableList<T>, binding: ViewDataBinding?, vararg args: Any?)
    }

    interface IType {
        val type: Int
    }

    interface Bindable {
        var binding: ViewDataBinding?
    }

    private val mOnItemThroughListeners: MutableList<OnItemThroughListener> = ArrayList()

    fun addOnItemThroughListener(l: OnItemThroughListener): RVBindingBaseAdapter<T> {
        if (!mOnItemThroughListeners.contains(l)) {
            mOnItemThroughListeners.add(l)
        }
        return this
    }

    interface OnItemThroughListener {
        fun onIn(position: Int)
        fun onOut(position: Int)
    }

    interface OnItemClickListener<T> {
        fun onItemClick(v: View?, t: T)
    }

    companion object {
        private const val DEFAULT_TYPE = 0
    }
}