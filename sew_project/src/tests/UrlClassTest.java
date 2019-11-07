package tests;

import main.url.UrlClass;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class UrlClassTest {

    private UrlClass url;
    
    @Test
    void getRawUrl() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker");
        assertEquals("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker", url.getRawUrl());
    }

    @Test
    void getPath() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker");
        assertEquals("home/units/example.html", url.getPath());
    }

    @Test
    void getParameter() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss");

        assertEquals("michael", url.getParameter().get("request"));
        assertEquals("weiss", url.getParameter().get("lastname"));
    }

    @Test
    void getParameterCount() {
        url = new UrlClass("/example.html?request=michael&lastname=weiss#anker");
        assertEquals(2, url.getParameterCount());
    }

    @Test
    void getSegments() {
        url = new UrlClass("https://testurl.com/home/units/example.html");
        String[] answer = {"home", "units", "example.html"};
        Assert.assertArrayEquals(answer, url.getSegments());
    }

    @Test
    void getFileName() {
        url = new UrlClass("www.testurl.com/home/units/example.html");
        assertEquals("example.html", url.getFileName());
    }

    @Test
    void getExtension() {
        url = new UrlClass("https://testurl.com/home/units/example.html");
        assertEquals("html", url.getExtension());
    }

    @Test
    void getFragment() {
        url = new UrlClass("https://testurl.com");
        assertEquals("", url.getFragment());
    }
}
