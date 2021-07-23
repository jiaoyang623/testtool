package guru.ioio.testtool2.battery;

import android.os.Build;
import android.os.health.UidHealthStats;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HealthStatsHelper {
    private Map<Integer, String> mNameMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HealthStatsHelper() {
        loadNameMap();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadNameMap() {
        for (Field field : UidHealthStats.class.getDeclaredFields()) {
        }
    }
}
