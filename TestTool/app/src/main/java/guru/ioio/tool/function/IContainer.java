package guru.ioio.tool.function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface IContainer {
    View onCreateView(Context context, LayoutInflater inflater, ViewGroup parent);

    void onDestroyView();

}
