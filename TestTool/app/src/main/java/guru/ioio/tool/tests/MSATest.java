package guru.ioio.tool.tests;

import android.view.View;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdk;

import guru.ioio.tool.ITest;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MSATest implements ITest {
    @Override
    public Observable<String> onClick(View v) {
        return getIds(v).subscribeOn(Schedulers.io());
    }

    private Observable<String> getIds(View v) {
        return Observable.create(e -> {
            MdidSdk sdk = new MdidSdk();
            int result = sdk.InitSdk(v.getContext(), (isSupport, idSupplier) -> {
                if (isSupport && idSupplier != null) {
                    StringBuilder builder = new StringBuilder();

                    builder.append("OAID=").append(idSupplier.getOAID()).append('\n')
                            .append("VAID=").append(idSupplier.getVAID()).append('\n')
                            .append("AAID=").append(idSupplier.getAAID()).append('\n');
                    e.onNext(builder.toString());
                } else {
                    e.onNext("not supported");
                }
            });
            String error = translate(result);
            if (error != null) {
                e.onNext(error);
            }
        });
    }

    private static String translate(int src) {
        switch (src) {
            case ErrorCode.INIT_ERROR_BEGIN:
                return "error_ begin";
            case ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT:
                return "device not support";
            case ErrorCode.INIT_ERROR_LOAD_CONFIGFILE:
                return "config error";
            case ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT:
                return "manufacturer not support";
            case ErrorCode.INIT_ERROR_RESULT_DELAY:
                return "delay";
            case ErrorCode.INIT_HELPER_CALL_ERROR:
                return "reflect error";
            default:
                return null;
        }
    }
}
