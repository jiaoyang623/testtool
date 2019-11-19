package guru.ioio.tool.function;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.tool.R;
import guru.ioio.tool.databinding.ContainerPagingBinding;

public class PagingContainer implements IContainer {
    private static final String TAG = "PagingContainer";
    private ContainerPagingBinding mBinding;
    private PagingModel mModel;

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.container_paging, parent, false);
            mModel = ViewModelProviders.of((FragmentActivity) context).get(PagingModel.class);
            PagingAdapter adapter = new PagingAdapter();
            mModel.list.observe((LifecycleOwner) context, adapter::submitList);
            mBinding.recycler.setAdapter(adapter);
            mBinding.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            mBinding.refresh.setOnRefreshListener(() -> {
                try {
                    mModel.list.getValue().getDataSource().invalidate();
                } catch (NullPointerException e) {
                }
                mBinding.refresh.setRefreshing(false);
            });
        }
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {

    }

    public static class PagingModel extends ViewModel {
        public LiveData<PagedList<String>> list;

        public PagingModel() {
            list = new LivePagedListBuilder<>(new DataSource.Factory<Integer, String>() {
                @Override
                public DataSource<Integer, String> create() {
                    return new PagingDataSource();
                }
            }, 20).build();
        }
    }

    public static class PagingDataSource extends PageKeyedDataSource<Integer, String> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, String> callback) {
            Log.i(TAG, "loadInitial " + params.requestedLoadSize + ", " + Thread.currentThread().getId());
            callback.onResult(get(0, params.requestedLoadSize), 0, params.requestedLoadSize);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, String> callback) {
            Log.i(TAG, "loadBefore " + params.key + ", " + params.requestedLoadSize + ", " + Thread.currentThread().getId());
//            callback.onResult(get(params.key - params.requestedLoadSize, params.requestedLoadSize), params.key - params.requestedLoadSize);
            callback.onResult(new ArrayList<>(0), null);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, String> callback) {
            Log.i(TAG, "loadAfter " + params.key + ", " + params.requestedLoadSize + ", " + Thread.currentThread().getId());
            callback.onResult(get(params.key, params.requestedLoadSize), params.key + params.requestedLoadSize);
        }

        private List<String> get(int from, int size) {
            Log.i(TAG, "get " + from + ", " + size);
            List<String> list = new ArrayList<>(size);
            for (int i = from; i < from + size; i++) {
                list.add("item " + i);
            }
            return list;
        }
    }

    public static class PagingAdapter extends PagedListAdapter<String, PagingViewHolder> {

        protected PagingAdapter() {
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String s, @NonNull String t1) {
                    return s.equals(t1);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String s, @NonNull String t1) {
                    return s.equals(t1);
                }
            });
        }

        @NonNull
        @Override
        public PagingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PagingViewHolder(new TextView(viewGroup.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull PagingViewHolder pagingViewHolder, int i) {
            pagingViewHolder.view.setText(getItem(i));
        }
    }

    public static class PagingViewHolder extends RecyclerView.ViewHolder {
        public TextView view;

        public PagingViewHolder(@NonNull TextView itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
