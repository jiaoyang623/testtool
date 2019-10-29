package guru.ioio.tool.function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseContainer implements IContainer {
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText("BaseContainer");
        tv.setBackgroundColor(0xffaaff11);
        return tv;
    }

    @Override
    public void onDestroyView() {

    }
}
