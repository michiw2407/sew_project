package tests.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class RequestTestHelper {

    public static InputStream getValidRequestStream(String url) throws Exception {
        return getValidRequestStream(url, "GET", "localhost", null, null);
    }

    public static InputStream getValidRequestStream(String url, String method, String body) throws Exception {
        return getValidRequestStream(url, method, "localhost", null, body);
    }


    private static InputStream getValidRequestStream(String url, String method, String host, String[][] header, String body) throws Exception {

        byte[] bodyBytes = null;

        if (body != null) bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os,
                StandardCharsets.UTF_8));
        pw.printf("%s %s HTTP/1.1\n", method, url);
        pw.printf("Host: %s\n", host);
        pw.printf("Connection: keep-alive\n");
        pw.printf("Accept: text/html,application/xhtml+xml\n");
        pw.printf("User-Agent: Unit-Test-Agent/1.0\n");
        pw.printf("Accept-Encoding: gzip,deflate\n");
        pw.printf("Accept-Language: de-AT\n");

        if (bodyBytes != null) {
            pw.printf("Content-Length: %d\n", bodyBytes.length);
            pw.printf("Content-Type: application/x-www-form-urlencoded\n");
        }

        if (header != null) {
            for (String[] h : header) {
                pw.printf("%s: %s\n", h[0], h[1]);
            }
        }

        pw.println();

        if (bodyBytes != null) {
            pw.flush();
            os.write(bodyBytes);
        }

        pw.flush();
        return new ByteArrayInputStream(os.toByteArray());
    }
}
