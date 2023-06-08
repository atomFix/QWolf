package com.quick.wolf.urtils;

import com.google.gson.Gson;
import com.quick.wolf.utils.UrlUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/10:20
 */

public class UrlUtilsTest {

    @Test
    public void testParserQueryParam() {
        Map<String, String> map = UrlUtils.parserQueryParam("query=java programming&name=101&%20value%20=%20ooo");
        System.out.println(new Gson().toJson(map));
        Assert.assertEquals(map.size(), 3);
    }

}
