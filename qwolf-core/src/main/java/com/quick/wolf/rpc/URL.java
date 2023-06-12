package com.quick.wolf.rpc;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.exception.WolfException;
import com.quick.wolf.utils.StringUtils;
import com.quick.wolf.utils.UrlUtils;
import com.quick.wolf.utils.WolfFrameworkUtil;

import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/17:24
 */
public class URL {

    private String protocol;

    private String host;

    private int port;

    // interfaceName
    private String path;

    private Map<String, String> parameters;

    private final transient Map<String, Number> numberMap = Maps.newConcurrentMap();;

    public URL(String protocol, String host, int port, String path) {
        this(protocol, host, port, path, Maps.newHashMap());
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = removeAsyncPath(path);
        this.parameters = parameters;
    }

    public static URL valueOf(String url) {
        if (Strings.isNullOrEmpty(url)) {
            throw new WolfException("url is null");
        }
        String protocol = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = Maps.newHashMap();
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");

            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(StringUtils.urlDecode(part.substring(0, j)), StringUtils.urlDecode(part.substring(j + 1)));
                    } else {
                        part = StringUtils.urlDecode(part);
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }

        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0) host = url;
        return new URL(protocol, host, port, path, parameters);
    }

    public URL copy() {
        return new URL(protocol, host, port, path, this.parameters == null ? Maps.newHashMap() : Maps.newHashMap(this.getParameters()));
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = removeAsyncPath(path);
    }

    public String getVersion() {
        return getParameter(URLParamType.version.getName(), URLParamType.version.getValue());
    }

    public String getGroup() {
        return getParameter(URLParamType.group.getName(), URLParamType.group.getValue());
    }

    public String getApplication() {
        return getParameter(URLParamType.application.getName(), URLParamType.application.getValue());
    }

    public String getModule() {
        return getParameter(URLParamType.module.getName(), URLParamType.shareChannel.getValue());
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        String value = parameters.get(name);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public void addParameter(String name, String value) {
        if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(value)) {
            return;
        }
        parameters.put(name, value);
    }

    public void addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    public void removeParameter(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return;
        }
        this.parameters.remove(name);
    }

    public void addParameterIfAbsent(String name, String value) {
        if (hasParameter(name)) {
            return;
        }
        this.parameters.put(name, value);
    }

    public boolean hasParameter(String name) {
        return !Strings.isNullOrEmpty(this.parameters.get(name));
    }

    public Boolean getBooleanParameter(String name, boolean defaultValue) {
        String value = getParameter(name);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public Integer getIntParameter(String name, Integer defaultValue) {
        Number number = numberMap.get(name);
        if (Objects.nonNull(number)) {
            return number.intValue();
        }

        String value = getParameter(name);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        int result = Integer.parseInt(value);
        numberMap.put(name, result);
        return result;
     }

    public String getMethodParameter(String methodName, String paramDesc, String name) {
        String value = getParameter(StringUtils.join(WolfConstants.METHOD_CONFIG_PREFIX, methodName, "(", paramDesc, ").", name));
        if (Strings.isNullOrEmpty(value)) {
            return getParameter(name);
        }
        return value;
    }

    public String getMethodParameter(String methodName, String paramDesc, String name, String defaultValue) {
        String value = getMethodParameter(methodName, paramDesc, name);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    private String removeAsyncPath(String path) {
        return WolfFrameworkUtil.removeAsyncSuffix(path);
    }



    public String getSimpleString() {
        return StringUtils.join(getUri(), "?group=", getGroup());
    }

    public String getUri() {
        return StringUtils.join(protocol, WolfConstants.PROTOCOL_SEPARATOR, host, WolfConstants.PARAM_SEPARATOR, port, WolfConstants.PATH_SEPARATOR, path);
    }

    public String toFullString() {
        return StringUtils.join(getUri(), "?", UrlUtils.structureMapUrls(parameters, URLParamType.refreshTimestamp.name()));
    }

    public String getIdentity() {
        return StringUtils.join(protocol, WolfConstants.PROTOCOL_SEPARATOR, host, WolfConstants.PARAM_SEPARATOR, port,
                WolfConstants.PATH_SEPARATOR, getParameter(URLParamType.group.getName(), URLParamType.group.getValue()),
                WolfConstants.PATH_SEPARATOR, getPath(),
                WolfConstants.PATH_SEPARATOR, getParameter(URLParamType.version.getName(), URLParamType.version.getValue()),
                WolfConstants.PATH_SEPARATOR, getParameter(URLParamType.nodeType.getName()));
    }


    @Override
    public String toString() {
        return getSimpleString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return port == url.port && Objects.equals(protocol, url.protocol) && Objects.equals(host, url.host) && Objects.equals(path, url.path) && Objects.equals(parameters, url.parameters);
    }


    @Override
    public int hashCode() {
        return Objects.hash(protocol, host, port, path, parameters);
    }


    public String getServerPortStr() {
        return buildHostPortStr(host, port);
    }

    private static String buildHostPortStr(String host, int defaultPort) {
        if (defaultPort <= 0) {
            return host;
        }

        int idx = host.indexOf(":");
        if (idx < 0) {
            return host + ":" + defaultPort;
        }

        int port = Integer.parseInt(host.substring(idx + 1));
        if (port <= 0) {
            return host.substring(0, idx + 1) + defaultPort;
        }
        return host;
    }

}
