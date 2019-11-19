package guru.ioio.tool.utils;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
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

public class PageBindingBaseAdapter<T> extends PagedListAdapter<T, PageBindingBaseAdapter.RViewHolder> {
    private final int sValueId;
    protected List<T> mList = new ArrayList<>();
    private Map<Integer, Object> mPresenterMap = new HashMap<>();
    private SparseIntArray mTypeMap;
    private static final int DEFAULT_TYPE = 0;

    private DiffUtil.ItemCallback<T> getItemCallback() {
        return new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }
        };

    }

    public PageBindingBaseAdapter(int layoutId, int valueId) {
        this(layoutId, valueId, null);
    }

    public PageBindingBaseAdapter(int layoutId, int valueId, DiffUtil.ItemCallback<T> callback) {
        super(callback != null ? callback : new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }
        });
        mTypeMap = new SparseIntArray();
        mTypeMap.put(super.getItemViewType(0), layoutId);
        sValueId = valueId;
    }

    public PageBindingBaseAdapter(SparseIntArray typeMap, int valueId) {
        this(typeMap, valueId, null);
    }

    public PageBindingBaseAdapter(SparseIntArray typeMap, int valueId, DiffUtil.ItemCallback<T> callback) {
        super(callback != null ? callback : new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
                return t.equals(t1);
            }
        });
        mTypeMap = typeMap;
        sValueId = valueId;
    }

    public PageBindingBaseAdapter<T> addPresenter(int variableId, Object presenter) {
        mPresenterMap.put(variableId, presenter);
        return this;
    }

    public PageBindingBaseAdapter<T> removePresenter(int variableId) {
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

    public PageBindingBaseAdapter<T> setAdaptArguments(Object... args) {
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

    public PageBindingBaseAdapter<T> addOnItemThroughListener(OnItemThroughListener l) {
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
