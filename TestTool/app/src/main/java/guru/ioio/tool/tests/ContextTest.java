package guru.ioio.tool.tests;

import android.content.Context;
import android.view.View;

public class ContextTest extends AbsBaseTest {
    @Override
    protected String doClick(View v) throws Throwable {
        Context context = v.getContext();
        return context.getClass().getSimpleName();
    }
}
