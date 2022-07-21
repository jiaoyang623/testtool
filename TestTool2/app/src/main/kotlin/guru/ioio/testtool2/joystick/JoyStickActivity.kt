package guru.ioio.testtool2.joystick

import android.app.Activity
import android.os.Bundle
import android.util.Log
import guru.ioio.testtool2.R

class JoyStickActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joystick)
        findViewById<JoyStickView>(R.id.joystick).onKeyEvent = { code, isDown ->
            Log.i("JSA", "$code, $isDown")
        }
    }
}