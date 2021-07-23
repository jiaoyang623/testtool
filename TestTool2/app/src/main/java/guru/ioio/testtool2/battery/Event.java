/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package guru.ioio.testtool2.battery;

import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.battery.reporter.core.SystemMetricsReporter;

/** Poor man's analytics: also known as Logcat. */
public class Event implements SystemMetricsReporter.Event {
  @Override
  public boolean isSampled() {
    return true;
  }

  @Override
  public void acquireEvent(@Nullable String moduleName, String eventName) {
    Log.i("BatteryApplication", "New event: {");
  }

  @Override
  public void add(String key, String value) {
    Log.i("BatteryApplication", key + ":" + value);
  }

  @Override
  public void add(String key, int value) {
    Log.i("BatteryApplication", key + ":" + value);
  }

  @Override
  public void add(String key, long value) {
    Log.i("BatteryApplication", key + ":" + value);
  }

  @Override
  public void add(String key, double value) {
    Log.i("BatteryApplication", key + ":" + value);
  }

  @Override
  public void logAndRelease() {
    Log.i("BatteryApplication", "}");
  }
}