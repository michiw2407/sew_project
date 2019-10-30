package main.response;

import at.technikum.Interfaces.Request;
import at.technikum.Interfaces.Response;
import main.url.UrlClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class ResponseClass implements Response {

    private String serverHeader;
    private byte[] cont;
    private String contentType;
    private int contentLength;
    private Map<String, String> headers = new HashMap<>();
    private StatusCode statusCode;

    public ResponseClass() {
        headers.put("Server", "BIF-SWE1-Server");
    }

    /**
     * @return Returns a writable map of the response headers. Never returns
     * null.
     */
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return Returns the content length or 0 if no content is set yet.
     */
    @Override
    public int getContentLength() {
        return contentLength;
    }

    /**
     * @return Gets the content type of the response.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType Sets the content type of the response.
     * @throws IllegalStateException A specialized implementation may throw a
     *                               InvalidOperationException when the content type is set by the
     *                               implementation.
     */
    @Override
    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }

    /**
     * @return Gets the current status code. An Exceptions is thrown, if no status code was set.
     */
    @Override
    public int getStatusCode() {
        if (statusCode == null)
            throw new IllegalArgumentException("null");
        else
            return statusCode.getStatusCode();

    }

    /**
     * @param status Sets the current status code.
     */
    @Override
    public void setStatusCode(int status) {
        statusCode = StatusCode.valueOf(Integer.toString(status));
    }

    /**
     * @return Returns the status code as string. (200 OK)
     */
    @Override
    public String getStatus() {
        if (statusCode == null)
            throw new IllegalArgumentException("null");
        else
            return "(" + statusCode.getStatusCode() + " " + statusCode.getDescription() + ")";
    }

    /**
     * Adds or replaces a response header in the headers map
     *
     * @param header
     * @param value
     */
    @Override
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    /**
     * @return Returns the Server response header. Defaults to "BIF-SWE1-Server".
     */
    @Override
    public String getServerHeader() {
        return serverHeader;
    }

    /**
     * Sets the Server response header.
     *
     * @param server
     */
    @Override
    public void setServerHeader(String server) {
        headers.put("Server", server);
    }

    /**
     * @param content Sets a string content. The content will be encoded in UTF-8.
     */
    @Override
    public void setContent(String content) {
        cont = content.getBytes();
    }

    /**
     * @param content Sets a byte[] as content.
     */
    @Override
    public void setContent(byte[] content) {
        cont = content;
    }

    /**
     * @param stream Sets the stream as content.
     */
    @Override
    public void setContent(InputStream stream) {
        try {
            cont = stream.readAllBytes();
        } catch (IOException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param network Sends the response to the network stream.
     */
    @Override
    public void send(OutputStream network) {

        try {
            if (statusCode == null || cont.length == 0) {
                throw new IllegalStateException("No status code or content set.");
            }

            StringBuilder respHeaders = new StringBuilder();

            respHeaders.append("HTTP/1.1 ").append(getStatus()).append("\n");
            respHeaders.append("Content-Length").append(getContentLength()).append("\n");

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                respHeaders.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            respHeaders.append("\n");

            network.write(respHeaders.toString().getBytes());
            network.write(cont);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
