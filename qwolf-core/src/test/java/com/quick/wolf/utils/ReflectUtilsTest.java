package com.quick.wolf.utils;

import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import com.quick.wolf.config.handler.ConfigHandler;
import org.junit.Ignore;
import org.junit.Test;

public class ReflectUtilsTest {
    /**
     * Method under test: {@link ReflectUtils#getMethodDesc(Method)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetMethodDesc() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.quick.wolf.utils.ReflectUtils.getMethodDesc(ReflectUtils.java:30)
        //       at com.quick.wolf.utils.ReflectUtils.getMethodDesc(ReflectUtils.java:23)
        //   See https://diff.blue/R013 to resolve this issue.

        ReflectUtils.getMethodDesc(null);
    }

    /**
     * Method under test: {@link ReflectUtils#getMethodDesc(Method)}
     */
    @Test
    public void testGetMethodDesc2() {
        assertNull(ReflectUtils.getMethodDesc(null));
    }


    @Test
    public void testGetMethodDesc3() {
        System.out.println(ReflectUtils.getMethodDesc(ConfigHandler.class.getMethods()[0]));
    }
}

