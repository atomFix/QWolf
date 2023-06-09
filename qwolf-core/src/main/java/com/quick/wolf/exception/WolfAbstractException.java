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

package com.quick.wolf.exception;



/**
 * @author maijunsheng
 * @version 创建时间：2013-5-30
 * 
 */
public abstract class WolfAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected WolfErrorMsg motanErrorMsg = WolfErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public WolfAbstractException() {
        super();
    }

    public WolfAbstractException(WolfErrorMsg motanErrorMsg) {
        super();
        this.motanErrorMsg = motanErrorMsg;
    }

    public WolfAbstractException(String message) {
        this(message, (WolfErrorMsg) null);
    }

    public WolfAbstractException(String message, WolfErrorMsg motanErrorMsg) {
        super(message);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public WolfAbstractException(String message, WolfErrorMsg motanErrorMsg, boolean writableStackTrace) {
        this(message, null, motanErrorMsg, writableStackTrace);
    }

    public WolfAbstractException(String message, Throwable cause, WolfErrorMsg motanErrorMsg, boolean writableStackTrace) {
        super(message, cause, false, writableStackTrace);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public WolfAbstractException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public WolfAbstractException(String message, Throwable cause, WolfErrorMsg motanErrorMsg) {
        super(message, cause);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public WolfAbstractException(Throwable cause) {
        super(cause);
    }

    public WolfAbstractException(Throwable cause, WolfErrorMsg motanErrorMsg) {
        super(cause);
        this.motanErrorMsg = motanErrorMsg;
    }

    @Override
    public String getMessage() {
        String message = getOriginMessage();

        return "error_message: " + message + ", status: " + motanErrorMsg.getStatus() + ", error_code: " + motanErrorMsg.getErrorCode();
//                + ",r=" + RpcContext.getContext().getRequestId();
    }

    public String getOriginMessage(){
        if (motanErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = motanErrorMsg.getMessage();
        }
        return message;
    }

    public int getStatus() {
        return motanErrorMsg != null ? motanErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return motanErrorMsg != null ? motanErrorMsg.getErrorCode() : 0;
    }

    public WolfErrorMsg getWolfErrorMsg() {
        return motanErrorMsg;
    }
}
