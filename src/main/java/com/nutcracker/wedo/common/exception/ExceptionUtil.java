package com.nutcracker.wedo.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理工具类
 * Created by huh on 2017/2/21.
 */
@Slf4j
public class ExceptionUtil {

    /**
     * 未知异常处理， 记录异常日志，并抛出runtime类型异常
     *
     * @param message message
     * @param e e
     * @param sourceClass sourceClass
     */
    public static void wrapException(String message, Exception e, Class<?> sourceClass) {

        if (e == null) {
            log.error(message);
        } else {
            log.error(message, e);
            e.printStackTrace();
        }
        throw new RuntimeException(message, e);
    }

    /**
     * 未知异常处理， 记录异常日志，并抛出runtime类型异常
     *
     * @param message message
     * @param sourceClass sourceClass
     */
    public static void wrapException(String message, Class<?> sourceClass) {
        wrapException(message, null, sourceClass);
    }

    /**
     * 未知异常处理， 记录异常日志，并抛出runtime类型异常
     *
     * @param message message
     */
    public static void wrapException(String message) {
        wrapException(message, null, ExceptionUtil.class);
    }

    /**
     * 未知异常处理， 记录异常日志，并抛出runtime类型异常
     *
     * @param e e
     */
    public static void wrapException(Exception e) {
        wrapException(e.getMessage(), e, ExceptionUtil.class);
    }

    /**
     * 业务异常处理封装并抛出runtime类型异常
     *
     * @param args args
     * @param message message
     * @param e e
     * @param sourceClass sourceClass
     */
    public static void wrapBusinessException(String message, Object[] args, Exception e, Class<?> sourceClass) {
        if (e == null) {
            org.slf4j.LoggerFactory.getLogger(sourceClass).error(message, args);
        } else {
            org.slf4j.LoggerFactory.getLogger(sourceClass).error(message, args, e);
            e.printStackTrace();
        }
        throw new BusinessException(message, e);
    }

    /**
     * TODO
     *
     * @param message message
     * @param e e
     * @param sourceClass sourceClass
     */
    public static void wrapBusinessException(String message, Exception e, Class<?> sourceClass) {
        wrapBusinessException(message, null, e, sourceClass);
    }

    /**
     * TODO
     *
     * @param message message
     * @param args args
     * @param sourceClass sourceClass
     */
    public static void wrapBusinessException(String message, Object[] args, Class<?> sourceClass) {
        wrapBusinessException(message, args, null, sourceClass);
    }

    /**
     * 业务异常处理封装并抛出runtime类型异常
     *
     * @param message message
     * @param sourceClass sourceClass
     */
    public static void wrapBusinessException(String message, Class<?> sourceClass) {
        wrapBusinessException(message, null, null, sourceClass);
    }

    /**
     * 业务异常处理封装并抛出runtime类型异常
     *
     * @param message message
     */
    public static void wrapBusinessException(String message) {
        wrapBusinessException(message, null, null, ExceptionUtil.class);
    }

    /**
     * 内部异常处理封装并抛出runtime类型异常
     *
     * @param message message
     * @param e e
     * @param sourceClass sourceClass
     */
    public static void wrapInternalException(String message, Exception e, Class<?> sourceClass) {
        if (e == null) {
            log.error(message);
        } else {
            log.error(message, e);
        }
        throw new InternalException(message, e);
    }

    /**
     * 内部异常处理封装并抛出runtime类型异常
     *
     * @param message message
     * @param sourceClass sourceClass
     */
    public static void wrapInternalException(String message, Class<?> sourceClass) {
        wrapInternalException(message, null, sourceClass);
    }

    /**
     * 内部异常处理封装并抛出runtime类型异常
     *
     * @param message message
     */
    public static void wrapInternalException(String message) {
        wrapInternalException(message, null, ExceptionUtil.class);
    }

}
