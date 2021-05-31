package guru.ioio.testtool2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import guru.ioio.testtool2.databinding.ActivityPanelBinding

class PanelActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPanelBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_panel)
        mBinding.presenter = this
    }

    fun onSelectAllClick(v: View): Boolean {
        mBinding.input.requestFocus()
        mBinding.input.selectAll()
        return true;
    }
}