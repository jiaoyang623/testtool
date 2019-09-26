package guru.ioio.tool.tests;

import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;

import guru.ioio.tool.utils.GzipUtils;

public class DnsTest extends BaseTest {
    @Override
    public String doClick(View v) {
        String result = getDnsCacheRequest();
        return result == null ? "none" : result;
    }

    private String getDnsCacheRequest() {
        long startTime = System.currentTimeMillis();
        String result = null;
        DefaultHttpClientConnection connection = new DefaultHttpClientConnection();
        HttpGet dnsCacheInHttpGet = new HttpGet("http://cloud.v.tvfanqie.com/all");
        try {
            Socket socket = new Socket(dnsCacheInHttpGet.getURI().getHost(), 80);
            connection.bind(socket, new BasicHttpParams());
            URI requestURI = dnsCacheInHttpGet.getURI();
            String host = requestURI.getHost();
            String scheme = requestURI.getScheme();
            String pre = scheme + "://" + host;
            String path = requestURI.toString().substring(pre.length());
            BasicHttpRequest httpRequest = new BasicHttpRequest(dnsCacheInHttpGet.getMethod(), path, HttpVersion.HTTP_1_1);
            httpRequest.addHeader("Host", host);
            httpRequest.addHeader("Connection", "Keep-Alive");
            httpRequest.addHeader("Accept-Encoding", "gzip");
            httpRequest.addHeader("User-Agent", "360 Video App/3.4.5 Android/4.3 QIHU");
            HttpProcessor httpProcessor = new BasicHttpProcessor();
            HttpRequestExecutor httpExecutor = new HttpRequestExecutor();
            HttpContext httpContext = new BasicHttpContext();
            httpExecutor.preProcess(httpRequest, httpProcessor, httpContext);
            HttpResponse response = httpExecutor.execute(httpRequest, connection, httpContext);
            httpExecutor.postProcess(response, httpProcessor, httpContext);
            result = GzipUtils.getJsonStringFromGZIP(response);
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException e) {
            }

        }
        return "Cost: " + (System.currentTimeMillis() - startTime) + '\n' + result;
    }

}
