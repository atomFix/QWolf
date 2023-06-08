package com.quick.wolf.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.quick.wolf.exception.WolfException;
import org.junit.Ignore;
import org.junit.Test;

public class URLTest {

    Gson GSON =  new Gson();

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf() {
        URL actualValueOfResult = URL.valueOf("https://example.org/example");
        assertEquals("https", actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertEquals("example", actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertEquals("example.org", actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf2() {
        URL actualValueOfResult = URL.valueOf("?");
        assertNull(actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertNull(actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertNull(actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf3() {
        assertThrows(IllegalStateException.class, () -> URL.valueOf("://"));
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf4() {
        URL actualValueOfResult = URL.valueOf("/");
        assertNull(actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertEquals("", actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertNull(actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf5() {
        URL actualValueOfResult = URL.valueOf(":");
        assertNull(actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertNull(actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertEquals(":", actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf6() {
        assertThrows(WolfException.class, () -> URL.valueOf(""));
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf7() {
        assertThrows(IllegalStateException.class, () -> URL.valueOf(":/"));
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf8() {
        URL actualValueOfResult = URL.valueOf("https://example.org/exampleAsync");
        assertEquals("https", actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertEquals("example", actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertEquals("example.org", actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf9() {
        URL actualValueOfResult = URL.valueOf("?https://example.org/example");
        assertNull(actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertNull(actualValueOfResult.getPath());
        assertEquals(1, actualValueOfResult.getParameters().size());
        assertNull(actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf10() {
        URL actualValueOfResult = URL.valueOf("/:/");
        assertEquals("/", actualValueOfResult.getProtocol());
        assertEquals(0, actualValueOfResult.getPort());
        assertEquals("", actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertNull(actualValueOfResult.getHost());
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testValueOf11() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NumberFormatException: For input string: ":"
        //       at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
        //       at java.lang.Integer.parseInt(Integer.java:652)
        //       at java.lang.Integer.parseInt(Integer.java:770)
        //       at com.quick.wolf.rpc.URL.valueOf(URL.java:96)
        //   See https://diff.blue/R013 to resolve this issue.

        URL.valueOf("::");
    }

    /**
     * Method under test: {@link URL#valueOf(String)}
     */
    @Test
    public void testValueOf12() {
        URL actualValueOfResult = URL.valueOf(":42");
        assertNull(actualValueOfResult.getProtocol());
        assertEquals(42, actualValueOfResult.getPort());
        assertNull(actualValueOfResult.getPath());
        assertTrue(actualValueOfResult.getParameters().isEmpty());
        assertNull(actualValueOfResult.getHost());
    }

    @Test
    public void testValueOf13() {
        URL actualValueOfResult = URL.valueOf("wolf://127.0.0.1:8088?version=1.0");
        URL copy = actualValueOfResult.copy();
        System.out.println(GSON.toJson(actualValueOfResult));
        System.out.println(GSON.toJson(copy));
        System.out.println(actualValueOfResult.toFullString());
    }
}

