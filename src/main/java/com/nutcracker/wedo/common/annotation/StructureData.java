package com.nutcracker.wedo.common.annotation;

import java.lang.annotation.*;

/**
 * 消息中的构建信息，标注为该注解的字段将不在序列化为微信XML文件中收集
 * Created by huh on 2017/2/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE,ElementType.FIELD })
public @interface StructureData {

}
