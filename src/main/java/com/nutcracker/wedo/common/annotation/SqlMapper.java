package com.nutcracker.wedo.common.annotation;

import java.lang.annotation.*;

/**
 * Created by huh on 2017/1/19.
 */
@Target(value={ElementType.TYPE})
@java.lang.annotation.Retention(value= RetentionPolicy.RUNTIME)
@Inherited
@Documented
public abstract @interface SqlMapper {

}
