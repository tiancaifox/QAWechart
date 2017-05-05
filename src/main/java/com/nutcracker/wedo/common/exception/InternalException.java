package com.nutcracker.wedo.common.exception;

/**
 * 程序内部异常基类，可以直接调用
 * Created by huh on 2017/2/21.
 */
public class InternalException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2135010861776951017L;

    /**
     * InternalException
     *
     * @param message
     *            message
     * @param e
     *            e
     */
    public InternalException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * InternalException
     *
     * @param message
     *            message
     */
    public InternalException(String message) {
        this(message, null);
    }

}
