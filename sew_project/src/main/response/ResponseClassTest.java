package main.response;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

class ResponseClassTest {

    private ResponseClass resp = new ResponseClass();

    @Test
    void getHeaders() {
        assertEquals(Map.of("Server", "BIF-SWE1-Server"), resp.getHeaders());
    }

    @Test
    void getContentLength() {
        String content = "hello world";
        resp.setContent(content);
        assertEquals(content.length(), resp.getContentLength());
    }

    @Test
    void getContentType() {
        String contentType = "html";
        resp.setContentType(contentType);
        assertEquals(contentType, resp.getContentType());
    }

    @Test
    void getStatusCode() {
        resp.setStatusCode(200);
        assertEquals(200, resp.getStatusCode());
    }


    @Test
    void getStatus() {
        resp.setStatusCode(200);
        assertEquals("(200 OK)", resp.getStatus());
    }


    @Test
    void getServerHeader() {
        String header = "TestHeader";
        resp.setServerHeader(header);
        assertEquals(Map.of("Server", header), resp.getHeaders());
    }

}