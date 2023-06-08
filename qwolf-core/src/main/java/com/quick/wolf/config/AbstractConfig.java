package com.quick.wolf.config;

import com.google.common.base.Strings;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.config.annotation.ConfigDesc;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.utils.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/18:14
 */
public abstract class AbstractConfig implements Serializable {

    private static final long serialVersionUID = 5811662746140019557L;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void collectConfigParams(Map<String, String> parameters, AbstractConfig... configs) {
        for (AbstractConfig config : configs) {
            if (config != null) {
                config.appendConfigParams(parameters);
            }
        }
    }

    public static void collectMethodConfigParams(Map<String, String> parameters, List<MethodConfig> methodConfigs) {
        if (methodConfigs == null || methodConfigs.isEmpty()) {
            return;
        }
        for (MethodConfig mc : methodConfigs) {
            if (mc != null) {
                mc.appendConfigParams(parameters, StringUtils.join(WolfConstants.METHOD_CONFIG_PREFIX, mc.getName(), "(", mc.getArgumentTypes(), ")"));
            }
        }
    }

    protected void appendConfigParams(Map<String, String> parameters) {
        appendConfigParams(parameters, null);
    }

    @SuppressWarnings("unchecked")
    protected void appendConfigParams(Map<String, String> parameters, String prefix) {
        Method[] methods = this.getClass().getMethods();
        try {
            for (Method method : methods) {
                String name = method.getName();
                if (isConfigMethod(method)) {
                    int index = name.startsWith("get") ? 3 : 2;
                    String key = name.substring(index, index + 1).toLowerCase() + name.substring(index + 1);
                    ConfigDesc configDesc = method.getAnnotation(ConfigDesc.class);
                    if (configDesc != null && !Strings.isNullOrEmpty(configDesc.key())) {
                        key = configDesc.key();
                    }
                    Object value = method.invoke(this);
                    if (value == null ||  Strings.isNullOrEmpty(String.valueOf(value))) {
                        if (configDesc != null && configDesc.required()) {
                            throw new WolfFrameworkException("config Desc require is true, but the value is null ! the key is : " + key);
                        }
                        continue;
                    }
                    parameters.put(wrapKeyPrefix(prefix, key), String.valueOf(value).trim());
                } else if (isParamsMethod(method)){
                    Map<String, String> map = (Map<String, String>) method.invoke(this);
                    if (map != null && map.size() != 0) {
                        map.forEach((key, value) -> parameters.put(wrapKeyPrefix(prefix, key), value));
                    }
                }
            }
        } catch (Exception e) {
            throw new WolfFrameworkException(e.getMessage());
        }
    }

    private boolean isConfigMethod(Method method) {
        boolean checkMethod = (method.getName().startsWith("get") || method.getName().startsWith("is")) && !"isDefault".equals(method.getName())
                && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0
                && isPrimitive(method.getReturnType());
        if (checkMethod) {
            ConfigDesc configDesc = method.getAnnotation(ConfigDesc.class);
            if (configDesc != null && configDesc.excluded()) {
                return false;
            }
        }
        return checkMethod;
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class || clazz == Integer.class || clazz == Short.class || clazz == Byte.class || clazz == Character.class || clazz == Boolean.class || clazz == Long.class
                || clazz == Float.class || clazz == Double.class;
    }

    private boolean isParamsMethod(Method method) {
        return method.getName().equals("getParameters") && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0 && Map.class.isAssignableFrom(method.getReturnType());
    }

    private String wrapKeyPrefix(String prefix, String key) {
        if (Strings.isNullOrEmpty(prefix)) {
            return key;
        }
        return prefix + "." + key;
    }


}
