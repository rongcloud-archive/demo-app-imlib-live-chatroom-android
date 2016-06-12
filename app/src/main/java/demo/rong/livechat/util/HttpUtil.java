package demo.rong.livechat.util;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    public static final String REQUEST_GET_TOKEN = "https://api.cn.ronghub.com/user/getToken.json";
    public static final String APP_KEY = "n19jmcy59ocx9";
    public static final String APP_SECRET = "PblLNSx3hSkW";

    private static final String APPKEY = "RC-App-Key";
    private static final String NONCE = "RC-Nonce";
    private static final String TIMESTAMP = "RC-Timestamp";
    private static final String SIGNATURE = "RC-Signature";

    private static HttpURLConnection conn;
    private static String bodyContent;
    private static OnResponse cb;

    public interface OnResponse {
        void onResponse(int code, String body);
    }

    public static void post(String url, String body, OnResponse callback) throws IOException {
        bodyContent = body;
        cb = callback;

        // Create HttpURLConnection.
        URL reqUrl = new URL(url);
        conn = (HttpURLConnection) reqUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        // Write header.
        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = toHex(toSHA1(APP_SECRET + nonce + timestamp));
        conn.setRequestProperty(APPKEY, APP_KEY);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // start post
        new HttpThread().start();
    }

    private static class HttpThread extends Thread {
        @Override
        public void run() {
            try {
                // Write content.
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(bodyContent);
                out.flush();
                out.close();

                int code = conn.getResponseCode();
                InputStream in;
                if (code == 200) {
                    in = conn.getInputStream();
                } else {
                    in = conn.getErrorStream();
                }
                // Send http request, convert response to String
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                byte[] data = outStream.toByteArray();
                outStream.close();
                in.close();
                String json = new String(data);

                cb.onResponse(code, json);
            } catch (IOException e) {
                e.printStackTrace();
                cb.onResponse(-1, "Unknown error!");
            } finally {
                conn.disconnect();
            }
        }
    }

    private static byte[] toSHA1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes());
            return md.digest();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String toHex(byte[] data) {
        final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int j = 0; i < l; ++i) {
            out[j++] = DIGITS_LOWER[(0xf0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0f & data[i]];
        }
        return String.valueOf(out);
    }
}
