package main.response;

import interfaces.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class ResponseClass implements Response {

    private byte[] cont;

    private String contentType;
    private int statusCode;
    private String serverHeader;

    private String status;

    private int contentLength;
    private Map<String, String> headers = new HashMap<>();

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
        this.contentType = contentType;
    }

    /**
     * @return Gets the current status code. An Exceptions is thrown, if no status code was set.
     */
    @Override
    public int getStatusCode() {
        if (statusCode == 0)
            throw new IllegalArgumentException("null");
        else
            return statusCode;

    }

    /**
     * @param status Sets the current status code.
     */
    @Override
    public void setStatusCode(int status) {
        switch (status) {
            case 200:
                this.status = "200 OK";
                break;
            case 404:
                this.status = "404 Not Found";
                break;
            case 500:
                this.status = "500 Internal Server Error";
                break;
            default:
                this.status = "400 Bad Request";
        }

        statusCode = status;
    }

    /**
     * @return Returns the status code as string. (200 OK)
     */
    @Override
    public String getStatus() {
        if (statusCode == 0)
            throw new IllegalArgumentException("null");
        else
            return "(" + status + ")";
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
        contentLength = cont.length;
    }

    /**
     * @param content Sets a byte[] as content.
     */
    @Override
    public void setContent(byte[] content) {
        cont = content;
        contentLength = cont.length;
    }

    /**
     * @param stream Sets the stream as content.
     */
    @Override
    public void setContent(InputStream stream) {
        try {
            cont = stream.readAllBytes();
            contentLength = cont.length;
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
            if (status == null || cont.length == 0) {
                throw new IllegalStateException("No status code or content set.");
            }

            StringBuilder respHeaders = new StringBuilder();

            respHeaders.append("HTTP/1.1 ").append(getStatus()).append("\n");
            respHeaders.append("Content-Length: ").append(getContentLength()).append("\n");

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                respHeaders.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            respHeaders.append("\n");

            network.write(respHeaders.toString().getBytes());
            network.write(cont);

            network.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
