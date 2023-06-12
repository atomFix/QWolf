package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.rpc.DefaultResponse;
import com.quick.wolf.rpc.Request;
import com.quick.wolf.rpc.URL;

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


    /**
     * protocol key: protocol://host:port/group/interface/version
     *
     * @param url
     * @return
     */
    public static String getProtocolKey(URL url) {
        return url.getProtocol() + WolfConstants.PROTOCOL_SEPARATOR + url.getServerPortStr() + WolfConstants.PATH_SEPARATOR
                + url.getGroup() + WolfConstants.PATH_SEPARATOR + url.getPath() + WolfConstants.PATH_SEPARATOR + url.getVersion();
    }

    public static String getServiceKey(Request request) {
        String version = getVersionFromRequest(request);
        String group = getGroupFromRequest(request);

        return getServiceKey(group, request.getInterfaceName(), version);
    }

    /**
     * serviceKey: group/interface/version
     *
     * @param group
     * @param interfaceName
     * @param version
     * @return
     */
    private static String getServiceKey(String group, String interfaceName, String version) {
        return group + WolfConstants.PATH_SEPARATOR + interfaceName + WolfConstants.PATH_SEPARATOR + version;
    }


    public static String getVersionFromRequest(Request request) {
        return getValueFromRequest(request, URLParamType.version.name(), URLParamType.version.getValue());
    }

    public static String getGroupFromRequest(Request request) {
        return getValueFromRequest(request, URLParamType.group.name(), URLParamType.group.getValue());
    }

    public static String getValueFromRequest(Request request, String key, String defaultValue) {
        String value = defaultValue;
        if (request.getAttachments() != null && request.getAttachments().containsKey(key)) {
            value = request.getAttachments().get(key);
        }
        return value;
    }


    public static DefaultResponse buildErrorResponse(Request request, Exception e) {
        return buildErrorResponse(request.getRequestId(), request.getRpcProtocolVersion(), e);
    }


    public static DefaultResponse buildErrorResponse(long requestId, byte version, Exception e) {
        DefaultResponse response = new DefaultResponse();
        response.setRequestId(requestId);
        response.setRpcProtocolVersion(version);
        response.setException(e);
        return response;
    }


}
