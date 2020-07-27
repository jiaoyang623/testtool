package guru.ioio.tool.tests;

import android.content.Intent;
import android.view.View;

public class WifiPullActivityTest extends BaseTest {
    @Override
    protected String doClick(View v) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction("com.qihoo.video.wft.caller.Trans");
        v.getContext().startActivity(intent);
        return "done";
    }

}
