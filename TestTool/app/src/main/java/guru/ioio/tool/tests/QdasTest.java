package guru.ioio.tool.tests;

import android.text.TextUtils;
import android.view.View;

import com.qihoo.sdk.qhdeviceid.QHDevice;

public class QdasTest extends BaseTest {
    @Override
    protected String doClick(View v) {
        String imei = QHDevice.getDeviceId(v.getContext(), QHDevice.DataType.MAC);
        return TextUtils.isEmpty(imei) ? "none" : imei;
    }
}
