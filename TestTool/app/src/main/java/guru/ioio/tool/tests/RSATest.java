package guru.ioio.tool.tests;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.security.PublicKey;

import guru.ioio.tool.utils.RSAUtils;
import guru.ioio.tool.utils.RSAUtils2;

public class RSATest extends AbsBaseTest {
    @Override
    protected String doClick(View v) throws Throwable {
        return test1();
    }

    private String test1() {
        String result = RSAUtils.encryptByRsa("abc123", "ad_keys/baidu_rsa_public_key.pem");
        Log.i("RSATest", result);
        return result;
    }

    private String test2() {
        try {
            PublicKey pubkey = RSAUtils2.loadPublicKey(
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvz5XO+wDhxUaIDOtrp72" +
                            "fUeIfTYXUSkZXNbA0REQzFGXPFqeMvKEOacgixdfeb/1jWif6dE2pzX1kwMAaOCe" +
                            "nIjP9MSw8ZRgR3bZmRq8IuiBPDLI68tFDE6jpA8WjTlcaSkBy06iPtPckAT3LQiP" +
                            "FQroz4Dsoxnrw1QFO82QyWoFfUhGZjj895BQSjfjJjZajOoEY6GBtcRmI30XlVUw" +
                            "MJT9JAqf8GjyvoOMDR3Tjp226UepBIF/NhJKMrW3M5a0SHWo6r+KiAuG6pSVCHPX" +
                            "dP6MaQ/6W2W62wxRqrf24hi407qyKOu4MiEAPbEP3UjdIV3AW1nADjUzg2nxSjRF" +
                            "KQIDAQAB"
            );
            byte[] result = RSAUtils2.encryptDataPublic("861980030653449".getBytes(), pubkey);
            return Base64.encodeToString(result, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
