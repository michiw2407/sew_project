package main.request;

import at.technikum.Interfaces.Request;
import main.url.UrlClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class RequestClass implements Request {

    private BufferedReader in;
    private String[] parameters;

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public static void main(String[] args) {
        System.out.println();
    }

    public RequestClass(InputStream in) throws Exception {
        this.in = new BufferedReader(new InputStreamReader(in));
        try {
            setParameters();
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private void setParameters() throws IOException {
        final String requestLine = in.readLine();
        this.parameters = requestLine.split("[ ]");
    }

    public void readMessage() throws IOException {
        if (in != null) {
            String raw;

            while ((raw = in.readLine()) != null) {
                System.out.println(raw);
            }
        }
    }

    /**
     * @return Returns true if the request is valid. A request is valid, if
     * method and url could be parsed. A header is not necessary.
     */
    @Override
    public boolean isValid() {
        if (parameters.length >= 3 && parameters[1].startsWith("/")) {
            for (HttpMethod h : HttpMethod.values()) {
                if (h.name().equals(parameters[0].toUpperCase())) {
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
        return this.isValid() ? parameters[0] : null;
    }

    /**
     * @return Returns a URL object of the request. Never returns null.
     */
    @Override
    public UrlClass getUrl() {
        return this.isValid() ? new UrlClass(parameters[1]) : null;
    }

    /**
     * @return Returns the request header. Never returns null. All keys must be
     * lower case.
     */
    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    /**
     * @return Returns the number of header or 0, if no header where found.
     */
    @Override
    public int getHeaderCount() {
        return 0;
    }

    /**
     * @return Returns the user agent from the request header
     */
    @Override
    public String getUserAgent() {
        return null;
    }

    /**
     * @return Returns the parsed content length request header. Never returns
     * null.
     */
    @Override
    public int getContentLength() {
        return 0;
    }

    /**
     * @return Returns the parsed content type request header. Never returns
     * null.
     */
    @Override
    public String getContentType() {
        return null;
    }

    /**
     * @return Returns the request content (body) stream or null if there is no
     * content stream.
     */
    @Override
    public InputStream getContentStream() {
        return null;
    }

    /**
     * @return Returns the request content (body) as string or null if there is
     * no content.
     */
    @Override
    public String getContentString() {
        return null;
    }

    /**
     * @return Returns the request content (body) as byte[] or null if there is
     * no content.
     */
    @Override
    public byte[] getContentBytes() {
        return new byte[0];
    }




}
