package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.quick.wolf.common.WolfConstants;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/18:24
 */
public class WolfFrameworkUtil {

    public static String removeAsyncSuffix(String path) {
        if (!Strings.isNullOrEmpty(path) && path.endsWith(WolfConstants.ASYNC_SUFFIX)) {
            return path.substring(0, path.length() - WolfConstants.ASYNC_SUFFIX.length());
        }
        return path;
    }

}
