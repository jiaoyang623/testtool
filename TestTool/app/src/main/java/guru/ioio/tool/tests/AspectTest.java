package guru.ioio.tool.tests;

import android.view.View;

public class AspectTest extends BaseTest {
    @Override
    protected String doClick(View v) {
        return doClick("doClick View");
    }

    protected String doClick(String s) {
        return "doClick String";
    }
}
