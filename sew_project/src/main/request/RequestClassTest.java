package main.request;

import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

class RequestClassTest {

    private RequestClass requ;
    private String url = "/www.technikum-wien.at";

    @Test
    void isValid() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));
        assertTrue(requ.isValid());
    }

    @Test
    void getMethod() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));
        assertEquals("GET", requ.getMethod());
    }

    @Test
    void getUrl() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));
        assertEquals(url, requ.getUrl().getRawUrl());
    }

    @Test
    void getHeaders() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));

        assertThat(requ.getHeaders(), IsMapContaining.hasKey("host"));
        assertThat(requ.getHeaders(), IsMapContaining.hasKey("accept-encoding"));

        assertThat(requ.getHeaders(), IsMapContaining.hasValue("localhost"));
        assertThat(requ.getHeaders(), IsMapContaining.hasValue("gzip,deflate"));

    }

    @Test
    void getHeaderCount() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));
        assertEquals(requ.getHeaders().size(), requ.getHeaderCount());
    }

    @Test
    void getUserAgent() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url));
        assertEquals("Unit-Test-Agent/1.0", requ.getUserAgent());
    }

    @Test
    void getContentLength() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url, "GET", "hello world"));
        assertEquals(11, requ.getContentLength());
    }

    @Test
    void getContentType() throws Exception {
        requ = new RequestClass(RequestTestHelper.getValidRequestStream(url, "GET", "hello world"));
        assertEquals("application/x-www-form-urlencoded", requ.getContentType());
    }

    @Test
    void getContentStream() throws Exception {
        InputStream inpStream = RequestTestHelper.getValidRequestStream(url, "GET", "hello world");
        requ = new RequestClass(inpStream);
        assertEquals(inpStream, requ.getContentStream());
    }

    @Test
    void getContentString() throws Exception {
        InputStream inpStream = RequestTestHelper.getValidRequestStream(url, "GET", "hello world");
        requ = new RequestClass(inpStream);
        assertEquals(IOUtils.toString(inpStream, StandardCharsets.UTF_8), requ.getContentString());
    }

    @Test
    void getContentBytes() throws Exception {
        InputStream inpStream = RequestTestHelper.getValidRequestStream(url, "GET", "hello world");
        requ = new RequestClass(inpStream);
        assertEquals(IOUtils.toByteArray(inpStream), requ.getContentBytes());
    }
}