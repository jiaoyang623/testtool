package guru.ioio.tool.utils;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpResponse;

public class GzipUtils {
    public GzipUtils() {
    }

    public static String getJsonStringFromGZIP(HttpResponse response) {
        String jsonString = null;

        try {
            InputStream is = response.getEntity().getContent();
            jsonString = getJsonStringFromGZIPInputStream(is);
            is.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return jsonString;
    }

    public static String getJsonStringFromGZIPInputStream(InputStream iStream) {
        String jsonString = null;

        try {
            BufferedInputStream bis = new BufferedInputStream(iStream);
            InputStream is = null;
            bis.mark(2);
            byte[] header = new byte[2];
            int result = bis.read(header);
            bis.reset();
            int headerData = getShort(header);
            if (result != -1 && headerData == 8075) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }

            InputStreamReader reader = new InputStreamReader((InputStream)is, "utf-8");
            char[] data = new char[100];
            StringBuffer sb = new StringBuffer();

            int readSize;
            while((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }

            jsonString = sb.toString();
            bis.close();
            reader.close();
            ((InputStream)is).close();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        Log.d("HttpTask", "getJsonStringFromGZIP net output : " + jsonString);
        return jsonString;
    }

    private static int getShort(byte[] data) {
        return data[0] << 8 | data[1] & 255;
    }
}