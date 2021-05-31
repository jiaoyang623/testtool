package guru.ioio.tool.tests;

import android.view.View;

public class ExitTest extends AbsBaseTest {

    @Override
    protected String doClick(View v) throws Throwable {
        System.exit(-1);
        return null;
    }
}
