package guru.ioio.testtool2.utils

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


class ActivityUtils {
    companion object {
        fun getTopActivityName(context: Context): String? {
            val topActivityPackageName: String
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //此处要判断用户的安全权限有没有打开，如果打开了就进行获取栈顶Activity的名字的方法
                //当然，我们的要求是如果没打开就不获取了，要不然跳转会影响用户的体验
                if (isSecurityPermissionOpen(context)) {
                    val mUsageStatsManager =
                        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                    val endTime = System.currentTimeMillis()
                    val beginTime = endTime - 1000 * 60 * 2
                    var recentStats: UsageStats? = null
                    val queryUsageStats = mUsageStatsManager.queryUsageStats(
                        UsageStatsManager.INTERVAL_BEST,
                        beginTime,
                        endTime
                    )
                    if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                        return null
                    }
                    for (usageStats in queryUsageStats) {
                        if (recentStats == null || recentStats.lastTimeUsed < usageStats.lastTimeUsed) {
                            recentStats = usageStats
                        }
                    }
                    topActivityPackageName = recentStats!!.packageName
                    topActivityPackageName
                } else {
                    null
                }
            } else {
                val taskInfos = manager.getRunningTasks(1)
                topActivityPackageName =
                    if (taskInfos.size > 0) taskInfos[0].topActivity!!.packageName else return null
                topActivityPackageName
            }
        }

        //判断用户对应的安全权限有没有打开
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun isSecurityPermissionOpen(context: Context): Boolean {
            val endTime = System.currentTimeMillis()
            val usageStatsManager = context.getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, endTime)
            return !(queryUsageStats == null || queryUsageStats.isEmpty())
        }
    }
}