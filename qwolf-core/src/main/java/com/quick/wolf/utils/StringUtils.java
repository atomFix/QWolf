package com.quick.wolf.utils;

import com.google.common.base.Strings;

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
                builder.append(param);
            }
        } else {
            int index = 0;
            for (Object param : params) {
                if (index < params.length -1) {
                    builder.append(param).append(joinSign);
                } else {
                    builder.append(param);
                }
            }
        }
        return builder.toString();
    }


}
