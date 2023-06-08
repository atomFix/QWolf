package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.quick.wolf.common.WolfConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/09:22
 */
public class StringUtils {

    public static String join(Object... params) {
        return joinUseDelimiter(null, params);
    }

    public static String joinUseDelimiter(String joinSign, Object... params) {
        if (params == null || params.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (Strings.isNullOrEmpty(joinSign)) {
            for (Object param : params) {
                if (param == null || (param instanceof String && ((String) param).length() == 0)) {
                    continue;
                }
                builder.append(param);
            }
        } else {
            int index = 0;
            for (Object param : params) {
                if (param == null || (param instanceof String && ((String) param).length() == 0)) {
                    continue;
                }
                if (index < params.length -1) {
                    builder.append(param).append(joinSign);
                } else {
                    builder.append(param);
                }
            }
        }
        return builder.toString();
    }

    public static Integer parserInteger(String value) {
        if (value == null) {
            return WolfConstants.DEFAULT_INT_VALUE;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return WolfConstants.DEFAULT_INT_VALUE;
        }
    }

    public static String urlEncode(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return "";
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String urlDecode(String value) {
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            return "";
        }
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}
