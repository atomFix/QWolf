package com.quick.wolf.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import com.google.gson.Gson;
import org.junit.Ignore;

import org.junit.Test;

public class RegisterConfigTest {
    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls() {
        assertEquals(1, (new RegisterConfig()).toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls2() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls3() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setName(" , ");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls4() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setRegisterProtocol(" , ");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls5() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("42 Main St");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls6() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setProxyRegister(new RegisterConfig());
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testToUrls7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.quick.wolf.exception.WolfFrameworkException: class java.lang.Integer cannot be cast to class java.lang.String (java.lang.Integer and java.lang.String are in module java.base of loader 'bootstrap')
        //       at com.quick.wolf.config.AbstractConfig.appendConfigParams(AbstractConfig.java:86)
        //       at com.quick.wolf.config.AbstractConfig.appendConfigParams(AbstractConfig.java:54)
        //       at com.quick.wolf.config.RegisterConfig.toUrls(RegisterConfig.java:170)
        //   See https://diff.blue/R013 to resolve this issue.

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setPort(1);
        registerConfig.toUrls();
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls8() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setProxyRegister(new RegisterConfig());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls9() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setName("");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testToUrls10() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.quick.wolf.exception.WolfException: url is null
        //       at com.quick.wolf.rpc.URL.valueOf(URL.java:50)
        //       at com.quick.wolf.utils.UrlUtils.parseURL(UrlUtils.java:58)
        //       at com.quick.wolf.utils.UrlUtils.parseURLs(UrlUtils.java:31)
        //       at com.quick.wolf.config.RegisterConfig.toUrls(RegisterConfig.java:190)
        //   See https://diff.blue/R013 to resolve this issue.

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress(" , ");
        registerConfig.appendConfigParams(new HashMap<>());
        registerConfig.toUrls();
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls11() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("?");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testToUrls12() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: url missing protocol: "://"
        //       at com.quick.wolf.rpc.URL.valueOf(URL.java:77)
        //       at com.quick.wolf.utils.UrlUtils.parseURL(UrlUtils.java:58)
        //       at com.quick.wolf.utils.UrlUtils.parseURLs(UrlUtils.java:31)
        //       at com.quick.wolf.config.RegisterConfig.toUrls(RegisterConfig.java:190)
        //   See https://diff.blue/R013 to resolve this issue.

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("://");
        registerConfig.appendConfigParams(new HashMap<>());
        registerConfig.toUrls();
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls13() {

        RegisterConfig proxyRegister = new RegisterConfig();
        proxyRegister.setPort(1);

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setProxyRegister(proxyRegister);
        registerConfig.appendConfigParams(new HashMap<>());
        registerConfig.toUrls();
    }

    /**
     * Method under test: {@link RegisterConfig#toUrls()}
     */
    @Test
    public void testToUrls14() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setRegisterProtocol("");
        registerConfig.appendConfigParams(new HashMap<>());
        assertEquals(1, registerConfig.toUrls().size());
    }

    @Test
    public void testToUrls15() {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setRegisterProtocol("");
        registerConfig.appendConfigParams(new HashMap<>());
        registerConfig.setAddress("wolf://127.0.0.1:8080;motan://3.0.91.1:8088;http://localhost?version=1");
        System.out.println(new Gson().toJson(registerConfig.toUrls()));
    }
}

