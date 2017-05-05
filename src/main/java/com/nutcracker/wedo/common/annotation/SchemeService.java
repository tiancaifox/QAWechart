package com.nutcracker.wedo.common.annotation;

import java.lang.annotation.*;

/**推广方案的service注解，用于标注方案类型，以通过公共部分的schemeType路由到相关的service
 * Created by huh on 2017/3/16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface SchemeService {
    public String schemeType();
}

