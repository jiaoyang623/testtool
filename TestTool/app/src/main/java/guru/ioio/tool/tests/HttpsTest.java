package guru.ioio.tool.tests;

import android.view.View;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsTest extends AbsBaseTest {
    @Override
    protected String doClick(View v) {
        String result = "";
        InputStream is = null;
        try {
            URL url = new URL("https://www.baidu.com");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, count));
            }
            result = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result = sw.toString();
            close(pw, sw);
        } finally {
            close(is);
        }
        return result;
    }

    private void close(Closeable... closeable) {
        for (Closeable c : closeable) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
