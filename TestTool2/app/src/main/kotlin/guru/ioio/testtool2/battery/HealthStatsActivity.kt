package guru.ioio.testtool2.battery

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.health.SystemHealthManager
import android.os.health.UidHealthStats
import androidx.annotation.RequiresApi
import guru.ioio.testtool2.R
import guru.ioio.testtool2.utils.Logger
import kotlinx.android.synthetic.main.activity_health_stats.*

class HealthStatsActivity : Activity() {
    private val mLogger = Logger(HealthStatsActivity::class.java.simpleName)


    private var mSystemHealthManager: SystemHealthManager? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_stats)
        show_btn.setOnClickListener {
            loadHealthStats()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadHealthStats() {

        if (mSystemHealthManager == null) {
            mSystemHealthManager = getSystemService(SYSTEM_HEALTH_SERVICE) as SystemHealthManager
        }
        val snapshot = mSystemHealthManager!!.takeMyUidSnapshot()
        mLogger.ci(snapshot)

    }
}