/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.quick.wolf.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liukairong1
 * @date 2023/06/08 18:46
 */
@Slf4j
public class MathUtil {


    /**
     * 针对int类型字符串进行解析，如果存在格式错误，则返回默认值（defaultValue）
     * Parse intStr, return defaultValue when numberFormatException occurs
     * @param intStr
     * @param defaultValue
     * @return
     */
    public static int parseInt(String intStr, int defaultValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            log.debug("ParseInt false, for malformed intStr:" + intStr);
            return defaultValue;
        }
    }

    /**
     * 针对long类型字符串进行解析，如果存在格式错误，则返回默认值（defaultValue）
     * Parse longStr, return defaultValue when numberFormatException occurs
     * @param longStr
     * @param defaultValue
     * @return
     */
    public static long parseLong(String longStr, long defaultValue){
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
}
