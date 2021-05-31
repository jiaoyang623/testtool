package guru.ioio.tool.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import guru.ioio.tool.BaseApp;

public class RSAUtils {
    private static String RSA = "RSA/ECB/PKCS1Padding";
    private static String RSA_KEY_FACTORY = "RSA";

    public static Map<String, String> sKeyMap = new HashMap<>();

    public static String encryptByRsa(String source, String assetKeyPath) {
        String key;
        if (sKeyMap.containsKey(assetKeyPath)) {
            key = sKeyMap.get(assetKeyPath);
        } else {
            key = getPublicKeyFromAssets(BaseApp.getInstance(), assetKeyPath);
            sKeyMap.put(assetKeyPath, key);
        }
        return qucRsaEncryptStr(source, key);
    }

    /**
     * 获取公钥
     *
     * @return
     */
    private static String getPublicKeyFromAssets(Context context, String path) {
        BufferedReader bufReader = null;
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(path));
            bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.charAt(0) == '-') {
                    continue;
                }
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String qucRsaEncryptStr(String source, String key) {
        String encryptedStr = "";

        try {
            PublicKey publicKey = getPublicKeyFromX509(key);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(1, publicKey);
            byte[] encryptedData = cipher.doFinal(source.getBytes());
            encryptedStr = android.util.Base64.encodeToString(encryptedData, 0);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return encryptedStr;
    }

    private static PublicKey getPublicKeyFromX509(String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodeKey = android.util.Base64.decode(bysKey, 0);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_FACTORY);
        return keyFactory.generatePublic(x509);
    }
}
