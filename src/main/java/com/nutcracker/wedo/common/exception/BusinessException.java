package com.nutcracker.wedo.common.exception;

/**
 * 业务异常基类 可以直接使用
 * Created by huh on 2017/2/21.
 */
public class BusinessException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6111129871895942897L;

    /**
     * BusinessException
     *
     * @param message
     *            message
     * @param e
     *            Throwable
     */
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * BusinessException
     *
     * @param message
     *            message
     */
    public BusinessException(String message) {
        this(message, null);
    }

}


