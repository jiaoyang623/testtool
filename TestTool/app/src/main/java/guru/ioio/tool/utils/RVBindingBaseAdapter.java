package guru.ioio.tool.utils;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaoyang on 16/06/2017.
 * base adapter for RecyclerView.Adapter
 */

public class RVBindingBaseAdapter<T> extends RecyclerView.Adapter<RVBindingBaseAdapter.RViewHolder> {
    private final int sValueId;
    protected List<T> mList = new ArrayList<>();
    private Map<Integer, Object> mPresenterMap = new HashMap<>();
    private SparseIntArray mTypeMap;
    private static final int DEFAULT_TYPE = 0;

    public RVBindingBaseAdapter(int layoutId, int valueId) {
        mTypeMap = new SparseIntArray();
        mTypeMap.put(super.getItemViewType(0), layoutId);
        sValueId = valueId;
    }

    public RVBindingBaseAdapter(SparseIntArray typeMap, int valueId) {
        mTypeMap = typeMap;
        sValueId = valueId;
    }

    public RVBindingBaseAdapter<T> set(List<T> data) {
        mList.clear();
        if (data != null) {
            mList.addAll(checkList(data));
            notifyDataSetChanged();
        }

        return this;
    }

    private boolean checkType(T t) {
        if (t instanceof IType) {
            return mTypeMap.get(((IType) t).getType()) != 0;
        } else {
            return true;
        }
    }

    private List<T> checkList(List<T> list) {
        if (list != null && list.size() > 0) {
            List<T> removeList = new ArrayList<>();
            for (T t : list) {
                if (!checkType(t)) {
                    removeList.add(t);
                }
            }
            list.removeAll(removeList);
        }
        return list;
    }

    public RVBindingBaseAdapter<T> addToTail(T t) {
        return add(t, mList.size());
    }

    public RVBindingBaseAdapter<T> addToHead(T t) {
        return add(t, 0);
    }

    public RVBindingBaseAdapter<T> add(T t) {
        add(t, mList.size());
        return this;
    }

    public RVBindingBaseAdapter<T> add(T t, int position) {
        if (t != null && mList != null && !mList.contains(t) && mList.size() >= position && checkType(t)) {
            mList.add(position, t);
//            notifyItemInserted(position);
            notifyDataSetChanged();
        }
        return this;
    }

    public RVBindingBaseAdapter<T> add(List<T> data) {
        return add(data, mList.size());
    }

    public RVBindingBaseAdapter<T> add(List<T> data, int position) {
        if (data == null) {
            return this;
        }

        data.removeAll(mList);
        checkList(data);
        if (data.size() > 0) {
            mList.addAll(position, data);
            notifyItemRangeInserted(position, data.size());
        }
        return this;
    }

    public List<T> getList() {
        return mList;
    }

    public RVBindingBaseAdapter<T> remove(int position) {
        if (position >= 0 && position < getItemCount()) {
            mList.remove(position);
            notifyDataSetChanged();
        }
        return this;
    }

    public RVBindingBaseAdapter<T> remove(T t) {
        if (t != null) {
            remove(mList.indexOf(t));
        }
        return this;
    }

    public int indexOf(T t) {
        if (mList != null && t != null) {
            return mList.indexOf(t);
        } else {
            return -1;
        }
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public RVBindingBaseAdapter<T> addToHead(List<T> data) {
        add(data, 0);
        return this;
    }

    public RVBindingBaseAdapter<T> addToTail(List<T> data) {
        add(data, mList.size());
        return this;
    }


    public RVBindingBaseAdapter<T> addPresenter(int variableId, Object presenter) {
        mPresenterMap.put(variableId, presenter);
        return this;
    }

    public RVBindingBaseAdapter<T> removePresenter(int variableId) {
        if (mPresenterMap.containsKey(variableId)) {
            mPresenterMap.remove(variableId);
        }

        return this;
    }

    @Override
    public void onViewDetachedFromWindow(RViewHolder holder) {
        final int pos = holder.getAdapterPosition();
        for (OnItemThroughListener l : mOnItemThroughListeners) {
            l.onOut(pos);
        }
    }

    @Override
    public void onViewAttachedToWindow(RViewHolder holder) {
        final int pos = holder.getAdapterPosition();
        for (OnItemThroughListener l : mOnItemThroughListeners) {
            l.onIn(pos);
        }
    }


    private Object[] mAdaptArgs = new Object[0];

    public RVBindingBaseAdapter<T> setAdaptArguments(Object... args) {
        mAdaptArgs = args;
        return this;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), mTypeMap.get(viewType), parent, false);
        binding.getRoot().setTag(binding);
        return new RViewHolder(binding, sValueId, mPresenterMap);
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        if (mList != null && mList.size() > position) {
            T t = mList.get(position);
            if (t instanceof Adaptable) {
                ((Adaptable) t).adapt(position, mList, holder.binding, mAdaptArgs);
            }
            holder.bind(t);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null && mList.size() > position) {
            T t = mList.get(position);
            if (t instanceof IType) {
                return ((IType) t).getType();
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public T getItem(int position) {
        if (position >= 0 && mList != null && mList.size() > 0 && position < mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class RViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private int valueId;
        private Map<Integer, Object> presenterMap;

        public RViewHolder(ViewDataBinding binding, int valueId, Map<Integer, Object> presenterMap) {
            super(binding.getRoot());
            this.binding = binding;
            this.valueId = valueId;
            this.presenterMap = presenterMap;
        }

        public RViewHolder(View v) {
            super(v);
        }

        public void bind(Object t) {
            if (t instanceof Bindable) {
                ((Bindable) t).setBinding(binding);
            }
            binding.setVariable(valueId, t);
            if (presenterMap != null && presenterMap.size() > 0) {
                for (Map.Entry<Integer, Object> entry : presenterMap.entrySet()) {
                    binding.setVariable(entry.getKey(), entry.getValue());
                }
            }
            binding.executePendingBindings();
        }
    }

    public interface Adaptable<T> {
        void adapt(int position, List<T> list, ViewDataBinding binding, Object... args);
    }

    public interface IType {
        int getType();
    }

    public interface Bindable {
        void setBinding(ViewDataBinding binding);

        ViewDataBinding getBinding();
    }

    private List<OnItemThroughListener> mOnItemThroughListeners = new ArrayList<>();

    public RVBindingBaseAdapter<T> addOnItemThroughListener(OnItemThroughListener l) {
        if (!mOnItemThroughListeners.contains(l)) {
            mOnItemThroughListeners.add(l);
        }
        return this;
    }

    public interface OnItemThroughListener {
        void onIn(int position);

        void onOut(int position);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View v, T t);
    }
}
