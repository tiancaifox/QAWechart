package com.nutcracker.wedo.common.annotation;

import com.nutcracker.wedo.common.enumerate.FieldType;

import java.lang.annotation.*;

/**
 * 自定义字段类型
 * Created by huh on 2017/2/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface CustField {

    /**
     * 自定义字段类型
     * @return FieldType
     */
    public FieldType fieldType();
}
