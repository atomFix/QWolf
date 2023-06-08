package com.quick.wolf.utils;

import com.google.common.base.Joiner;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/16:01
 */
public class JoinerUtils {

    public final static Joiner.MapJoiner MAP_QUERY_PARAM_JOINER = Joiner.on("&").withKeyValueSeparator("=");
    public final static Joiner COMMA_JOINER = Joiner.on(",").skipNulls();

}
