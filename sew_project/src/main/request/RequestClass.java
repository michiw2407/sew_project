package main.request;

import interfaces.Request;
import main.url.UrlClass;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class RequestClass implements Request {


    private InputStream inpStream;
    private UrlClass url = new UrlClass("/");
    private Map<String, String> headers = new HashMap<>();

    private String method;
    private String httpVersion;

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public RequestClass(InputStream inputStream) throws IOException {
        inpStream = inputStream;
        resolveInputStream();
    }

    private void resolveInputStream() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inpStream, StandardCharsets.UTF_8));

        //System.out.println(inpStream);
        String methodLine = reader.readLine();

        if(methodLine==null) {
            throw new IllegalStateException();
        }

        // GET /url HTTP/1.1
        String[] segment = methodLine.split(" ", 3);

        method = segment[0].toUpperCase();
        url = new UrlClass(segment[1]);
        httpVersion = segment[2];

        String line;

        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerSplit = line.split(": ", 2);
            headers.put(headerSplit[0].toLowerCase(), headerSplit[1]);
        }
    }

    /**
     * @return Returns true if the request is valid. A request is valid, if
     * method and url could be parsed. A header is not necessary.
     */
    @Override
    public boolean isValid() {
        if (method.length() >= 3) {
            for (HttpMethod h : HttpMethod.values()) {
                if (h.name().equals(method)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return Returns the request method in UPPERCASE. get -> GET
     */
    @Override
    public String getMethod() {
        return this.isValid() ? method : null;
    }

    /**
     * @return Returns a URL object of the request. Never returns null.
     */
    @Override
    public UrlClass getUrl() {
        return url;
    }

    /**
     * @return Returns the request header. Never returns null. All keys must be
     * lower case.
     */
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return Returns the number of header or 0, if no header where found.
     */
    @Override
    public int getHeaderCount() {
        return headers.size();
    }

    /**
     * @return Returns the user agent from the request header
     */
    @Override
    public String getUserAgent() {
        return headers.getOrDefault("user-agent", null);
    }

    /**
     * @return Returns the parsed content length request header. Never returns
     * null.
     */
    @Override
    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("content-length", "0"));
    }

    /**
     * @return Returns the parsed content type request header. Never returns
     * null.
     */
    @Override
    public String getContentType() {
        return headers.getOrDefault("content-type", "");
    }

    /**
     * @return Returns the request content (body) stream or null if there is no
     * content stream.
     */
    @Override
    public InputStream getContentStream() {
        return inpStream;
    }

    /**
     * @return Returns the request content (body) as string or null if there is
     * no content.
     */
    @Override
    public String getContentString() throws IOException {
        //System.out.print(IOUtils.toString(inpStream, StandardCharsets.UTF_8));
        return inpStream != null ? IOUtils.toString(inpStream, StandardCharsets.UTF_8) : null;
    }

    /**
     * @return Returns the request content (body) as byte[] or null if there is
     * no content.
     */
    @Override
    public byte[] getContentBytes() throws IOException {
        return IOUtils.toByteArray(inpStream);
    }
}
