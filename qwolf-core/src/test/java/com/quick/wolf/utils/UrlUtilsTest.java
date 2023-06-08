package com.quick.wolf.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.junit.Ignore;

import org.junit.Test;

public class UrlUtilsTest {
    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs() {
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", new HashMap<>()).size());
        assertEquals(1, UrlUtils.parseURLs("42 Main St", new HashMap<>()).size());
        assertEquals(1, UrlUtils.parseURLs("?", new HashMap<>()).size());
        assertNull(UrlUtils.parseURLs(null, new HashMap<>()));
        assertEquals(1, UrlUtils.parseURLs("/", new HashMap<>()).size());
        assertEquals(1, UrlUtils.parseURLs(":", new HashMap<>()).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseURLs2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.quick.wolf.exception.WolfException: url is null
        //       at com.quick.wolf.rpc.URL.valueOf(URL.java:50)
        //       at com.quick.wolf.utils.UrlUtils.parseURL(UrlUtils.java:58)
        //       at com.quick.wolf.utils.UrlUtils.parseURLs(UrlUtils.java:31)
        //   See https://diff.blue/R013 to resolve this issue.

        UrlUtils.parseURLs(" , ", new HashMap<>());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseURLs3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: url missing protocol: "://"
        //       at com.quick.wolf.rpc.URL.valueOf(URL.java:77)
        //       at com.quick.wolf.utils.UrlUtils.parseURL(UrlUtils.java:58)
        //       at com.quick.wolf.utils.UrlUtils.parseURLs(UrlUtils.java:31)
        //   See https://diff.blue/R013 to resolve this issue.

        UrlUtils.parseURLs("://", new HashMap<>());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs4() {
        HashMap<String, String> params = new HashMap<>();
        params.put(" , ", " , ");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs5() {
        HashMap<String, String> params = new HashMap<>();
        params.put("protocol", "protocol");
        params.put(" , ", " , ");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs6() {
        HashMap<String, String> params = new HashMap<>();
        params.put("port", " , ");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs7() {
        HashMap<String, String> params = new HashMap<>();
        params.put(" , ", null);
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs8() {
        HashMap<String, String> params = new HashMap<>();
        params.put(" , ", "");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs9() {
        HashMap<String, String> params = new HashMap<>();
        params.put("protocol", "");
        params.put(" , ", " , ");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }

    /**
     * Method under test: {@link UrlUtils#parseURLs(String, Map)}
     */
    @Test
    public void testParseURLs10() {
        HashMap<String, String> params = new HashMap<>();
        params.put("port", "42");
        params.put("protocol", "protocol");
        params.put(" , ", " , ");
        assertEquals(1, UrlUtils.parseURLs("https://example.org/example", params).size());
    }
}

