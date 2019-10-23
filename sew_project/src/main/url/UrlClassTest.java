package main.url;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlClassTest {
    
    @Test
    void rawUrlTest() {
        UrlClass url = new UrlClass("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker");
        Assertions.assertEquals("https://testurl.com/home/units/example.html?request=michael&lastname=weiss#anker", url.getRawUrl());
    }
}
