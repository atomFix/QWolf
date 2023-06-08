package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.exception.WolfException;
import com.quick.wolf.rpc.URL;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/10:07
 */
public class UrlUtils {

    public static List<URL> parseURLs(String address, Map<String, String> params) {
        if (Strings.isNullOrEmpty(address)) {
            return null;
        }
        Iterator<String> iterator = SplitterUtils.REGISTRY_SPLITTER.split(address).iterator();
        List<URL> urls = Lists.newArrayList();
        while (iterator.hasNext()) {
            urls.add(parseURL(iterator.next(), params));
        }
        return urls;
    }

    private static URL parseURL(String address, Map<String, String> defaults) {
        if (address == null || address.length() == 0) {
            return null;
        }

        Iterator<String> addresses = SplitterUtils.COMMA_SPLIT_PATTERN.split(address).iterator();
        String url = addresses.next();

        String defaultProtocol = defaults == null ? null : defaults.get("protocol");
        if (defaultProtocol == null || defaultProtocol.length() == 0) {
            defaultProtocol = URLParamType.protocol.getValue();
        }

        int defaultPort = StringUtils.parserInteger(defaults == null ? null : defaults.get("port"));
        String defaultPath = defaults == null ? null : defaults.get("path");
        Map<String, String> defaultParameters = defaults == null ? null : Maps.newHashMap(defaults);
        if (defaultParameters != null) {
            defaultParameters.remove("protocol");
            defaultParameters.remove("host");
            defaultParameters.remove("port");
            defaultParameters.remove("path");
        }
        URL u = URL.valueOf(url);
        u.addParameters(defaults);
        boolean changed = false;
        String protocol = u.getProtocol();
        String host = u.getHost();
        int port = u.getPort();
        String path = u.getPath();
        Map<String, String> parameters = Maps.newHashMap(u.getParameters());
        if ((protocol == null || protocol.length() == 0) && defaultProtocol != null && defaultProtocol.length() > 0) {
            changed = true;
            protocol = defaultProtocol;
        }

        if (port <= 0) {
            changed = true;
            port = defaultPort > 0 ? defaultPort : WolfConstants.DEFAULT_INT_VALUE;
        }
        if (path == null || path.length() == 0) {
            if (defaultPath != null && defaultPath.length() > 0) {
                changed = true;
                path = defaultPath;
            }
        }
        if (defaultParameters != null && defaultParameters.size() > 0) {
            for (Map.Entry<String, String> entry : defaultParameters.entrySet()) {
                String key = entry.getKey();
                String defaultValue = entry.getValue();
                if (defaultValue != null && defaultValue.length() > 0) {
                    String value = parameters.get(key);
                    if (value == null || value.length() == 0) {
                        changed = true;
                        parameters.put(key, defaultValue);
                    }
                }
            }
        }
        if (changed) {
            u = new URL(protocol, host, port, path, parameters);
        }
        return u;
    }

    public static Map<String, String> parserQueryParam(String rawRefer) {
        Map<String, String> params = Maps.newHashMap();
        if (Strings.isNullOrEmpty(rawRefer)) {
            return params;
        }
        String escape;
        try {
            escape = URLDecoder.decode(rawRefer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new WolfException("decode register has error!" + e.getMessage());
        }
        return SplitterUtils.MAP_QUERY_PARAM_SPLITTER.split(escape);
    }

    public static String urlsToString(List<URL> urls) {
        if (urls == null || urls.size() == 0) {
            return null;
        }
        return JoinerUtils.COMMA_JOINER.join(urls);
    }

    public static String structureMapUrls(Map<String, String> params, String... exclude) {
        if (params == null || params.size() == 0) {
            return null;
        }
        List<String> excludeList = Arrays.asList(exclude);
        Map<String, String> targetParam = params.entrySet().stream().filter(entry -> !excludeList.contains(entry.getKey()))
                .map(entry -> Map.entry(StringUtils.urlEncode(entry.getKey()), StringUtils.urlEncode(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (targetParam.size() == 0) {
            return null;
        }
        return JoinerUtils.MAP_QUERY_PARAM_JOINER.join(targetParam);
    }


    public static Map<String, Integer> parserExport(String export) {
        Map<String, Integer> result = Maps.newHashMap();
        if (Strings.isNullOrEmpty(export)) {
            return result;
        }
        for (String pp : SplitterUtils.COMMA_SPLIT_PATTERN.split(export)) {
            if (Strings.isNullOrEmpty(pp)) {
                continue;
            }
            String[] ppDetail = pp.split(":");
            if (ppDetail.length == 2) {
                result.put(ppDetail[0], Integer.parseInt(ppDetail[1]));
            } else if (ppDetail.length == 1) {
                if (WolfConstants.PROTOCOL_INJVM.equals(ppDetail[0])) {
                    result.put(ppDetail[0], WolfConstants.DEFAULT_INT_VALUE);
                } else {
                    int port = MathUtil.parseInt(ppDetail[0], WolfConstants.DEFAULT_INT_VALUE);
                    if (port <= 0) {
                        throw new WolfException("Export is malformed :" + export);
                    } else {
                        result.put(WolfConstants.PROTOCOL_WOLF, port);
                    }
                }
            } else {
                throw new WolfException("Export is malformed :" + export);
            }
        }
        return result;
    }
}
