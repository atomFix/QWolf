package com.quick.wolf.rpc;

import com.quick.wolf.exception.WolfServiceException;
import com.quick.wolf.protocol.rpc.RpcProtocolVersion;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/14:35
 */
public class DefaultResponse implements Response {

    private Object value;
    private Exception exception;
    private long requestId;
    private long processTime;
    private int timeout;
    /**
     * rpc协议版本兼容时可以回传一些额外的信息
     */
    private Map<String, String> attachments;
    private byte rpcProtocolVersion = RpcProtocolVersion.VERSION_1.getVersion();
    private int serializeNumber = 0;// default serialization is hessian2

    public DefaultResponse() {
    }

    public DefaultResponse(long requestId) {
        this.requestId = requestId;
    }

    public DefaultResponse(Response response) {
        this.value = response.getValue();
        this.exception = response.getException();
        this.requestId = response.getRequestId();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
        this.rpcProtocolVersion = response.getRpcProtocolVersion();
        this.serializeNumber = response.getSerializeNumber();
        this.attachments = response.getAttachments();
    }



    @Override
    public Object getValue() {
        if (exception != null) {
            throw exception instanceof RuntimeException ? (RuntimeException) exception : new WolfServiceException(exception.getMessage(), exception);
        }
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    @Override
    public long getProcessTime() {
        return processTime;
    }

    @Override
    public void setProcessTime(long time) {
        this.processTime = time;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    @Override
    public void setAttachment(String key, String value) {

    }

    @Override
    public byte getRpcProtocolVersion() {
        return rpcProtocolVersion;
    }

    @Override
    public void setRpcProtocolVersion(byte rpcProtocolVersion) {
        this.rpcProtocolVersion = rpcProtocolVersion;
    }

    @Override
    public int getSerializeNumber() {
        return serializeNumber;
    }

    @Override
    public void setSerializeNumber(int number) {
        this.serializeNumber = number;
    }
}
