package guru.ioio.tool.tests;

import android.view.View;

import guru.ioio.tool.ITest;

public class DemoTest implements ITest {
    @Override
    public String onClick(View v) {
        return "hello test";
    }
}
