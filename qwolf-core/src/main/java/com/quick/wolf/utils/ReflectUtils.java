package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/14:53
 */
public class ReflectUtils {

    public static final String PARAM_CLASS_SPLIT = ",";
    public static final String EMPTY_VALUE = "void";
    private static final ConcurrentMap<Class<?>, String> class2NameCache = Maps.newConcurrentMap();


    public static String getMethodDesc(Method method) {
        if (method == null) {
            return null;
        }
        String paramDesc = getMethodParamDesc(method);
        return getMethodDesc(method.getName(), paramDesc);
    }

    public static String getMethodDesc(String methodName, String paramDesc) {
        if (Strings.isNullOrEmpty(paramDesc)) {
            return methodName + "()";
        } else {
            return methodName + "(" + paramDesc + ")";
        }
    }

    public static String getMethodParamDesc(Method method) {
        if (method == null || method.getParameterCount() == 0) {
            return EMPTY_VALUE;
        }
        StringBuilder params = new StringBuilder();

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> type : parameterTypes) {
            params.append(getName(type)).append(PARAM_CLASS_SPLIT);
        }
        return params.substring(0, params.length() - 1);
    }

    public static String getName(Class<?> clz) {
        if (clz == null) {
            return null;
        }

        String className = class2NameCache.get(clz);
        if (className != null) {
            return className;
        }

        className = getNameWithoutCache(clz);
        class2NameCache.putIfAbsent(clz, className);
        return className;
    }

    private static String getNameWithoutCache(Class<?> clz) {
        if (!clz.isArray()) {
            return clz.getName();
        }

        StringBuilder sb = new StringBuilder();
        while (clz.isArray()) {
            sb.append("[]");
            clz = clz.getComponentType();
        }

        return clz.getName() + sb;
    }

}
