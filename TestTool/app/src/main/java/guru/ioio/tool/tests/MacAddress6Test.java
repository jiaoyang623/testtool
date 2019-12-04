package guru.ioio.tool.tests;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;

public class MacAddress6Test extends BaseTest {
    @Override
    protected String doClick(View v) {
        return getLocalMacAddressFromWifiInfo(v.getContext());
    }

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;

    }
}
