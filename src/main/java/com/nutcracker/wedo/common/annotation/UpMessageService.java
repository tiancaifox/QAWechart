package com.nutcracker.wedo.common.annotation;

import java.lang.annotation.*;

/**上行消息服务注解
 * Created by huh on 2017/3/16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface UpMessageService {
    public String messageType();
}
