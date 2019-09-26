package guru.ioio.tool.tests;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;

import guru.ioio.tool.ITest;
import guru.ioio.tool.utils.Md5Util;
import io.reactivex.Observable;

public class InfoTest implements ITest {
    private static String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<String> onClick(View v) {
        return Observable.create(e -> ((Activity) v.getContext()).runOnUiThread(() -> {
                    RxPermissions rxPermissions = new RxPermissions((Activity) v.getContext());
                    rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                            .subscribe(b -> {
                                StringBuilder builder = new StringBuilder();
                                String aid = android.provider.Settings.System.getString(v.getContext().getContentResolver(), "android_id");
                                String sn = getDeviceSerial();
                                builder.append("AndroidID:\t").append(aid).append('\n')
                                        .append("MD5:\t").append(Md5Util.MD5Encode(aid)).append('\n')
                                        .append("SerialNo:\t").append(sn).append('\n')
                                        .append("MD5:\t").append(Md5Util.MD5Encode(sn)).append('\n');
                                if (b) {
                                    @SuppressLint("MissingPermission") String imei = ((TelephonyManager) v.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                    builder.append("IMEI:\t").append(imei).append('\n').append("MD5:\t").append(Md5Util.MD5Encode(imei)).append('\t');
                                }
                                e.onNext(builder.toString());
                            });
                })
        );
    }


}
