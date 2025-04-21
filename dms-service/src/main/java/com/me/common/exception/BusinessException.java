package com.me.common.exception;


import com.me.common.enums.ResultCode;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4202794327033370476L;

    private String code;

    public BusinessException() {
        super();
    }

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(final ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(final ResultCode resultCode, final String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BusinessException(final Throwable cause) {
        super(cause);
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusinessException(final String code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
