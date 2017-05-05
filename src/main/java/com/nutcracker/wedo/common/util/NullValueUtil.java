package com.nutcracker.wedo.common.util;

import com.nutcracker.wedo.common.annotation.NullDeal;
import com.nutcracker.wedo.common.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 空值处理工具
 * Created by huh on 2017/2/21.
 */
@Slf4j
public class NullValueUtil {

    /**
     * 字符串的空值
     */
    public static final String NULL_STRING = "";
    /**
     * integer空值
     */
    public static final Integer NULL_INTEGER = -1;
    /**
     * LONG空值
     */
    public static final Long NULL_LONG = -1L;
    /**
     * FLOAT空值
     */
    public static final Float NULL_FLOAT = -1.0f;
    /**
     * DATE空值
     */
    public static final Date NULL_DATE = DateUtils.parseDate("19700101", "yyyyMMdd");

    /**
     * 处理对象中的null为默认值
     * @param object 原始对象
     * @return object 处理后的对象
     */
    public static final Object dealNullValue(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(NullDeal.class) != null) {
                field.setAccessible(true);
                setNullValue(object, field);
            }
        }

        return object;
    }

    /**
     * 处理对象中的默认值为null
     * @param object 原始对象
     * @return object 处理后的对象
     */
    public static final Object reDealNullValue(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(NullDeal.class) != null) {
                field.setAccessible(true);
                resetNullValue(object, field);
            }
        }

        return object;
    }

    /**
     * 按字段处理对象中的空值为默认空值
     * @param object 对象
     * @param field 字段
     */
    private static final void setNullValue(Object object, Field field) {
        Class<?> clazz = field.getType();
        try {
            if (field.get(object) == null) {
                if (clazz.equals(String.class)) {
                    field.set(object, NULL_STRING);
                } else if (clazz.equals(Integer.class)) {
                    field.set(object, Integer.valueOf(NULL_INTEGER));
                } else if (clazz.equals(Long.class)) {
                    field.set(object, Long.valueOf(NULL_LONG));
                } else if (clazz.equals(Float.class)) {
                    field.set(object, Float.valueOf(NULL_FLOAT));
                } else if (clazz.equals(Date.class)) {
                    field.set(object, NULL_DATE);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ExceptionUtil.wrapException(e);
        }
    }
    /**
     * 按字段处理对象中默认空值改为null
     * @param object 对象
     * @param field 字段
     */
    private static final void resetNullValue(Object object, Field field) {
        try {
            if (field.get(object) != null) {
                if (field.get(object).equals(NULL_STRING)
                        || field.get(object).equals(NULL_INTEGER)
                        || field.get(object).equals(NULL_LONG)
                        || field.get(object).equals(NULL_FLOAT)
                        || field.get(object).equals(NULL_DATE)) {
                    field.set(object, null);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

