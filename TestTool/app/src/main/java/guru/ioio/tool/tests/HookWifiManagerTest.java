package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.hook.WifiHookManager;

public class HookWifiManagerTest extends BaseTest {
    @Override
    protected String doClick(View v) {
        boolean result = WifiHookManager.getInstance().hook(v.getContext());

        WifiHookManager.getInstance().addBanList("getConnectionInfo");

        return result ? "success" : "failed";
    }
}
