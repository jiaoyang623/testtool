package guru.ioio.testtool2

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import guru.ioio.testtool2.databinding.ActivityMainBinding
import guru.ioio.testtool2.utils.RVBindingBaseAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding;
    private lateinit var mAdapter: RVBindingBaseAdapter<ActivityInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.presenter = this
        mBinding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = RVBindingBaseAdapter(R.layout.item_activity, BR.data)
        mBinding.recycler.adapter = mAdapter
        mAdapter.add(getActivities(), 0)
    }

    private fun getActivities(): MutableList<ActivityInfo> {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            val list: MutableList<ActivityInfo> = ArrayList(packageInfo.activities.size)
            val repluginPrefix = "$packageName.loader.a."
            for (info in packageInfo.activities) {
                if (MainActivity::class.java.name == info.name) {
                    continue
                }
                if (info.name.startsWith(repluginPrefix)) {
                    continue
                }
                list.add(info)
            }
            return list
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    fun onItemClick(info: ActivityInfo): Boolean {
        val intent = Intent()
        intent.setClassName(applicationContext, info.name)
        startActivity(intent)

        return true
    }

    fun getActivityName(info: ActivityInfo): String {
        return info.name.replace("$packageName.", "")
    }
}