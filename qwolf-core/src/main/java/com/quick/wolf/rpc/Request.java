package com.quick.wolf.rpc;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/15:42
 */
public interface Request {

    /**
     * service interface
     *
     * @return
     */
    String getInterfaceName();

    /**
     * service method name
     *
     * @return
     */
    String getMethodName();

    /**
     * service method param desc (sign)
     *
     * @return
     */
    String getParamtersDesc();

    /**
     * service method param
     *
     * @return
     */
    Object[] getArguments();

    /**
     * get framework param
     *
     * @return
     */
    Map<String, String> getAttachments();

    /**
     * set framework param
     *
     * @return
     */
    void setAttachment(String name, String value);

    /**
     * request id
     *
     * @return
     */
    long getRequestId();

    /**
     * retries
     *
     * @return
     */
    int getRetries();

    /**
     * set retries
     */
    void setRetries(int retries);

    /**
     * set rpc protocol version. for compatible diffrent version.
     * this value must set by server end while decode finish.
     * it only used in local, will not send to remote.
     *
     * @param rpcProtocolVersion
     */
    void setRpcProtocolVersion(byte rpcProtocolVersion);

    byte getRpcProtocolVersion();

    /**
     * set the serialization number.
     * same to the protocol version, this value only used in server end for compatible.
     *
     * @param number
     */
    void setSerializeNumber(int number);

    int getSerializeNumber();

}
