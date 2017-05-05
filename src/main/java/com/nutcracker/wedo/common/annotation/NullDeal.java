package com.nutcracker.wedo.common.annotation;

import java.lang.annotation.*;

/**
 * 标记该注解的bo字段做空值处理
 * Created by huh on 2017/2/21.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface NullDeal {

}