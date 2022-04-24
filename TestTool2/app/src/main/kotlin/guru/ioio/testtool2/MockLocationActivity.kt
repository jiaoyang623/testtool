package guru.ioio.testtool2

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_mock_location.*


class MockLocationActivity : Activity(), LocationListener {
    private val mMockProviderName = LocationManager.GPS_PROVIDER
    private val locationManager: LocationManager by lazy {
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager).apply {
            addTestProvider(
                mMockProviderName,
                false,
                true,
                false,
                false,
                true,
                true,
                true,
                0,
                5
            )
            setTestProviderEnabled(mMockProviderName, true)
//            locationManager.requestLocationUpdates(mMockProviderName, 0L, 0F, this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_location)
        set_btn.setOnClickListener {
            setLocation(latitude.text.toString().toDouble(), longitude.text.toString().toDouble())
        }
        val thread = Thread {
            while (!isFinishing) {
                Thread.sleep(500)
                setLocation(
                    latitude.text.toString().toDouble(),
                    longitude.text.toString().toDouble()
                )
            }
        }
        thread.start()

    }

    private fun setLocation(longitude: Double, latitude: Double) {
        Log.i("MLA", "$latitude, $longitude")
        val location = Location(mMockProviderName).apply {
            time = System.currentTimeMillis()
            setLatitude(latitude)
            setLongitude(longitude)
            altitude = 2.0
            accuracy = 3.0f
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        locationManager.setTestProviderLocation(mMockProviderName, location)
    }

    override fun onLocationChanged(location: Location) {

    }
}