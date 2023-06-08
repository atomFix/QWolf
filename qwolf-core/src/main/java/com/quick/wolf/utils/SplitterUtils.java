package com.quick.wolf.utils;

import com.google.common.base.Splitter;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/15:53
 */
public class SplitterUtils {

    public final static Splitter REGISTRY_SPLITTER = Splitter.on(";").trimResults();

    public final static Splitter.MapSplitter MAP_QUERY_PARAM_SPLITTER = Splitter.on("&").trimResults().withKeyValueSeparator('=');

    public final static Splitter COMMA_SPLIT_PATTERN = Splitter.onPattern("\\s*[,]+\\s*");

}
