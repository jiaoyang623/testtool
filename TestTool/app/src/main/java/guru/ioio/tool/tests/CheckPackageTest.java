package guru.ioio.tool.tests;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.View;

public class CheckPackageTest extends AbsBaseTest {
    @Override
    protected String doClick(View v) throws Throwable {
        Context context = v.getContext();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.android.systemui", 0);
        return packageInfo == null ? "false" : "true";
    }
}
