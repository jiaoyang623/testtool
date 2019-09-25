package guru.ioio.tool;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.tool.databinding.ActivityListBinding;
import guru.ioio.tool.utils.RVBindingBaseAdapter;

/**
 * Created by daniel on 9/29/17.
 * to list activities in this app
 */

public class ActivityListActivity extends Activity {
    private ActivityListBinding mBinding;
    private RVBindingBaseAdapter<ActivityInfo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        mBinding.setPresenter(this);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RVBindingBaseAdapter<>(R.layout.item_activity, BR.data);
        mAdapter.addPresenter(BR.presenter, this);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.add(getActivities());
    }

    private List<ActivityInfo> getActivities() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            List<ActivityInfo> list = new ArrayList<>(packageInfo.activities.length);
            for (ActivityInfo info : packageInfo.activities) {
                if (ActivityListActivity.class.getName().equals(info.name)) {
                    continue;
                }
                list.add(info);
            }
            return list;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean onItemClick(ActivityInfo info) {
        Intent intent = new Intent();
        intent.setClassName(getApplicationContext(), info.name);
        startActivity(intent);

        return true;
    }

    public String getActivityName(ActivityInfo info) {
        return info.name.replace(getPackageName() + ".", "");
    }
}