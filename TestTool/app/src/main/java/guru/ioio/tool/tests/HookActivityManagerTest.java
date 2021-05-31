package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.utils.HookUtils;

public class HookActivityManagerTest extends AbsBaseTest {
    @Override
    protected String doClick(View v) throws Throwable {
        HookUtils.hookActivityManager();
        return "what?";
    }
}
