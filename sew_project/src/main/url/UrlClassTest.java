package main.url;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlClassTest {

    private UrlClass url;
    
    @Test
    void rawUrlTest() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker");
        Assertions.assertEquals("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker", url.getRawUrl());
    }

    @Test
    void pathTest() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker");
        Assertions.assertEquals("home/units/example.html", url.getPath());
    }

    @Test
    void parameterCountTest() {
        url = new UrlClass("/example.html?request=michael&lastname=weiss#anker");
        Assertions.assertEquals(2, url.getParameterCount());
    }

    @Test
    void getFileNameTest() {
        url = new UrlClass("www.testurl.com/home/units/example.html");
        Assertions.assertEquals("example.html", url.getFileName());
    }

    @Test
    void getExtensionTest() {
        url = new UrlClass("https://testurl.com/home/units/example.html");
        Assertions.assertEquals("html", url.getExtension());
    }

    @Test
    void getFragmentTest() {
        url = new UrlClass("https://testurl.com");
        Assertions.assertEquals("", url.getFragment());
    }

    @Test
    void getSegmentTest() {
        url = new UrlClass("https://testurl.com/home/units/example.html");
        String[] answer = {"home", "units", "example.html"};
        Assert.assertArrayEquals(answer, url.getSegments());
    }

    @Test
    void getParameterTest() {
        url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss");

        Assertions.assertEquals("michael", url.getParameter().get("request"));
        Assertions.assertEquals("weiss",url.getParameter().get("lastname"));
    }
}
